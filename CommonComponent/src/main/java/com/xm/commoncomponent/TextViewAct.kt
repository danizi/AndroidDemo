package com.xm.commoncomponent

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView

class TextViewAct : AppCompatActivity(), ISetup {

    var tvImg: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_view)
        findViews()
        initEvent()
        initDisplay()
        initData()
    }

    override fun findViews() {
        tvImg = findViewById(R.id.tv_img)
    }

    override fun initEvent() {

    }

    override fun initDisplay() {
        val drawable = tvImg?.compoundDrawables
        //0左 1上 2右 3下
        drawable!![0].setBounds(0, 0, 40, 40)
        drawable!![1].setBounds(0, 0, 40, 40)
        tvImg?.setCompoundDrawables(drawable[0], drawable[1], drawable[2], drawable[3])
    }

    override fun initData() {

    }
}
