package com.azamovhudstc.logosmart.utils

import android.content.Context
import android.graphics.Bitmap
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

class YoloProcessor(
    context: Context,
    modelName: String = "chap_ong.tflite",
    labelsName: String = "labels.txt"
) {
    private val interpreter: Interpreter
    private val labels: List<String>
    private val imageProcessor: ImageProcessor

    init {
        // TFLite model va label fayllarni yuklaymiz
        val model = FileUtil.loadMappedFile(context, modelName)
        interpreter = Interpreter(model)
        labels = FileUtil.loadLabels(context, labelsName)

        // 416x416 resize + normalize [0, 1]
        imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(416, 416, ResizeOp.ResizeMethod.BILINEAR))
            .add(NormalizeOp(0f, 255f)) // FLOAT32 uchun normalizatsiya
            .build()
    }

    fun process(bitmap: Bitmap): List<DetectionResult> {
        val tensorImage = TensorImage(DataType.FLOAT32)
        tensorImage.load(bitmap)

        val processedImage = imageProcessor.process(tensorImage)
        val inputBuffer = processedImage.buffer

        // Modeldan chiqadigan natijaning shape'i
        val outputShape = intArrayOf(1, 10647, 7)
        val outputBuffer = TensorBuffer.createFixedSize(outputShape, DataType.FLOAT32)

        interpreter.run(inputBuffer, outputBuffer.buffer)

        val resultList = mutableListOf<DetectionResult>()
        val outputArray = outputBuffer.floatArray
        val confidenceThreshold = 0.5f

        for (i in 0 until outputArray.size step 7) {
            val x = outputArray[i]
            val y = outputArray[i + 1]
            val w = outputArray[i + 2]
            val h = outputArray[i + 3]
            val objScore = outputArray[i + 4]
            val classIdx = outputArray[i + 5].toInt().coerceIn(labels.indices)
            val classProb = outputArray[i + 6]

            val finalConfidence = objScore * classProb
            if (finalConfidence > confidenceThreshold) {
                val label = labels.getOrElse(classIdx) { "Unknown" }
                resultList.add(
                    DetectionResult(
                        x = x,
                        y = y,
                        w = w,
                        h = h,
                        confidence = finalConfidence,
                        label = label
                    )
                )
            }
        }

        return resultList
    }

    fun close() {
        interpreter.close()
    }

    // 👇 ModelActivity uchun mos natija klassi
    data class DetectionResult(
        val x: Float,
        val y: Float,
        val w: Float,
        val h: Float,
        val confidence: Float,
        val label: String
    )
}
