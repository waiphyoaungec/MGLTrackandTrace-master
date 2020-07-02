package com.sh.mgltrackandtrace.datasource

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.PageKeyedDataSource
import android.util.Log
import com.sh.mgltrackandtrace.api.API_Client
import com.sh.mgltrackandtrace.api.API_Connect
import com.sh.mgltrackandtrace.model.DataItem
import com.sh.mgltrackandtrace.model.NetWorkState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DataSource(var per_page: String,var page: Int) : PageKeyedDataSource<Int, DataItem>(){
 var networkStatus: MutableLiveData<NetWorkState> = MutableLiveData()
    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, DataItem>) {
        val client = API_Client.getRetrofit().create(API_Connect::class.java)
        networkStatus.postValue(NetWorkState.LOADING)
        //Log.i("DataSource", "loadInitial page"+ page)
        client.getPromotions(per_page, page).enqueue(object : Callback<MutableList<DataItem>>{
            override fun onFailure(call: Call<MutableList<DataItem>>, t: Throwable) {
                Log.i("DataSource", "loadInitial "+ t.message)
            }

            override fun onResponse(call: Call<MutableList<DataItem>>, response: Response<MutableList<DataItem>>) {
                //Log.i("DataSource", "loadInitial "+ response.body())
                if (response.body() != null) {
                    callback.onResult(response.body()!!, 1, 1)
                    networkStatus.postValue(NetWorkState.DONE)
                   Log.d("datasource", networkStatus.toString())
                }
            }

        })
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, DataItem>) {
        val client = API_Client.getRetrofit().create(API_Connect::class.java)
        networkStatus.postValue(NetWorkState.LOADING)
        //Log.i("DataSource", "loadAfter page"+ page)
        client.getPromotions(per_page, page).enqueue(object : Callback<MutableList<DataItem>>{
            override fun onFailure(call: Call<MutableList<DataItem>>, t: Throwable) {
                Log.i("DataSource", "loadAfter "+ t.message)
            }

            override fun onResponse(call: Call<MutableList<DataItem>>, response: Response<MutableList<DataItem>>) {
                val adjacentKey = if (params.key > 1) params.key - 1 else null
                //Log.i("DataSource", "loadAfter "+ call.e)
                //Log.i("DataSource", "loadAfter adjacentKey"+ adjacentKey)
                //Log.i("DataSource", "loadAfter "+ response.body())
                if (response.body() != null) {
                    callback.onResult(response.body()!!, adjacentKey)
                    networkStatus.postValue(NetWorkState.DONE)
                }
            }

        })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, DataItem>) {
        val client = API_Client.getRetrofit().create(API_Connect::class.java)
        //Log.i("DataSource", "loadBefore page"+ page)
        client.getPromotions(per_page, page).enqueue(object : Callback<MutableList<DataItem>>{
            override fun onFailure(call: Call<MutableList<DataItem>>, t: Throwable) {
                Log.i("DataSource", "loadBefore "+ t.message)
            }

            override fun onResponse(call: Call<MutableList<DataItem>>, response: Response<MutableList<DataItem>>) {
                //if (response.body() != null && response.body()!!.getNextPageUrl() != null) {
                Log.i("DataSource", "loadBefore "+ response.body())
                //callback.onResult(response.body()!!, params.key + 1)
                //}
            }

        })
    }

}