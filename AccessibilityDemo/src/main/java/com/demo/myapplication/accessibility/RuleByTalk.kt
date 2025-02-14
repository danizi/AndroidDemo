package com.demo.myapplication.accessibility

import android.view.accessibility.AccessibilityNodeInfo
import com.demo.myapplication.accessibility.AccessibilityConfig

/**
 * TIme:2025-01-25
 * Author:xm
 * Description:
 */
class RuleByTalk: AccessibilityConfig.IRule {

    fun talkBack(){

    }

    override fun apply(block: AccessibilityNodeInfo, config: AccessibilityConfig) {
        TODO("Not yet implemented")
    }
}