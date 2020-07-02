package com.sh.mgltrackandtrace.viewmodel

import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.sh.mgltrackandtrace.datasource.DataSource
import com.sh.mgltrackandtrace.datasource.DataSourceFactory
import com.sh.mgltrackandtrace.model.DataItem
import com.sh.mgltrackandtrace.model.NetWorkState

class DataViewModel: ViewModel()  {
    var itemPagedList: LiveData<PagedList<DataItem>>? = null
    var liveDataSource: LiveData<DataSource>? = null
    var networkState: LiveData<NetWorkState> = MutableLiveData()

    init {

    }

    fun getPromotion(){
        //Log.i("MagicViewModel", "init")
        val magicDataSourceFactory = DataSourceFactory("20",1)
        liveDataSource = magicDataSourceFactory.getPromotion()

        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(1).build()

        itemPagedList = LivePagedListBuilder(magicDataSourceFactory, pagedListConfig)
            .build()

        networkState = Transformations.switchMap(magicDataSourceFactory.getPromotion(), DataSource::networkStatus)
    }

    fun listIsEmpty(): Boolean{
        return itemPagedList!!.value?.isEmpty()?:true
    }


}