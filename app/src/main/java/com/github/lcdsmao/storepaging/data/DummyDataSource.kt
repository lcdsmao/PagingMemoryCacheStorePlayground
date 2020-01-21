package com.github.lcdsmao.storepaging.data

import androidx.paging.PageKeyedDataSource
import java.text.DateFormat
import java.util.Date
import java.util.Locale

class DummyDataSource(private val storeKey: String) : PageKeyedDataSource<Int, String>() {

    companion object {
        private val FORMAT = DateFormat.getTimeInstance(DateFormat.DEFAULT, Locale.US)
    }

    private val date = FORMAT.format(Date())

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, String>
    ) {
        Thread.sleep(100)
        val data = List(params.requestedLoadSize) { "StoreKey: $storeKey, Data: $it, Date: $date" }
        callback.onResult(data, null, data.size)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, String>) {
        Thread.sleep(100)
        val data = List(params.requestedLoadSize) { "StoreKey: $storeKey, Data: ${params.key + it}, Date: $date" }
        callback.onResult(data, data.size + params.key)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, String>) {
        // no op
    }
}
