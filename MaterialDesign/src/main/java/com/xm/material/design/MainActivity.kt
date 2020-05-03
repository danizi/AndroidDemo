package com.xm.material.design

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ListView
import android.content.Intent
import android.widget.SimpleAdapter


class MainActivity : AppCompatActivity() {

    private var listView: ListView? = null
    private val maps = HashMap<Int, Class<*>>()
    init {
        maps[0] = ToolbarActivity::class.java
        maps[1] = CardViewActivity::class.java
        maps[2] = SwitchCompatActivity::class.java
        maps[3] = SnackBarActivity::class.java
        maps[4] = SwipeRefreshLayoutActivity::class.java
        maps[5] = BottomSheetActivity::class.java
        maps[6] = BottomNavigationViewActivity::class.java
        maps[7] = TextInputLayoutActivity::class.java
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        listView = findViewById(R.id.lv)
        initListView()
    }

    private fun initListView() {
        val data = ArrayList<Map<String, String>>()
        for (set in maps.entries) {
            val map = HashMap<String, String>()
            map["text"] = set.value.simpleName
            data.add(map)
        }
        listView?.adapter = SimpleAdapter(this, data, android.R.layout.simple_list_item_1, arrayOf("text"), intArrayOf(android.R.id.text1))
        listView?.setOnItemClickListener { parent, view, position, id ->
            startAct(maps[position]!!)
        }
    }

    private fun startAct(cls: Class<*>) {
        startActivity(Intent(this@MainActivity, cls))
    }
}
