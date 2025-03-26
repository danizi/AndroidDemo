package com.demo.myapplication.accessibility

import android.view.View
import android.view.accessibility.AccessibilityNodeInfo

/**
 * TIme:2025-01-24
 * Author:xm
 * Description:
 */
class AccessibilityConfig {
    private lateinit var targetView: View

    val ruleInstanceMap = mutableMapOf<Class<*>, IRule>()

    private val infoFunctionMap = mutableMapOf<Class<*>, () -> Any>()

    /**
     * todo 有新的规则必须在此处添加,这里可以改成反射
     */
    private fun newRule(java: Class<*>) {
        if (ruleInstanceMap[java] == null) {
            when (java) {
                RuleByCondition::class.java -> {
                    ruleInstanceMap[java] = RuleByCondition()
                }
                RuleByContentDescription::class.java -> {
                    ruleInstanceMap[java] = RuleByContentDescription()
                }
                else -> {

                }
            }
        }
    }

    fun getView(): View {
        return targetView
    }

    fun <R : Any> addRuleClass(java: Class<*>, function: () -> R) = apply {
        newRule(java)
        infoFunctionMap[java] = function
    }

    fun <R> invokeInfoFunction(java: Class<*>): R? {
        val function = infoFunctionMap[java] as? () -> R // 显式转换为具体的函数类型
        return function?.invoke()
    }

    fun into(view: View) = apply {
        targetView = view
    }

    fun getRuleInstances(): List<IRule> {
        return ruleInstanceMap.values.toList()
    }

    inline fun < reified T:IRule> getRuleInstance(): T {
        return ruleInstanceMap[T::class.java] as T
    }

    interface IRule {
        fun apply(info: AccessibilityNodeInfo, config: AccessibilityConfig)
    }
}