package com.example.idphoto

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.*
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.idphoto.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.min
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    private var currentPersonBitmap: Bitmap? = null
    // 新增成员变量：保存原始透明人像
    private var originMaskedBitmap: Bitmap? = null
    // 添加这些成员变量
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var binding: ActivityMainBinding
    private var imageCapture: ImageCapture? = null

    // 权限请求码
    private val PERMISSION_REQUEST_CODE = 101

    // 所需权限列表
    private val REQUIRED_PERMISSIONS = mutableListOf<String>().apply {
        add(Manifest.permission.CAMERA)
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) { // API 28及以下
            add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }.toTypedArray()
    // 在MainActivity类顶部添加
    private var currentBgColor = Color.WHITE

    // 在类顶部添加尺寸定义
    private val PHOTO_SIZES = listOf(
        PhotoSize("1寸", 295, 413),
        PhotoSize("2寸", 413, 579),
        PhotoSize("小2寸", 413, 626),
        PhotoSize("5寸", 1500, 2100)
    )

    data class PhotoSize(
        val name: String,
        val widthPx: Int,
        val heightPx: Int
    )

    private var selectedSize: PhotoSize = PHOTO_SIZES[0]

    private fun showColorPicker() {
        val colors = listOf(
            Color.WHITE to "白色",
            Color.BLUE to "蓝色",
            Color.RED to "红色",
            Color.GREEN to "绿色",
            Color.parseColor("#FFD700") to "金色"
        )

        MaterialAlertDialogBuilder(this)
            .setTitle("选择背景颜色")
            .setItems(colors.map { it.second }.toTypedArray()) { _, which ->
                currentBgColor = colors[which].first
                applyBackgroundToPreview()
            }
            .setNegativeButton("取消", null)
            .show()
    }

    private fun applyBackgroundToPreview() {
        originMaskedBitmap?.let { masked ->
            // 生成新背景
            val background = Bitmap.createBitmap(
                masked.width,
                masked.height,
                Bitmap.Config.ARGB_8888
            ).apply { eraseColor(currentBgColor) }

            // 正确合成步骤
            val result = Bitmap.createBitmap(
                masked.width,
                masked.height,
                Bitmap.Config.ARGB_8888
            )

            Canvas(result).apply {
                // 关键修正：使用SRC_OVER模式
                drawBitmap(masked, 0f, 0f, null) // 先画透明人像
                drawBitmap(background, 0f, 0f, Paint().apply {
                    xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OVER)
                })
            }

            binding.ivPreview.setImageBitmap(result)
            background.recycle()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // toolbar初始化
        setSupportActionBar(binding.toolbar)
        // 初始化线程池
        cameraExecutor = Executors.newSingleThreadExecutor()

        if (checkPermissions()) {
            startCamera()
        } else {
            requestPermissions()
        }


        // 在onCreate中设置点击监听器
        binding.btnCapture.setOnClickListener {
            takePhoto()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_background -> {
                showColorPicker()
                true
            }
            R.id.menu_size -> {
                showSizeSelector()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showSizeSelector() {
        MaterialAlertDialogBuilder(this)
            .setTitle("选择证件照尺寸")
            .setItems(PHOTO_SIZES.map { "${it.name} (${it.widthPx}x${it.heightPx})" }.toTypedArray()) { _, which ->
                selectedSize = PHOTO_SIZES[which]
                applyPhotoSize()
            }
            .setNegativeButton("取消", null)
            .show()
    }

    private fun applyPhotoSize() {
        originMaskedBitmap?.let { original ->
            lifecycleScope.launch(Dispatchers.Default) {
                // 获取标准尺寸
                val targetWidth = selectedSize.widthPx
                val targetHeight = selectedSize.heightPx

                // 生成标准尺寸背景
                val background = Bitmap.createBitmap(
                    targetWidth,
                    targetHeight,
                    Bitmap.Config.ARGB_8888
                ).apply { eraseColor(currentBgColor) }

                // 保持人像原始比例缩放
                val scale = min(
                    targetWidth.toFloat() / original.width,
                    targetHeight.toFloat() / original.height
                )
                val scaledPerson = Bitmap.createScaledBitmap(
                    original,
                    (original.width * scale).toInt(),
                    (original.height * scale).toInt(),
                    true
                )

                // 居中绘制
                Canvas(background).apply {
                    drawBitmap(
                        scaledPerson,
                        (targetWidth - scaledPerson.width)/2f,
                        (targetHeight - scaledPerson.height)/2f,
                        null
                    )
                }

                withContext(Dispatchers.Main) {
                    // 关键：正确更新视图尺寸
                    binding.ivPreview.apply {
                        layoutParams = layoutParams.apply {
                            width = targetWidth
                            height = targetHeight
                        }
                        requestLayout()
                        setImageBitmap(background)
                    }

                    // 内存管理
                    currentPersonBitmap?.recycle()
                    currentPersonBitmap = background

                    // 验证日志
                    Log.d("SizeDebug", """
                    |目标尺寸: ${selectedSize.name} (${targetWidth}×${targetHeight})
                    |生成图片尺寸: ${background.width}×${background.height}
                    |ImageView尺寸: ${binding.ivPreview.width}×${binding.ivPreview.height}
                """.trimMargin())
                }
            }
        }
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val photoFile = File(
            getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "temp_${System.currentTimeMillis()}.jpg"
        )

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e("CameraX", "拍照失败: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    Log.d("CameraX", "照片保存成功: ${photoFile.absolutePath}")
                    processCapturedImage(photoFile)
                }
            }
        )
    }

    private fun processCapturedImage(photoFile: File) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                // 步骤1：压缩加载原始图片
                val options = BitmapFactory.Options().apply {
                    inJustDecodeBounds = true
                    BitmapFactory.decodeFile(photoFile.absolutePath, this)

                    // 计算缩放比例（保持长边不超过2048）
                    val maxSize = 2048
                    val (width, height) = when {
                        outWidth > outHeight -> outWidth to outHeight
                        else -> outHeight to outWidth
                    }
                    inSampleSize = if (width > maxSize) (width / maxSize) else 1
                    inJustDecodeBounds = false
                }
                val originalBitmap = BitmapFactory.decodeFile(photoFile.absolutePath, options)
                    ?: throw Exception("无法解码图片文件")

                // 步骤2：调整到模型输入尺寸
                val resizedBitmap = Bitmap.createScaledBitmap(
                    originalBitmap,
                    ImageSegmenterHelper.MODEL_INPUT_SIZE,
                    ImageSegmenterHelper.MODEL_INPUT_SIZE,
                    true
                )
                originalBitmap.recycle() // 及时回收原始大图

                // 步骤3：执行分割
                val segmenter = ImageSegmenterHelper(this@MainActivity)
                val maskedBitmap = segmenter.segment(resizedBitmap)
                resizedBitmap.recycle()
                segmenter.close()

                // 替换原有步骤4
                maskedBitmap?.let { result ->
                    originMaskedBitmap = result.copy(result.config, true) // 保存原始透明人像
                    currentPersonBitmap = result.copy(result.config, true)
                    withContext(Dispatchers.Main) {
                        applyBackgroundToPreview() // 应用背景颜色
                    }
                    result.recycle()
                }
            } catch (e: Exception) {
                Log.e("PhotoProcess", "图片处理失败", e)
            }
        }
    }

    private fun checkPermissions(): Boolean {
        return REQUIRED_PERMISSIONS.all { permission ->
            ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            REQUIRED_PERMISSIONS,
            PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                startCamera()
            } else {
                Toast.makeText(this, "需要授予所有权限才能使用相机", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // 创建预览用例
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            // 创建拍照用例
            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build()

            // 选择前置摄像头
            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                // 解绑所有用例
                cameraProvider.unbindAll()

                // 绑定用例到生命周期
                val camera = cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )

            } catch(exc: Exception) {
                Toast.makeText(this, "相机启动失败", Toast.LENGTH_SHORT).show()
                Log.e("CameraX", "绑定失败", exc)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
        // 释放Bitmap资源
        currentPersonBitmap?.recycle()
        binding.ivPreview.setImageBitmap(null)
    }
}