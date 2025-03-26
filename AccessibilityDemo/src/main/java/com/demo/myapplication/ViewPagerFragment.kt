package com.demo.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.demo.myapplication.databinding.FragmentViewpagerBinding

/**
 * TIme:2025-02-05
 * Author:xm
 * Description:
 */
class ViewPagerFragment(private val text: String) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentViewpagerBinding.inflate(layoutInflater)
        binding.textView.text = text
        return binding.root
    }
}