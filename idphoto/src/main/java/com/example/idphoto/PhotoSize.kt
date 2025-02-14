package com.example.idphoto

/**
 * TIme:2025-02-14
 * Author:xm
 * Description:
 */
data class PhotoSize(
    val type: String,    // 证件类型
    val spec: String,    // 规格说明
    val widthPx: Int,    // 像素宽度
    val heightPx: Int,   // 像素高度
    val bgColor: Int,    // 背景颜色
    val dpi: Int = 300,  // 标准DPI
    val remark: String?  // 特殊要求
)