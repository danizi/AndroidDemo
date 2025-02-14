package com.demo.myapplication.accessibility

import android.view.View
import android.view.accessibility.AccessibilityNodeInfo

/**
 * TIme:2025-02-05
 * Author:xm
 * Description:
 */
class RuleByCondition:AccessibilityConfig.IRule {

    override fun apply(info: AccessibilityNodeInfo, config: AccessibilityConfig) {
        config.getView().importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_NO
        info.isFocusable = config.invokeInfoFunction<Boolean>(RuleByCondition::class.java)!!
    }
}