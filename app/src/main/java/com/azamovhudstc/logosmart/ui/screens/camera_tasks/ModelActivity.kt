package com.azamovhudstc.logosmart.ui.screens.camera_tasks

import android.content.Intent
import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.util.Size
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.azamovhudstc.logosmart.R
import com.azamovhudstc.logosmart.databinding.ActivityModelBinding
import com.azamovhudstc.logosmart.utils.YoloProcessor
import com.bumptech.glide.Glide
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ModelActivity : AppCompatActivity() {
    private lateinit var binding: ActivityModelBinding
    private lateinit var processor: YoloProcessor
    private var cameraProvider: ProcessCameraProvider? = null
    private var imageAnalysis: ImageAnalysis? = null
    private var isRunning = true
    private var hasNavigated = false
    private var successStartTime: Long = 0L
    private var cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()
    private var modelIndex = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModelBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        modelIndex = intent.getIntExtra("ch", 1)
        modelIndex=1

        processor = when (modelIndex) {
            1 -> {
                Glide.with(this).asGif().load(R.drawable.ic_ong).into(binding.gif)
                YoloProcessor(this, "chap_ong.tflite", "labels.txt")
            }
            2 -> {
                Glide.with(this).asGif().load(R.drawable.ic_lab).into(binding.gif)
                YoloProcessor(this, "lab_ochiq.tflite", "labels2.txt")
            }
            3 -> {
                Glide.with(this).asGif().load(R.drawable.ic_tepa).into(binding.gif)
                YoloProcessor(this, "tepa_past.tflite", "labels3.txt")
            }
            else -> YoloProcessor(this, "tepa_past.tflite", "labels3.txt")
        }

        setupCamera()

        binding.next.setOnClickListener {
            if (hasNavigated) {
                lifecycleScope.launch { navigateToNext() }
            }
        }
    }

    private fun setupCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.previewView.surfaceProvider)
            }

            imageAnalysis = ImageAnalysis.Builder()
                .setTargetResolution(Size(416, 416))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build().also {
                    it.setAnalyzer(cameraExecutor) { imageProxy ->
                        processFrame(imageProxy)
                    }
                }

            val selector = CameraSelector.DEFAULT_FRONT_CAMERA
            cameraProvider?.unbindAll()
            cameraProvider?.bindToLifecycle(this, selector, preview, imageAnalysis)
        }, ContextCompat.getMainExecutor(this))
    }

    private fun processFrame(imageProxy: ImageProxy) {
        if (!isRunning) {
            imageProxy.close()
            return
        }

        val rotationDegrees = imageProxy.imageInfo.rotationDegrees.toFloat()
        val bitmap = imageProxyToBitmap(imageProxy).rotate(rotationDegrees)
        imageProxy.close()

        val results = processor.process(bitmap)

        results.firstOrNull()?.let { result ->
            runOnUiThread { updateUI(result) }
        }
    }

    private fun updateUI(result: YoloProcessor.DetectionResult) {
        when {
            result.confidence > 0.7f -> {
                binding.camera.setStrokeColor(Color.GREEN)
                if (successStartTime == 0L) successStartTime = System.currentTimeMillis()

                val elapsed = System.currentTimeMillis() - successStartTime
                if (!hasNavigated && elapsed >= 2000) {
                    hasNavigated = true
                    binding.next.setImageResource(R.drawable.next)
                }
            }
            result.confidence > 0.5f -> {
                binding.camera.setStrokeColor(Color.YELLOW)
                successStartTime = 0L
            }
            else -> {
                binding.camera.setStrokeColor(Color.RED)
                successStartTime = 0L
            }
        }
    }

    private suspend fun navigateToNext() {
        isRunning = false
        imageAnalysis?.clearAnalyzer()
        cameraProvider?.unbindAll()
        processor.close()
        delay(200)

        val intent = when (modelIndex) {
            1 -> Intent(this, GameActivity::class.java).putExtra("ch", 1)
            2 -> Intent(this, GameActivity::class.java).putExtra("ch", 2)
            3 -> Intent(this, VideoActivity::class.java).putExtra("ch", 1)
            else -> return
        }
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
        processor.close()
        imageAnalysis?.clearAnalyzer()
        cameraProvider?.unbindAll()
        cameraExecutor.shutdown()
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
        return BitmapFactory.decodeByteArray(out.toByteArray(), 0, out.size())
    }

    private fun Bitmap.rotate(degrees: Float): Bitmap {
        val matrix = Matrix().apply { postRotate(degrees) }
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }
}
