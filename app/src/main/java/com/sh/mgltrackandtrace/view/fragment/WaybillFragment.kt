package com.sh.mgltrackandtrace.view.fragment

import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.sh.mgltrackandtrace.R

import com.sh.mgltrackandtrace.viewmodel.RegisterViewModel
import kotlinx.android.synthetic.main.fragment_waybill.*
import me.myatminsoe.mdetect.MDetect
import me.myatminsoe.mdetect.Rabbit
import org.w3c.dom.Text


class WaybillFragment : Fragment() {
    //var TAG = "WaybillFragment"
    private var finalParent : ViewGroup? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_waybill, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        finalParent = activity?.findViewById(R.id.contenedor)
        progress.visibility = View.GONE
        edtWaybillcode.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
                if (event.getAction() === KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    //Toast.makeText(activity, "click", Toast.LENGTH_LONG).show()
                    activity?.let { hideSoftKeyboard(it) }
                    search()
                    return true
                }
                return false
            }
        })

//        btnSearch.setOnClickListener {
//            activity?.let { hideSoftKeyboard(it) }
//            search()
//        }
        imgsearch.setOnClickListener {
            activity?.let { hideSoftKeyboard(it) }
            search()
        }
    }

    fun search(){
        progress.visibility = View.VISIBLE
        if (isNetworkAvailable()){
            val viewModel = ViewModelProviders.of(this).get(RegisterViewModel::class.java)
            viewModel.getWaybillTracking(edtWaybillcode.text.toString())
            with(viewModel){
                checkpointData.observe(this@WaybillFragment, Observer {
                    Log.i(TAG, "result"+it?.response+"\t"+it?.waybillData?.size)
                    if(it?.response == "success"){
                        Log.d("test","is successfully")
                        //Log.i(TAG, "size "+ it.waybillData.size)
                        finalParent?.removeAllViews()
                        if(it?.waybillData.size==0){
                            Log.d("test","successful size 0")
                            progress.visibility = View.GONE
                            txtnosearchdata.visibility=View.VISIBLE
                        }else{
                            Log.d("test","Successful size is greater ${it.waybillData.size}")
                            for (i in it.waybillData.size - 1 downTo 0) {
                                //Log.i(TAG, "$i")
                                if(it.waybillData[i].image == ""){

                                    Log.d("test","image is null")
                                    Log.d("test","hello is ${it.waybillData[i].trackingPointMM}")
                                   // createXmlElement(i,it.waybillData[i].trackingPoint, it.waybillData[i].trackingPointMM, it.waybillData[i].image)
                                    //Log.i(TAG, it.waybillData[i].trackingPoint)
                                }else{
                                    Log.d("test","okokok")
                                    createXmlElement(i,it.waybillData[i].trackingPoint, it.waybillData[i].trackingPointMM, it.waybillData[i].image,it.waybillData[i].created_at)
                                }
                            }
                        }
                    }
                })
            }
        }else{
            progress.visibility = View.GONE
            showNetworkError("No Internet Connection")
        }
    }

    fun hideSoftKeyboard(activity: Activity) {
        val inputMethodManager = activity.getSystemService(
            Activity.INPUT_METHOD_SERVICE
        ) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(
            activity.currentFocus!!.windowToken, 0
        )
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = activity?.getSystemService(Context.CONNECTIVITY_SERVICE)
        return if (connectivityManager is ConnectivityManager) {
            val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
            networkInfo?.isConnected ?: false
        } else false
    }

    private fun showNetworkError(des : String){
        Toast.makeText(activity, des, Toast.LENGTH_LONG).show()
    }

    private fun createXmlElement(count : Int, title:String, description:String, image : String,date : String){
        val mainparent = LinearLayout(activity!!)
        mainparent.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)
        mainparent.setPadding(30,30,30,30)
        mainparent.orientation = LinearLayout.VERTICAL

        val parent = LinearLayout(activity)
        parent.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)

        parent.orientation = LinearLayout.HORIZONTAL

        //children of parent linearlayout
        val iv = ImageView(activity)
        val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)

        lp.setMargins(0, 11, 7, 0)
        iv.layoutParams = lp
        iv.layoutParams.height = 110
        iv.layoutParams.width = 116
        Glide.with(this)
            .load(image)
            .thumbnail(0.1f)
            .into(iv)

        val linearCH = LinearLayout(activity)
        linearCH.layoutParams = LinearLayout.LayoutParams(400,
            LinearLayout.LayoutParams.WRAP_CONTENT)
        linearCH.orientation = LinearLayout.VERTICAL
        // TextView1
        val tv1 = TextView(activity)
        val tv3 = TextView(activity)
        val lptv1 = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)
        val lptv3 = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)

        lptv1.setMargins(0, 11, 0, 0)

        tv1.layoutParams = lptv1
        tv3.layoutParams = lptv3
        tv3.text = date
        tv1.text = title // title

        // TextView2
        val tv2 = TextView(activity)
        val lptv2 = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)

        lptv2.setMargins(0, 11, 7, 0)

        tv2.layoutParams = lptv1
        if(MDetect.isUnicode()){
            tv2.text = Rabbit.zg2uni(description) // description
        }else{
            tv2.text = description // description
        }

        linearCH.removeAllViews()
        linearCH.addView(tv1)
        linearCH.addView(tv2)
        linearCH.addView(tv3)

        val view = View(activity)
        val linear = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2)

        linear.setMargins(50, 10, 10, 50)
        view.layoutParams = linear
        view.setBackgroundColor(Color.BLACK)


        parent.removeAllViews()
        parent.addView(linearCH)
        parent.addView(iv)
        //parent.addView(view)

        mainparent.removeAllViews()
        //
        mainparent.addView(parent)
        if(count != 0){
            mainparent.addView(view)
        }
        finalParent?.addView(mainparent)
        /*
        view gone
         */
        txtnosearchdata.visibility=View.GONE
        progress.visibility = View.GONE
    }
}