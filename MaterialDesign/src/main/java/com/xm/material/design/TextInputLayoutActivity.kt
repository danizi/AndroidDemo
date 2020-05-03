package com.xm.material.design

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

/**
 * 需求：
 * 1 修改下划线颜色
 * 2 修改hint选中后的样式
 * 3 加入数字统计样式
 *   app:counterEnabled="true"      //设置是否显示字数统计
 *   app:counterMaxLength="15"      //设置字数统计的最大值
 *   app:counterOverflowTextAppearance="@style/counterOverFlowAppearance"   // 设置超出最大值后的样式
 *   app:counterTextAppearance="@style/counterAppearance"                   // 设置字数统计的样式
 * 4 加入错误提示以及样式
 *   app:errorEnabled="true"
 *   app:errorTextAppearance="@style/errorAppearance"
 *   til_til.setError("超出字数限制");
 *
 * 5 简化处理
 *   添加一个样式
 *   <style name="text_inp">
 *          <item name="android:colorAccent">#00ff00</item>              // 不起作用
 *          <item name="android:textColorHint">#674ea7</item>            // 默认hint颜色
 *          <item name="colorControlNormal">#000</item>                  // 不起作用
 *          <item name="colorControlActivated">#ff0</item>               // 获取焦点的时候的颜色
 *          <item name="textColorError">#0000ff</item>                   // 错误提示的颜色
 *          <item name="colorControlHighlight">@color/colorAccent</item> // 不起作用
 *   </style>
 *
 *  6 输入限制
 *    android:inputType="textPassword"
 */
class TextInputLayoutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_input_layout)
    }
}
