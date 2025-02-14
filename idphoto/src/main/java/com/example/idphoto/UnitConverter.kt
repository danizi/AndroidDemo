package com.example.idphoto

import kotlin.math.roundToInt

/**
 * 单位转换工具类
 * TIme:2025-02-14
 * Author:xm
 * Description:
 */
object UnitConverter {
    // 毫米转像素（默认300dpi）
    fun mmToPx(mm: Float, dpi: Int = 300): Int {
        return (mm / 25.4f * dpi).roundToInt()
    }

    // 示例：中国护照33x48mm转换
    val passportWidthPx = mmToPx(33f)  // 输出390 (但以官方354px为准)
}
