package com.demo.myapplication.accessibility

import android.view.View
import android.view.View.AccessibilityDelegate
import android.view.accessibility.AccessibilityNodeInfo

/**
 * TIme:2025-01-25
 * Author:xm
 * Description:
 */
class CusAccessibilityDelegate(
    private val configs: List<AccessibilityConfig>,
    private val logFunction: (log: String) -> Unit
) : AccessibilityDelegate() {

    override fun onInitializeAccessibilityNodeInfo(host: View, info: AccessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(host, info)
        configs.forEach { config ->
            if (host == config.getView()) {
                config.getRuleInstances().forEach { rule ->
                    logFunction.invoke("${rule::class.java.simpleName} - $config")
                    rule.apply(info, config)
                }
            }
        }
    }
}