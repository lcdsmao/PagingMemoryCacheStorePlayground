package com.github.lcdsmao.storepaging.data

import androidx.paging.Config
import androidx.paging.DataSource
import androidx.paging.PagedList
import com.dropbox.android.external.store4.StoreBuilder
import com.dropbox.android.external.store4.fresh
import com.dropbox.android.external.store4.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import java.lang.ref.WeakReference

object AppStore {

    private val dummyDataStore = StoreBuilder.fromDataSource { key: String ->
        DummyDataSource(key)
    }.build()

    suspend fun getDummyDataList(key: String) = dummyDataStore.get(key)

    suspend fun freshDummyDataList(key: String) = dummyDataStore.fresh(key)
}

fun <StoreKey : Any, PagingKey : Any, Value : Any> StoreBuilder.Companion.fromDataSource(
    dataSourceFactory: (storeKey: StoreKey) -> DataSource<PagingKey, Value>
): StoreBuilder<StoreKey, PagedList<Value>> {

    val pagedListMap = mutableMapOf<StoreKey, WeakReference<PagedList<Value>>>()

    return fromNonFlow { key: StoreKey ->
        val dataSource = dataSourceFactory(key)
        val pagedList = dataSource.toPagedList()
        pagedListMap[key]?.get()?.dataSource?.invalidate()
        pagedListMap[key] = WeakReference(pagedList)
        pagedList
    }
}

fun <K, V> DataSource<K, V>.toPagedList(): PagedList<V> {
    val config = Config(10, 10, false)
    return PagedList(
        this,
        config,
        notifyExecutor = Dispatchers.Main.immediate.asExecutor(),
        fetchExecutor = Dispatchers.IO.asExecutor()
    )
}
