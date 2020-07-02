package com.sh.mgltrackandtrace.view.activity

import android.app.ProgressDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.text.Html
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

import com.sh.mgltrackandtrace.model.DataItem
import kotlinx.android.synthetic.main.activity_detail.*
import me.myatminsoe.mdetect.MDetect
import me.myatminsoe.mdetect.Rabbit
import android.webkit.WebView
import android.graphics.Bitmap
import android.webkit.WebViewClient
import android.view.View

import com.sh.mgltrackandtrace.R


class DetailActivity : AppCompatActivity() {
    lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading...") // Setting Message
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER) // Progress Dialog Style Spinner
        detailwebview.webViewClient = CustomWebViewClient()

        if(intent.hasExtra("data")){
            val item : DataItem = intent.extras!!.getSerializable("data") as DataItem
            //Log.i("Detail " , ""+item.date)
            postdate.text = "Posted on: "+item.date.split("T")[0]
            if(MDetect.isUnicode()){
                detail_description.text = Html.fromHtml(Rabbit.zg2uni(item.content?.rendered))
                detail_title.text = Rabbit.zg2uni(item.title?.rendered)
                toolbar.setTitle(Rabbit.zg2uni(item.title?.rendered))
                detailwebview.loadDataWithBaseURL(null, item.content?.rendered, "text/html","",null)
            }else{
                detail_description.text = Html.fromHtml(item.content?.rendered)
                detail_title.text = item.title?.rendered
                toolbar.setTitle(item.title?.rendered)
                detailwebview.loadDataWithBaseURL(null, Rabbit.zg2uni(item.content?.rendered), "text/html","","about:blank")
            }

            toolbar.setNavigationIcon(R.drawable.outline_arrow_back_white_36dp)
            toolbar.setNavigationOnClickListener {
                finish()
            }

            if(item.better_featured_image == null){
                //Log.i("Event ", item?.better_featured_image );
               // holder.vImage.visibility = View.GONE
            }else{
                //holder.vImage.visibility = View.VISIBLE
                val img = item.better_featured_image?.media_details?.sizes?.medium?.source_url.toString()
                val requestOptions = RequestOptions()
                requestOptions.placeholder(R.mipmap.ic_launcher)
                Glide.with(this)
                    .load(img)
                    .apply(requestOptions)
                    .thumbnail(0.1f)
                    .into(detail_image)
            }
        }
    }

    // This allows for a splash screen
    // (and hide elements once the page loads)
    private inner class CustomWebViewClient : WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            progressDialog.show()
            view?.visibility= View.INVISIBLE
            super.onPageStarted(view, url, favicon)
        }

        override fun onPageFinished(view: WebView, url: String) {
            progressDialog.dismiss()
            view.visibility = View.VISIBLE
            super.onPageFinished(view, url)
        }
    }

}