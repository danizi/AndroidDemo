package com.xm.commoncomponent

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast

class MainActivity : AppCompatActivity(), ISetup {

    companion object {
        val TextView = "TextView"
        val EditText = "EditText"
        val Button = "Button"
        val RadioButton = "RadioButton"
        val ToggleButton = "ToggleButton"
        val ImageView = "ImageView"
        val ProgressBar = "ProgressBar"
        val SeekBar = "SeekBar"
        val ScrollView = "ScrollView"

        val ListView = "ListView"
        val GridView = "GridView"
        val Spinner = "Spinner列表选项框"
        val AutoCompleteTextView = "AutoCompleteTextView"
        val ViewFlipper = "ViewFlipper翻转视图"
        val ViewPager = "ViewPager"

        val CP_Toast = "Toast"
        val Notification = "Notification状态栏通知"
        val AlertDialog = "AlertDialog对话框"
        val PopupWindow = "PopupWindow悬浮框"

        val Menu = "Menu"
        val DrawerLayout = " DrawerLayout侧滑菜单"
    }

    var listView: ListView? = null

    val presidents = arrayOf(
            "------------基本控件------------",
            TextView,
            EditText,
            Button,
            RadioButton,
            ToggleButton,
            ImageView,
            ProgressBar,
            SeekBar,
            ScrollView,
            "------------多条目控件------------",
            ListView,
            GridView,
            Spinner,
            AutoCompleteTextView,
            ViewFlipper,
            ViewPager,
            "------------提示控件------------",
            CP_Toast,
            Notification,
            AlertDialog,
            PopupWindow,
            "------------菜单控件------------",
            Menu,
            DrawerLayout
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViews()
        initEvent()
        initDisplay()
        initData()
    }

    override fun findViews() {
        listView = findViewById(R.id.listView)
    }

    val intents: HashMap<String, Intent> = HashMap()

    override fun initEvent() {
        listView?.setOnItemClickListener { _, _, position, _ ->

            startActivity(intents.get(presidents[position]))

//            when (presidents[position]) {
//                TextView -> {
//                    startActivity(Intent(this@MainActivity, TextViewAct::class.java))
//                }
//                EditText -> {
//                    startActivity(Intent(this@MainActivity, TextViewAct::class.java))
//                }
//            }
            Toast.makeText(this, presidents[position], Toast.LENGTH_LONG).show()
        }
    }

    override fun initDisplay() {}

    override fun initData() {
        listView?.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, presidents)

        intents[TextView] = Intent(this@MainActivity, TextViewAct::class.java)
        intents[EditText] = Intent(this@MainActivity, EditTextAct::class.java)
        intents[Notification] = Intent(this@MainActivity, NotificationAct::class.java)
    }
}

