package com.github.lcdsmao.storepaging.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.github.lcdsmao.storepaging.data.AppStore
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _dummyData = MutableLiveData<PagedList<String>>()
    val dummyData get() = _dummyData

    fun fetchDummyData(isFresh: Boolean, key: String) {
        viewModelScope.launch {
            val data = if (isFresh) {
                AppStore.freshDummyDataList(key)
            } else {
                AppStore.getDummyDataList(key)
            }
            _dummyData.postValue(data)
        }
    }
}
