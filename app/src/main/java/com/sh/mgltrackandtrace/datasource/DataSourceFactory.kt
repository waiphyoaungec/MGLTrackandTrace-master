package com.sh.mgltrackandtrace.datasource

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.sh.mgltrackandtrace.model.DataItem

class DataSourceFactory(var per_page: String, var page: Int) : DataSource.Factory<Int, DataItem>(){
    //private val promotionDataSource = MutableLiveData<PageKeyedDataSource<Int, DataItem>>()
    private val promotionDataSource = MutableLiveData<com.sh.mgltrackandtrace.datasource.DataSource>()
    override fun create(): DataSource<Int, DataItem> {
        val dataSource = DataSource(per_page, page)
        promotionDataSource.postValue(dataSource)
        return dataSource
    }

    fun getPromotion() : MutableLiveData<com.sh.mgltrackandtrace.datasource.DataSource> {
        //Log.i(TAG, "getMagicLiveDataSource " +performanceLiveDataSource.value)
        return promotionDataSource
    }

}