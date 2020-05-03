package com.xm.material.design

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity

class BottomNavigationViewActivity : AppCompatActivity() {
    private var nav: BottomNavigationView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_navigation_view)

        nav = findViewById(R.id.nv_navigation)
        nav?.setOnNavigationItemSelectedListener {
            true
        }
    }
}
