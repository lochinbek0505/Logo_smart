package com.example.yolotest

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.SurfaceView
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.scale
import com.azamovhudstc.logosmart.R
import com.azamovhudstc.logosmart.ml.Model
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import java.io.ByteArrayOutputStream
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private lateinit var previewView: PreviewView
    private lateinit var overlay: SurfaceView
    private lateinit var labels: List<String>
    private lateinit var model: Model
    private lateinit var imageProcessor: ImageProcessor

    private val executor = Executors.newSingleThreadExecutor()
    private val imageSize = Size(416, 416)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        previewView = findViewById(R.id.previewView)
        overlay = findViewById(R.id.overlay)
        overlay.setZOrderOnTop(true)
        overlay.holder.setFormat(PixelFormat.TRANSPARENT)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            loadModelAndLabels()
            startCamera()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 1)
        }
    }

    private fun loadModelAndLabels() {
        model = Model.newInstance(this)
        labels = FileUtil.loadLabels(this, "labels.txt")
        imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(416, 416, ResizeOp.ResizeMethod.BILINEAR))
            .build()
    }

    private fun runInference(bitmap: Bitmap): List<DetectionResult> {
        val resized = bitmap.scale(416, 416)
        val tensorImage = TensorImage(DataType.FLOAT32)
        tensorImage.load(resized)
        val inputImageBuffer = imageProcessor.process(tensorImage)
        val outputBuffer = model.process(inputImageBuffer.tensorBuffer).outputFeature0AsTensorBuffer
        val outputArray = outputBuffer.floatArray

        val numDetections = outputArray.size / 7
        val results = mutableListOf<DetectionResult>()
        val confidenceThreshold = 0.5f

        for (i in 0 until numDetections) {
            val offset = i * 7
            val x = outputArray[offset]
            val y = outputArray[offset + 1]
            val w = outputArray[offset + 2]
            val h = outputArray[offset + 3]
            val objConf = outputArray[offset + 4]
            val classId = outputArray[offset + 5].toInt()
            val classConf = outputArray[offset + 6]
            val finalConf = objConf * classConf
            if (finalConf > confidenceThreshold) {
                val label = labels.getOrElse(classId) { "Unknown" }
                Log.e("YOLO", "$label $objConf $classConf = $finalConf")
                results.add(DetectionResult(x, y, w, h, finalConf, label))
            }
        }
        return results
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }
            val imageAnalysis = ImageAnalysis.Builder()
                .setTargetResolution(imageSize)
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
            imageAnalysis.setAnalyzer(executor, ImageAnalysis.Analyzer { imageProxy ->
                val bitmap = imageProxyToBitmap(imageProxy)
                val rotatedBitmap = bitmap.rotate(270f) // Front camera correction
                val results = runInference(rotatedBitmap)
                runOnUiThread { drawResults(results) }
                imageProxy.close()
            })
            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis)
        }, ContextCompat.getMainExecutor(this))
    }

    private fun imageProxyToBitmap(imageProxy: ImageProxy): Bitmap {
        val yBuffer = imageProxy.planes[0].buffer
        val uBuffer = imageProxy.planes[1].buffer
        val vBuffer = imageProxy.planes[2].buffer

        val ySize = yBuffer.remaining()
        val uSize = uBuffer.remaining()
        val vSize = vBuffer.remaining()

        val nv21 = ByteArray(ySize + uSize + vSize)
        yBuffer.get(nv21, 0, ySize)
        vBuffer.get(nv21, ySize, vSize)
        uBuffer.get(nv21, ySize + vSize, uSize)

        val yuvImage = YuvImage(nv21, ImageFormat.NV21, imageProxy.width, imageProxy.height, null)
        val out = ByteArrayOutputStream()
        yuvImage.compressToJpeg(Rect(0, 0, imageProxy.width, imageProxy.height), 100, out)
        val imageBytes = out.toByteArray()
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

    private fun drawResults(results: List<DetectionResult>) {
        val canvas = overlay.holder.lockCanvas() ?: return
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        val paint = Paint().apply {
            color = Color.RED
            style = Paint.Style.STROKE
            strokeWidth = 4f
            textSize = 40f
            isAntiAlias = true
        }
        val best = results.maxByOrNull { it.confidence } ?: run {
            overlay.holder.unlockCanvasAndPost(canvas)
            return
        }
        val scaleX = overlay.width / 416f
        val scaleY = overlay.height / 416f
        val left = (best.x - best.w / 2) * scaleX
        val top = (best.y - best.h / 2) * scaleY
        val right = (best.x + best.w / 2) * scaleX
        val bottom = (best.y + best.h / 2) * scaleY

        canvas.drawRect(left, top, right, bottom, paint)
        canvas.drawText("${best.label} ${"%.2f".format(best.confidence)}", left, top - 10, paint)
        overlay.holder.unlockCanvasAndPost(canvas)
    }

    data class DetectionResult(
        val x: Float,
        val y: Float,
        val w: Float,
        val h: Float,
        val confidence: Float,
        val label: String
    )

    override fun onDestroy() {
        super.onDestroy()
        model.close()
    }

    private fun Bitmap.rotate(degrees: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degrees)
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }
}