package com.example.idphoto

import android.content.Context
import android.graphics.*
import android.util.Log
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.vision.segmenter.ImageSegmenter
import org.tensorflow.lite.task.vision.segmenter.OutputType
import org.tensorflow.lite.task.vision.segmenter.Segmentation

class ImageSegmenterHelper(context: Context) {
    // 模型配置参数
    companion object {
        const val MODEL_INPUT_SIZE = 257 // 必须与模型输入层匹配
        private const val PERSON_CLASS_INDEX = 15 // Pascal VOC人像类别索引
    }

    private var segmenter: ImageSegmenter? = null

    init {
        initializeSegmenter(context)
    }

    private fun initializeSegmenter(context: Context) {
        try {
            val options = ImageSegmenter.ImageSegmenterOptions.builder()
                .setOutputType(OutputType.CATEGORY_MASK)
                .build()

            segmenter = ImageSegmenter.createFromFileAndOptions(
                context,
                "2.tflite", // 确保模型文件在assets目录
//                "deeplabv3.tflite", // 确保模型文件在assets目录
                options
            )
            Log.d("Segmenter", "模型加载成功")
        } catch (e: Exception) {
            Log.e("Segmenter", "模型初始化失败", e)
        }
    }

    // ImageSegmenterHelper.kt
    fun segment(bitmap: Bitmap): Bitmap? {
        // 严格尺寸验证
        if (bitmap.width != 257 || bitmap.height != 257) {
            val scaled = Bitmap.createScaledBitmap(bitmap, 257, 257, true)
            bitmap.recycle()
            return processSegmentation(scaled)
        }
        return processSegmentation(bitmap)
    }

    private fun processSegmentation(bitmap: Bitmap): Bitmap? {
        return try {
            val tensorImage = TensorImage.fromBitmap(bitmap)
            val results = segmenter?.segment(tensorImage)
            processResult(bitmap, results?.first())
        } catch (e: Exception) {
            Log.e("Segmenter", "分割失败", e)
            null
        } finally {
            bitmap.recycle()
        }
    }
    private fun processResult(original: Bitmap, segmentation: Segmentation?): Bitmap? {
        segmentation ?: return null

        val mask = segmentation.masks.first()
        val buffer = mask.tensorBuffer
        val shape = buffer.shape

        Log.d("BufferDebug", """
        |- Buffer类型: ${buffer.buffer.javaClass.simpleName}
        |- 容量: ${buffer.buffer.capacity()}
        |- 当前位置: ${buffer.buffer.position()}
        |- 剩余字节: ${buffer.buffer.remaining()}
        |""".trimMargin())

        Log.e("ProcessError", "输出维度: ${shape.contentToString()}")

        // 动态获取类别索引
        val (width, height) = mask.width to mask.height
        val maskBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        (0 until height).forEach { y ->
            (0 until width).forEach { x ->
                val classId = when (buffer.dataType) {
                    DataType.UINT8 -> buffer.getIntValue(y * width + x).toInt()
                    DataType.FLOAT32 -> buffer.getFloatValue(y * width + x).toInt()
                    else -> -1
                }
                // 核心逻辑：仅保留人像区域
                val alpha = if (classId == PERSON_CLASS_INDEX) 255 else 0
                maskBitmap.setPixel(x, y, Color.argb(alpha, 255, 255, 255))
            }
        }

        return applyMask(original, maskBitmap)
    }

    private fun applyMask(source: Bitmap, mask: Bitmap): Bitmap {
        val result = Bitmap.createBitmap(
            source.width,
            source.height,
            Bitmap.Config.ARGB_8888 // 必须使用ARGB格式
        )

        Canvas(result).apply {
            // 先绘制原始图像
            drawBitmap(source, 0f, 0f, null)
            // 应用蒙版（保留人像区域）
            drawBitmap(mask, 0f, 0f, Paint().apply {
                xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
            })
        }
        return result
    }


    fun close() {
        segmenter?.close()
    }
}
