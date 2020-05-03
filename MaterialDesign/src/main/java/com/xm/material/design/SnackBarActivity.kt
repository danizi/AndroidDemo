package com.xm.material.design

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.support.design.widget.Snackbar
import android.support.v4.view.accessibility.AccessibilityEventCompat.setAction
import android.view.LayoutInflater
import android.view.View


class SnackBarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = LayoutInflater.from(this).inflate(R.layout.activity_snack_bar, null, false)
        setContentView(view)
        Snackbar.make(view, "谢谢浏览我的博客", Snackbar.LENGTH_SHORT).setAction("action") { Toast.makeText(this@SnackBarActivity, "snack bar ", Toast.LENGTH_SHORT).show() }.show()
    }
}
