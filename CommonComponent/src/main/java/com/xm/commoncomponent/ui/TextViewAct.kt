package com.xm.commoncomponent.ui

import android.graphics.Color
import android.graphics.MaskFilter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.*
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.xm.commoncomponent.ISetup
import com.xm.commoncomponent.R


class TextViewAct : AppCompatActivity(), ISetup {

    var tvImg: TextView? = null
    var tvCustom: TextView? = null
    var userSpannableStringBuilder = false

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
        tvCustom = findViewById(R.id.tv_custom)
    }

    override fun initEvent() {

    }

    override fun initDisplay() {
        //设置TextView顶部图片大小
        val drawable = tvImg?.compoundDrawables
        //0左 1上 2右 3下
        if (drawable != null) {
            drawable[0].setBounds(0, 0, 40, 40)
            drawable[1].setBounds(0, 0, 40, 40)
            tvImg?.setCompoundDrawables(drawable[0], drawable[1], drawable[2], drawable[3])
        }

        /*
         * SpannableString&SpannableStringBuilder定制化TextView 指定位置设置颜色 点击事件 图片等....
         * Spanable中的常用常量：
         * Spanned.SPAN_EXCLUSIVE_EXCLUSIVE --- 不包含start和end所在的端点                  (a,b)
         * Spanned.SPAN_EXCLUSIVE_INCLUSIVE --- 不包含端start，但包含end所在的端点          (a,b]
         * Spanned.SPAN_INCLUSIVE_EXCLUSIVE --- 包含start，但不包含end所在的端点            [a,b)
         * Spanned.SPAN_INCLUSIVE_INCLUSIVE--- 包含start和end所在的端点                     [a,b]
         *
         */
        if (userSpannableStringBuilder) {
            tvCustom?.text = getSpannableStringBuilder()
        } else {
            tvCustom?.text = getSpannableString()
        }
        tvCustom?.highlightColor = resources.getColor(android.R.color.transparent)//方法重新设置文字背景为透明色。
        tvCustom?.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun getSpannableString(): SpannableString {
        //SpannableString定制化TextView 指定位置设置颜色 点击事件 图片等....
        val spannableString = SpannableString("我是定制化TextView...")
        //文字背景颜色
        spannableString.setSpan(ForegroundColorSpan(Color.RED), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        //文本可点击
        spannableString.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                Toast.makeText(this@TextViewAct, "点击了第三个字", Toast.LENGTH_LONG).show()
            }
        }, 2, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        //文本效果修饰 模糊 浮雕
        spannableString.setSpan(MaskFilterSpan(MaskFilter()), 4, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        //文本删除线效果
        spannableString.setSpan(StrikethroughSpan(), 6, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        //文本超链接
        spannableString.setSpan(URLSpan("tel:15074770708"), 6, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        //文本添加图片
        val d = resources.getDrawable(R.mipmap.ic_launcher)
        d.setBounds(0, 0, d.intrinsicWidth, d.intrinsicHeight)
        spannableString.setSpan(ImageSpan(d, ImageSpan.ALIGN_BASELINE), 6, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannableString
    }

    private fun getSpannableStringBuilder(): SpannableStringBuilder {
        val spannableStringBuilder = SpannableStringBuilder("我是定制化TextView...")
        //文字背景颜色
        spannableStringBuilder.setSpan(ForegroundColorSpan(Color.RED), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        //文本可点击
        spannableStringBuilder.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                Toast.makeText(this@TextViewAct, "点击了第三个字", Toast.LENGTH_LONG).show()
            }
        }, 2, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        //文本效果修饰 模糊 浮雕
        spannableStringBuilder.setSpan(MaskFilterSpan(MaskFilter()), 4, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        //文本删除线效果
        spannableStringBuilder.setSpan(StrikethroughSpan(), 6, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        //文本超链接
        spannableStringBuilder.setSpan(URLSpan("tel:15074770708"), 6, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        tvCustom?.text = spannableStringBuilder
        tvCustom?.highlightColor = resources.getColor(android.R.color.transparent)//方法重新设置文字背景为透明色。
        tvCustom?.movementMethod = LinkMovementMethod.getInstance()
        return spannableStringBuilder
    }

    override fun initData() {

    }
}
