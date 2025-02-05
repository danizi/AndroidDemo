package com.demo.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

/**
 * TIme:2025-02-05
 * Author:xm
 * Description:
 */
class MainActivityViewModel : ViewModel(){

    private lateinit var viewPagerFlow:MutableSharedFlow<List<String>>
    var viewPagerLiveData = viewPagerFlow.asLiveData()

    fun getData() {
        viewModelScope.launch {
            viewPagerFlow.emit(arrayListOf())
        }
    }
}