package com.xm.material.design

import android.graphics.Canvas
import android.graphics.Rect
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class SwipeRefreshLayoutActivity : AppCompatActivity() {

    private var rv: RecyclerView? = null
    private var srl: SwipeRefreshLayout? = null
    private var datas = ArrayList<String>()

    init {
        for (i in 0..20) {
            datas.add("item$i")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_swipe_refresh_layout)
        rv = findViewById(R.id.rv)
        srl = findViewById(R.id.srl)
        rv?.adapter = MyAdapter(datas)
        rv?.layoutManager = LinearLayoutManager(this)
        rv?.layoutManager = GridLayoutManager(this,2)
        rv?.addItemDecoration(MyItemDecoration())
    }
}

class MyItemDecoration : RecyclerView.ItemDecoration() {

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        //outRect.set(0, 0, 0, 1)   //设定底部边距为1px
        outRect.set(0, 0, 0, 0)   //设定底部边距为1px
    }
}

class MyAdapter(val data: List<Any>) : RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.item_test, p0, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(p0: MyViewHolder, p1: Int) {
        p0.bindView(data[p1] as String)
    }
}

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var tv: TextView? = null
    fun bindView(des: String) {
        tv = itemView.findViewById(R.id.tv)
        tv?.text = des
    }
}
