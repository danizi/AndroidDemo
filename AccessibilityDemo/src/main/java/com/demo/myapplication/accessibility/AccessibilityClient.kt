package com.demo.myapplication.accessibility

import android.view.View

/**
 * TIme:2025-01-24
 * Author:xm
 * Description:
 */
class AccessibilityClient {

    private var logFunction: (log: String) -> Unit = {}
    private var configs: List<AccessibilityConfig>? = null

    fun log(logFunction: (log:String)->Unit) = apply {
        this.logFunction = logFunction
    }

    fun configs(configs: List<AccessibilityConfig>) = apply {
        this.configs = configs
    }

    fun newCall(): IAccessibilityCall {
        return object :IAccessibilityCall {
            override fun setAccessibilityDelegate() {
                val cusAccessibilityDelegate = configs?.let {
                    CusAccessibilityDelegate(it, logFunction)
                }
                configs?.forEach { config ->
                    config.getView().isFocusableInTouchMode = true
                    config.getView().accessibilityDelegate = cusAccessibilityDelegate
                }
            }

            override fun talkBack(view: View) {
                configs?.forEach { config ->
                    if (view == config.getView()) {
                        config.getRuleInstance<RuleByTalk>().talkBack()
                    }
                }
            }

            override fun refreshFocus(view: View) {
                configs?.forEach { config ->
                    if (view == config.getView()) {
                        config.getRuleInstance<RuleRefreshFocusBySelf>().refreshFocus()
                    }
                }
            }
        }
    }
}