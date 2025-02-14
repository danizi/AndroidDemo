package com.demo.myapplication

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * TIme:2025-02-05
 * Author:xm
 * Description:
 */
class ViewPager2Adapter(activity: FragmentActivity, private val pages: List<String>): FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = pages.size

    override fun createFragment(position: Int): Fragment {
        return ViewPagerFragment(pages[position])
    }
}