package com.xm.material.design

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem

/**
 * AppBarLayout
 * CoordinatorLayout
 * CollapsingToolbarLayout 顶部伸缩
 */
class ToolbarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_toolbar)
        setActionBar()
        setToolbar()
    }

    private fun setToolbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        //隐藏或者在actvity中设置theme央视@android:style/Theme.holo.NoActionBar”
        supportActionBar?.hide()
        //支持ActionBar
        setSupportActionBar(toolbar)
        //ActionBar设置必须在setSupportActionBar(toolbar)之后
        setActionBar()
        toolbar.setTitleTextColor(Color.WHITE)
        //与ActionBar不同，我们可以不用重写onOptionsMenuItemSelected方法
        toolbar.setOnMenuItemClickListener {
            finish()
            true
        }
    }

    /**
     *     一、Actionbar的使用用途
     *  1． Actionbar的五大用途：可以使用图标做导航
     *  2． 提供导航标签
     *  3． 提供下拉列表导航
     *  4． 为菜单添加动作视图
     *  5． 为菜单添加Actionprovide
     */
    private fun setActionBar() {
        //显示返回箭头,默认是不显示的
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //显示左侧的返回箭头，并且返回箭头和title一直设置返回箭头才能显示
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayUseLogoEnabled(true)
        //显示标题
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.title = "1111"
    }

    /**
     * 设置右边菜单选项
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        //布局添加
        menuInflater.inflate(R.menu.toobarbar, menu)
        //代码添加
        menu.add(0, 0, 0, "Settings")
        menu.add(0, 1, 1, "test")
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {

        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * 设置自定义ActionBar
     */
    private fun setCustomActionBar() {
        if (supportActionBar != null) {
            //在自定义的actionbar中设置
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            supportActionBar?.setDisplayShowHomeEnabled(false)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            //设置自定义的view
            supportActionBar?.setDisplayShowCustomEnabled(true)
            supportActionBar?.setCustomView(R.layout.view_cus_actionbar)
        }
    }
}
