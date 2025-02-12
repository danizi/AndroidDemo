package com.example.idphoto

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

                // 步骤4：显示结果
                maskedBitmap?.let { result ->
                    val displayBitmap = result.copy(result.config, true)
                    withContext(Dispatchers.Main) {
                        binding.ivPreview.setImageBitmap(displayBitmap)
                        // 可选：保存最终结果
//                        saveResultToGallery(displayBitmap)
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
    }
}