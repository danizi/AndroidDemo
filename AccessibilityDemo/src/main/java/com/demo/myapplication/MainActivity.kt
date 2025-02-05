package com.demo.myapplication

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.demo.myapplication.accessibility.AccessibilityClient
import com.demo.myapplication.accessibility.AccessibilityConfig
import com.demo.myapplication.accessibility.RuleByCondition
import com.demo.myapplication.accessibility.RuleByContentDescription
import com.demo.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val viewModel by lazy{MainActivityViewModel()}

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AccessibilityClient()
            .log { msg -> Log.d("MainActivity", msg) }
            .configs(accessibilityConfigs())
            .setAccessibilityDelegate()
    }

    private fun accessibilityConfigs(): List<AccessibilityConfig> {
        return listOf(
            // 其中的一个配置项
            AccessibilityConfig()
                .addRuleClass(RuleByCondition::class.java) { false }
                .addRuleClass(RuleByContentDescription::class.java) { "hello world" }
                .into(binding.button2)
        )
    }
}
