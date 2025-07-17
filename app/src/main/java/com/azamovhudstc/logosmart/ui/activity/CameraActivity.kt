package com.azamovhudstc.logosmart.ui.activity

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.azamovhudstc.logosmart.databinding.ActivityCameraBinding
import com.azamovhudstc.logosmart.utils.FaceLandmarkerHelper
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.facelandmarker.FaceLandmarkerResult
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.math.abs

class CameraActivity : AppCompatActivity(), FaceLandmarkerHelper.LandmarkerListener {

    private lateinit var binding: ActivityCameraBinding
    private lateinit var faceLandmarkerHelper: FaceLandmarkerHelper
    private lateinit var backgroundExecutor: ExecutorService

    private var preview: Preview? = null
    private var imageAnalyzer: ImageAnalysis? = null
    private var camera: Camera? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private var cameraFacing = CameraSelector.LENS_FACING_FRONT

    private var isMouthSuccess = false
    private var mouthCycleInProgress = false
    private var mouthMaxRatio = 0f

    private val mouthTriggerThreshold = 0.03f      // realistik og'iz ochilishi (~3%)
    private val mouthClosingDelta = 0.008f         // 0.8% kamayish og'iz yopildi deb hisoblanadi

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) startCameraWorkflow()
        else {
            Toast.makeText(this, "Kameraga ruxsat berilmadi", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (hasCameraPermission()) startCameraWorkflow()
        else requestPermissionLauncher.launch(Manifest.permission.CAMERA)
    }

    private fun hasCameraPermission() = ContextCompat.checkSelfPermission(
        this, Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED

    private fun startCameraWorkflow() {
        backgroundExecutor = Executors.newSingleThreadExecutor()

        binding.viewFinder.post { setUpCamera() }

        backgroundExecutor.execute {
            faceLandmarkerHelper = FaceLandmarkerHelper(
                context = this,
                runningMode = RunningMode.LIVE_STREAM,
                minFaceDetectionConfidence = 0.5f,
                minFaceTrackingConfidence = 0.5f,
                minFacePresenceConfidence = 0.5f,
                maxNumFaces = 1,
                currentDelegate = FaceLandmarkerHelper.DELEGATE_CPU,
                faceLandmarkerHelperListener = this
            )
        }
    }

    private fun setUpCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()
            bindCameraUseCases()
        }, ContextCompat.getMainExecutor(this))
    }

    private fun bindCameraUseCases() {
        val cameraProvider = cameraProvider ?: return
        val cameraSelector = CameraSelector.Builder().requireLensFacing(cameraFacing).build()

        preview = Preview.Builder()
            .setTargetAspectRatio(AspectRatio.RATIO_4_3)
            .setTargetRotation(binding.viewFinder.display.rotation)
            .build()

        imageAnalyzer = ImageAnalysis.Builder()
            .setTargetAspectRatio(AspectRatio.RATIO_4_3)
            .setTargetRotation(binding.viewFinder.display.rotation)
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
            .build()
            .also {
                it.setAnalyzer(backgroundExecutor) { image ->
                    faceLandmarkerHelper.detectLiveStream(
                        imageProxy = image,
                        isFrontCamera = cameraFacing == CameraSelector.LENS_FACING_FRONT
                    )
                }
            }

        cameraProvider.unbindAll()
        try {
            camera = cameraProvider.bindToLifecycle(
                this, cameraSelector, preview, imageAnalyzer
            )
            preview?.setSurfaceProvider(binding.viewFinder.surfaceProvider)
        } catch (e: Exception) {
            Log.e("Camera", "Bind failed", e)
        }
    }

    override fun onResults(resultBundle: FaceLandmarkerHelper.ResultBundle) {
        runOnUiThread {
            val result = resultBundle.result ?: return@runOnUiThread

            binding.overlay.setResults(
                result,
                resultBundle.inputImageHeight,
                resultBundle.inputImageWidth,
                RunningMode.LIVE_STREAM
            )
            binding.overlay.invalidate()

            // 👉 Har safar yuz ko‘ringanda borderni yangilash
            if (!isMouthSuccess) updateCardBorder()

            handleMouthCycle(result)
        }
    }


    private fun handleMouthCycle(result: FaceLandmarkerResult) {
        val ratio = getMouthOpenRatio(result)

        // sikl boshlanishi
        if (!mouthCycleInProgress && ratio > mouthTriggerThreshold) {
            mouthCycleInProgress = true
            mouthMaxRatio = ratio
        }

        // maksimalni yangilash
        if (mouthCycleInProgress && ratio > mouthMaxRatio) {
            mouthMaxRatio = ratio
        }

        // sikl tugadi va birinchi marta toʻliq sikl bo‘ldi
        if (mouthCycleInProgress && ratio < mouthMaxRatio - mouthClosingDelta && ratio < mouthTriggerThreshold) {
            mouthCycleInProgress = false

            // faqat birinchi marta bajarilsa holatni o‘zgartiramiz
            if (!isMouthSuccess) {
                isMouthSuccess = true
                showToast("Ogʻiz maksimal ochilib yopildi (max: ${"%.3f".format(mouthMaxRatio)})")
                updateCardBorder()
            }
        }
    }

    private fun updateCardBorder() {
        val color = ContextCompat.getColor(
            this,
            if (isMouthSuccess) android.R.color.holo_green_light else android.R.color.holo_red_dark
        )
        binding.camera.strokeColor = color
    }

    private fun getMouthOpenRatio(result: FaceLandmarkerResult): Float {
        val face = result.faceLandmarks().firstOrNull() ?: return 0f
        return abs(face[13].y() - face[14].y()) // 13 - pastki lab, 14 - yuqori lab
    }

    private fun showToast(message: String) {
        val timestamp = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        Toast.makeText(this, "[$timestamp] $message", Toast.LENGTH_SHORT).show()
    }

    override fun onEmpty() {
        binding.overlay.clear()
    }

    override fun onError(error: String, errorCode: Int) {
        runOnUiThread {
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        if (this::faceLandmarkerHelper.isInitialized && faceLandmarkerHelper.isClose()) {
            backgroundExecutor.execute {
                faceLandmarkerHelper.setupFaceLandmarker()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (this::faceLandmarkerHelper.isInitialized) {
            backgroundExecutor.execute {
                faceLandmarkerHelper.clearFaceLandmarker()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::backgroundExecutor.isInitialized) {
            backgroundExecutor.shutdown()
            backgroundExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        imageAnalyzer?.targetRotation = binding.viewFinder.display.rotation
    }
}
