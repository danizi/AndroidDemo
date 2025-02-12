package com.example.idphoto

import android.content.Context
import android.graphics.*
import android.util.Log
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import org.tensorflow.lite.task.vision.segmenter.ImageSegmenter
import org.tensorflow.lite.task.vision.segmenter.OutputType
import org.tensorflow.lite.task.vision.segmenter.Segmentation
import java.nio.ByteOrder

class ImageSegmenterHelper(context: Context) {
    // 模型配置参数
    companion object {
        const val MODEL_INPUT_SIZE = 257 // 必须与模型输入层匹配
        private const val TARGET_CLASS_INDEX = 15 // Pascal VOC人像类别索引
        private const val PERSON_CLASS_INDEX = 15 // Pascal VOC人像类别索引
        private const val BACKGROUND_CLASS_INDEX = 0

        private const val CONFIDENCE_THRESHOLD = 0.7f // 初始置信度阈值
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

        // 验证输出维度 [1, height, width]
//        if (shape.size != 3 || shape[0].toInt() != 1) {
//            Log.e("ProcessError", "无效输出维度: ${shape.contentToString()}")
//            return null
//        }
        Log.e("ProcessError", "无效输出维度: ${shape.contentToString()}")

        val height = shape[0].toInt()
        val width = shape[1].toInt()

        return when (buffer.dataType) {
            DataType.UINT8 -> createMaskFromUint8(buffer, width, height)
            DataType.FLOAT32 -> createMaskFromFloat(buffer, width, height)
            else -> null
        }?.let { applyMask(original, it) }
    }

    private fun createMaskFromUint8(buffer: TensorBuffer, width: Int, height: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val byteBuffer = buffer.buffer.duplicate().apply {
            rewind() // 重置读取位置
            order(ByteOrder.nativeOrder()) // 设置字节顺序
        }

        // 直接通过位置索引读取
        (0 until height).forEach { y ->
            (0 until width).forEach { x ->
                val pos = y * width + x
                val classId = byteBuffer.get(pos).toInt() and 0xFF // 无符号转换
                val alpha = if (classId == PERSON_CLASS_INDEX) 255 else 0
                bitmap.setPixel(x, y, Color.argb(alpha, 255, 255, 255))
            }
        }

        Log.d("BufferDebug", "首尾像素类别: ${byteBuffer.get(0)}, ${byteBuffer.get(width*height-1)}")
        return bitmap
    }


//    private fun processResult(original: Bitmap, segmentation: Segmentation?): Bitmap? {
//        segmentation ?: return null
//
//        val mask = segmentation.masks.first()
//        val tensorBuffer = mask.tensorBuffer
//
//        // 添加详细类型检查
//        return when (tensorBuffer.dataType) {
//            DataType.UINT8 -> {
//                Log.d("DataType", "检测到UINT8输出")
//                createMaskFromUint8(tensorBuffer, mask.width, mask.height)
//            }
//            DataType.FLOAT32 -> {
//                Log.d("DataType", "检测到FLOAT32输出")
//                createMaskFromFloat(tensorBuffer, mask.width, mask.height)
//            }
//            else -> {
//                Log.e("DataType", "不支持的输出类型: ${tensorBuffer.dataType}")
//                null
//            }
//        }?.let { applyMask(original, it) }
//    }
//
//    private fun createMaskFromUint8(buffer: TensorBuffer, width: Int, height: Int): Bitmap {
//        Log.d("MaskMeta", """
//        |- 数据类型: ${buffer.dataType}
//        |- 维度: ${buffer.shape.contentToString()}
//        |- 元素总数: ${buffer.flatSize}
//        |""".trimMargin()
//        )
//        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
//
//        // 兼容旧版API的数据获取方式
//        val byteBuffer = buffer.buffer
//        val byteArray = ByteArray(byteBuffer.remaining())
//        byteBuffer.get(byteArray)
//
//        for (y in 0 until height) {
//            for (x in 0 until width) {
//                val index = y * width + x
//                val classIndex = byteArray[index].toInt() and 0xFF // 无符号转换
//                val alpha = if (classIndex == TARGET_CLASS_INDEX) 255 else 0
//                bitmap.setPixel(x, y, Color.argb(alpha, 255, 255, 255))
//            }
//        }
//
//        Log.d("Uint8Process", "检测到UINT8类型数据，共处理${byteArray.size}字节")
//        return bitmap
//    }

    private fun createMaskFromFloat(buffer: TensorBuffer, width: Int, height: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val floatArray = buffer.floatArray // 此属性在0.4.4中可用

        for (y in 0 until height) {
            for (x in 0 until width) {
                val index = y * width + x
                val classIndex = floatArray[index].toInt()
                val alpha = if (classIndex == TARGET_CLASS_INDEX) 255 else 0
                bitmap.setPixel(x, y, Color.argb(alpha, 255, 255, 255))
            }
        }

        Log.d("FloatProcess", "检测到FLOAT32类型数据，共处理${floatArray.size}个浮点数")
        return bitmap
    }

    private fun applyMask(source: Bitmap, mask: Bitmap): Bitmap {
        val result = source.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(result)

        val paint = Paint().apply {
            xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        }

        canvas.drawBitmap(mask, 0f, 0f, paint)
        return result
    }

    fun close() {
        segmenter?.close()
    }
}
