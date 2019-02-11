package com.xm.commoncomponent

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.xm.commoncomponent.ui.*

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

    private val intents: HashMap<String, Intent> = HashMap()
    private var listView: ListView? = null
    private val presidents = arrayOf(
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

    override fun initEvent() {
        listView?.setOnItemClickListener { _, _, position, _ ->
            startActivity(intents[presidents[position]])
            Toast.makeText(this, presidents[position], Toast.LENGTH_LONG).show()
        }
    }

    override fun initDisplay() {}

    override fun initData() {
        //设置适配器
        listView?.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, presidents)
        //添加跳转intent
        putIntents()
    }

    private fun putIntents() {
        intents[TextView] = Intent(this@MainActivity, TextViewAct::class.java)
        intents[EditText] = Intent(this@MainActivity, EditTextAct::class.java)
        intents[Button] = Intent(this@MainActivity, ButtonAct::class.java)
        intents[RadioButton] = Intent(this@MainActivity, RadioButtonAct::class.java)
        intents[ToggleButton] = Intent(this@MainActivity, ToggleButtonAct::class.java)
        intents[ImageView] = Intent(this@MainActivity, ImageViewAct::class.java)
        intents[ProgressBar] = Intent(this@MainActivity, ProgressBarAct::class.java)
        intents[SeekBar] = Intent(this@MainActivity, SeekBarAct::class.java)
        intents[ScrollView] = Intent(this@MainActivity, ScrollViewAct::class.java)

        intents[ListView] = Intent(this@MainActivity, ListViewAct::class.java)
        intents[GridView] = Intent(this@MainActivity, GridViewAct::class.java)
        intents[Spinner] = Intent(this@MainActivity, SpinnerAct::class.java)
        intents[AutoCompleteTextView] = Intent(this@MainActivity, AutoCompleteTextViewAct::class.java)
        intents[ViewFlipper] = Intent(this@MainActivity, ViewFlipperAct::class.java)
        intents[ViewPager] = Intent(this@MainActivity, ViewPagerAct::class.java)

        intents[CP_Toast] = Intent(this@MainActivity, ToastAct::class.java)
        intents[Notification] = Intent(this@MainActivity, NotificationAct::class.java)
        intents[AlertDialog] = Intent(this@MainActivity, AlertDialogAct::class.java)
        intents[PopupWindow] = Intent(this@MainActivity, PopupWindowAct::class.java)

        intents[Menu] = Intent(this@MainActivity, MenuAct::class.java)
        intents[DrawerLayout] = Intent(this@MainActivity, DrawerLayoutAct::class.java)
    }
}

