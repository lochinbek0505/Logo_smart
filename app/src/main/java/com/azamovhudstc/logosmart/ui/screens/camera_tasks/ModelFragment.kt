package com.azamovhudstc.logosmart.ui.screens.camera_tasks

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.ImageFormat
import android.graphics.Matrix
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.YuvImage
import android.util.Log
import android.util.Size
import android.view.SurfaceView
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.core.graphics.scale
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.azamovhudstc.logosmart.R
import com.azamovhudstc.logosmart.databinding.FragmentModelBinding
import com.azamovhudstc.logosmart.ml.ChapOng
import com.azamovhudstc.logosmart.utils.BaseFragment
import com.azamovhudstc.logosmart.utils.animationTransaction
import com.bumptech.glide.Glide
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import java.io.ByteArrayOutputStream
import java.util.concurrent.Executors

class ModelFragment : BaseFragment<FragmentModelBinding>(FragmentModelBinding::inflate) {

    private lateinit var previewView: PreviewView
    private lateinit var overlay: SurfaceView
    private lateinit var labels: List<String>
    private var model: ChapOng? = null
    private lateinit var imageProcessor: ImageProcessor
    private val executor = Executors.newSingleThreadExecutor()
    private val imageSize = Size(416, 416)
    private var cameraProvider: ProcessCameraProvider? = null
    private var isRunning = true
    private var hasNavigated = false
    private var imageAnalysis: ImageAnalysis? = null

    override fun onViewCreate() {
        previewView = binding.root.findViewById(R.id.previewView)
        overlay = binding.root.findViewById(R.id.overlay)
        overlay.setZOrderOnTop(true)
        overlay.holder.setFormat(PixelFormat.TRANSPARENT)

        Glide.with(this).asGif().load(R.drawable.ic_lab).into(binding.gif)

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED
        ) {
            loadModelAndLabels()
            startCamera()
        } else {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), 1)
        }


        binding.next.setOnClickListener {
            if (hasNavigated){

                findNavController().navigate(
                    R.id.action_modelFragment_to_secondGame,
                    null,
                    animationTransaction().build()
                )
            }
        }
    }

    private fun loadModelAndLabels() {
        model = ChapOng.newInstance(requireContext())
        labels = FileUtil.loadLabels(requireContext(), "labels.txt")
        imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(416, 416, ResizeOp.ResizeMethod.BILINEAR))
            .build()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            imageAnalysis = ImageAnalysis.Builder()
                .setTargetResolution(imageSize)
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            imageAnalysis!!.setAnalyzer(executor, ImageAnalysis.Analyzer { imageProxy ->
                if (!isRunning || model == null) {
                    imageProxy.close()
                    return@Analyzer
                }

                try {
                    val bitmap = imageProxyToBitmap(imageProxy).rotate(270f)
                    val results = synchronized(this) { runInference(bitmap) }
                    imageProxy.close()
                } catch (e: Exception) {
                    Log.e("ModelFragment", "Analyzer crash", e)
                    imageProxy.close()
                }
            })

            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
            cameraProvider?.unbindAll()
            cameraProvider?.bindToLifecycle(
                viewLifecycleOwner,
                cameraSelector,
                preview,
                imageAnalysis
            )

        }, ContextCompat.getMainExecutor(requireContext()))
    }
    private fun runInference(bitmap: Bitmap): List<DetectionResult> {
        val resized = bitmap.scale(416, 416)
        val tensorImage = TensorImage(DataType.FLOAT32)
        tensorImage.load(resized)
        val inputBuffer = imageProcessor.process(tensorImage)

        val outputBuffer = model?.process(inputBuffer.tensorBuffer)?.outputFeature0AsTensorBuffer
            ?: return emptyList()
        val outputArray = outputBuffer.floatArray

        val results = mutableListOf<DetectionResult>()
        val confidenceThreshold = 0.5f
        val numDetections = outputArray.size / 7

        for (i in 0 until numDetections) {
            val offset = i * 7
            val finalConf = outputArray[offset + 4] * outputArray[offset + 6]
            if (finalConf > confidenceThreshold) {
                val label = labels.getOrElse(outputArray[offset + 5].toInt()) { "Unknown" }
                results.add(
                    DetectionResult(
                        outputArray[offset],
                        outputArray[offset + 1],
                        outputArray[offset + 2],
                        outputArray[offset + 3],
                        finalConf,
                        label
                    )
                )
                startMonitoring(finalConf)
            }
        }

        return results
    }

    private var firstCallTime: Long? = null

    private fun startMonitoring(confidence: Float) {
        val now = System.currentTimeMillis()

        // 3 sekunddan keyin bu funksiyani ishlatmaslik
        if (firstCallTime != null && now - firstCallTime!! > 3000) {

            binding.next.setImageResource(R.drawable.next)
            return
        }

        // Birinchi chaqirilgan vaqti saqlanadi
        if (firstCallTime == null) {
            firstCallTime = now
        }

        if (hasNavigated) return

        viewLifecycleOwner.lifecycleScope.launch {
            delay(2000)
            if (!isAdded || !isResumed || !isRunning) return@launch

            updateCardStroke(confidence)
//            pauseCameraAndModel()

            if (findNavController().currentDestination?.id == R.id.modelFragment) {
                hasNavigated = true
            }
        }
    }


    private fun updateCardStroke(confidence: Float) {
        val strokeColor = when {
            confidence > 0.7f -> Color.GREEN
            confidence > 0.5f -> Color.YELLOW
            else -> Color.RED
        }
        binding.camera.setStrokeColor(strokeColor)
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

    private fun pauseCameraAndModel() {
        isRunning = false

        try {
            imageAnalysis!!.clearAnalyzer()  // 🔒 Analyzer to‘xtatiladi
        } catch (e: Exception) {
            Log.e("ModelFragment", "Analyzer clear error", e)
        }
        try {
            model?.close()  // 🧠 Modelni to‘liq yopish
            model = null
        } catch (e: Exception) {
            Log.e("ModelFragment", "Model close error", e)
        }
        try {
            cameraProvider?.unbindAll()  // 📷 Kamerani o‘chirish
        } catch (e: Exception) {
            Log.e("ModelFragment", "Camera unbind error", e)
        }


    }


    override fun onPause() {
        super.onPause()
        model?.close()  // 🧠 Modelni to‘liq yopish

    }

    override fun onDestroyView() {
        super.onDestroyView()
        model?.close()  // 🧠 Modelni to‘liq yopish

    }

    private fun Bitmap.rotate(degrees: Float): Bitmap {
        val matrix = Matrix().apply { postRotate(degrees) }
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }

    data class DetectionResult(
        val x: Float,
        val y: Float,
        val w: Float,
        val h: Float,
        val confidence: Float,
        val label: String
    )
}
