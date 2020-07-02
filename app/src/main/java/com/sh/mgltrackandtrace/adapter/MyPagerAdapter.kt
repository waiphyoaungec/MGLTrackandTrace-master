package com.sh.mgltrackandtrace.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.sh.mgltrackandtrace.R
import com.sh.mgltrackandtrace.view.fragment.EventFragment
import com.sh.mgltrackandtrace.view.fragment.WaybillFragment

class MyPagerAdapter(fm : FragmentManager, var context: Context) : FragmentPagerAdapter(fm){
    override fun getItem(position: Int): Fragment {
        return when (position) {
            1 -> {
                WaybillFragment()
            }
            else -> {
                return EventFragment()
            }
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            1 -> context.resources.getString(R.string.waybilltrack)
            else -> {
                return context.resources.getString(R.string.event)
            }
        }
    }

}