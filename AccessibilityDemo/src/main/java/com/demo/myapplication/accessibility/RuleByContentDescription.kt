package com.demo.myapplication.accessibility

import android.view.accessibility.AccessibilityNodeInfo

/**
 * TIme:2025-02-05
 * Author:xm
 * Description:
 */
class RuleByContentDescription :AccessibilityConfig.IRule {
    override fun apply(info: AccessibilityNodeInfo, config: AccessibilityConfig) {
        info.contentDescription = config.invokeInfoFunction(RuleByContentDescription::class.java)
    }
}