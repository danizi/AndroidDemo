package com.xm.commoncomponent.ui

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.xm.commoncomponent.ISetup
import com.xm.commoncomponent.R


/**
 * 密码输入框 https://www.jianshu.com/p/69d11b7972c2
 */
class EditTextAct : AppCompatActivity(), ISetup {
    private val TAG = "EditTextAct"
    private var et1: EditText? = null
    private var btnEnter: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_text)
        findViews()
        initEvent()
        initDisplay()
        initData()
    }

    override fun findViews() {
        et1 = findViewById(R.id.et_1)
        btnEnter = findViewById(R.id.btn_enter)
    }

    override fun initEvent() {
        et1?.setOnEditorActionListener { v, actionId, event ->
            Log.e(TAG, "输入完点击确认执行该方法输入结束")
            false
        }

        et1?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                /*输入后的监听*/
                Log.d(TAG, "afterTextChanged " + s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                /*输入前的监听*/
                Log.d(TAG, "beforeTextChanged s:" + s + " start:" + start + "count:" + count + "after:" + after)
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                /*输入的内容变化的监听*/
                Log.d(TAG, "onTextChanged s:" + s + " start:" + start + "count:" + count)
                if (s?.length!! >= 5) {
                    btnEnter?.isEnabled = true
                    btnEnter?.setBackgroundColor(Color.parseColor("#D81B60"))
                } else {
                    btnEnter?.isEnabled = false
                    btnEnter?.setBackgroundColor(Color.parseColor("#008577"))
                }
            }
        })

        btnEnter?.setOnClickListener {
            Toast.makeText(this@EditTextAct, "btnEnter", Toast.LENGTH_LONG).show()
        }
    }

    override fun initDisplay() {

    }

    /**
     * 一次键清空内容
     * 不可编辑
     * 去掉下划线或者设置下划线颜色 https://blog.csdn.net/u014133119/article/details/80653568
     * requestFocus()
     * clearFocus()
     *  清单文件设置窗口模式 弹出键盘内容上顶
     * EditText的三种监听内容的方式
     * 光标控制
     */
    override fun initData() {

    }

    private fun show() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    private fun show2(view: View?) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED)
        imm.hideSoftInputFromWindow(view?.windowToken, 0) //强制隐藏键盘
    }
}
