package com.azamovhudstc.logosmart.utils

import android.content.Context
import android.graphics.Bitmap
import com.azamovhudstc.logosmart.ui.screens.camera_tasks.ModelActivity
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer

class YoloProcessor(
    context: Context,
    modelName: String = "chap_ong.tflite",
    labelsName: String = "labels.txt"
) {
    private val interpreter: Interpreter
    private val labels: List<String>
    private val imageProcessor: ImageProcessor

    init {
        val model = FileUtil.loadMappedFile(context, modelName)
        interpreter = Interpreter(model)
        labels = FileUtil.loadLabels(context, labelsName)
        imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(416, 416, ResizeOp.ResizeMethod.BILINEAR))
            .build()
    }
    fun process(bitmap: Bitmap): List<ModelActivity.DetectionResult> {
        val tensorImage = TensorImage.fromBitmap(bitmap)
        val processedImage = imageProcessor.process(tensorImage) // resize 416x416
        val inputTensorImage = TensorImage(org.tensorflow.lite.DataType.FLOAT32)
        inputTensorImage.load(processedImage.bitmap) // ← To‘g‘ri formatga o‘tkaziladi

        val inputBuffer = inputTensorImage.buffer

        val outputShape = intArrayOf(1, 10647, 7)
        val outputBuffer = TensorBuffer.createFixedSize(outputShape, org.tensorflow.lite.DataType.FLOAT32)

        interpreter.run(inputBuffer, outputBuffer.buffer.rewind())

        val results = mutableListOf<ModelActivity.DetectionResult>()
        val array = outputBuffer.floatArray
        val confidenceThreshold = 0.5f

        for (i in 0 until array.size step 7) {
            val conf = array[i + 4] * array[i + 6]
            if (conf > confidenceThreshold) {
                val labelIdx = array[i + 5].toInt()
                val label = labels.getOrElse(labelIdx) { "Unknown" }
                results.add(
                    ModelActivity.DetectionResult(
                        x = array[i],
                        y = array[i + 1],
                        w = array[i + 2],
                        h = array[i + 3],
                        confidence = conf,
                        label = label
                    )
                )
            }
        }

        return results
    }


    fun close() {
        interpreter.close()
    }
}
