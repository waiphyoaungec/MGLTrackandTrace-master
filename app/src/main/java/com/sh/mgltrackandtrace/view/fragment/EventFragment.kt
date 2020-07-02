package com.sh.mgltrackandtrace.view.fragment

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.sh.mgltrackandtrace.R

import com.sh.mgltrackandtrace.adapter.EventAdapter
import com.sh.mgltrackandtrace.model.DataItem
import com.sh.mgltrackandtrace.model.NetWorkState
import com.sh.mgltrackandtrace.viewmodel.DataViewModel
import kotlinx.android.synthetic.main.fragment_event.*

class EventFragment : Fragment() {
    lateinit var magicViewModel: DataViewModel
    lateinit var adapter: EventAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler.layoutManager =
            LinearLayoutManager(context)
        //recycler.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        magicViewModel = ViewModelProviders.of(this).get(DataViewModel::class.java)
        adapter = EventAdapter(this.activity!!)
        subscribedata()
        swprefresh.setOnRefreshListener {
            subscribedata()
        }
        recycler.adapter = adapter
    }

    fun subscribedata(){
        magicViewModel.getPromotion()
        magicViewModel.itemPagedList?.observe(this,
            Observer<PagedList<DataItem>> { t ->
                magicViewModel.networkState.observe(this, Observer {
                    if(it!!.equals(NetWorkState.LOADING)){
                        swprefresh.isRefreshing=true
                        progressBar.visibility=View.VISIBLE
                    }
                    if(it!!.equals(NetWorkState.DONE)){
                        progressBar.visibility=View.GONE
                        swprefresh.isRefreshing=false
                        adapter.submitList(t)
                    }
                    if(it!!.equals(NetWorkState.ERROR)){
                        progressBar.visibility=View.GONE
                        swprefresh.isRefreshing=false
                        Toast.makeText(context,"error",Toast.LENGTH_SHORT).show()
                    }
                })

            })

    }
}