package com.demo.myapplication.accessibility

import android.view.View

/**
 * TIme:2025-01-25
 * Author:xm
 * Description:
 */
interface IAccessibilityCall {
    /**
     * 设置代理
     */
    fun setAccessibilityDelegate()

    /**
     * 控件内容发生变化主动播报
     */
    fun talkBack(view: View)

    /**
     * 刷新控件是否可获取焦点
     */
    fun refreshFocus(view: View)
}