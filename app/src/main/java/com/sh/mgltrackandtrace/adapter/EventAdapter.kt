package com.sh.mgltrackandtrace.adapter

import androidx.paging.PagedListAdapter
import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.request.RequestOptions
import com.sh.mgltrackandtrace.R


import com.sh.mgltrackandtrace.model.DataItem
import com.sh.mgltrackandtrace.view.activity.DetailActivity
import de.hdodenhof.circleimageview.CircleImageView
import me.myatminsoe.mdetect.MDetect
import me.myatminsoe.mdetect.Rabbit

class EventAdapter(val context: Context) : PagedListAdapter<DataItem, EventAdapter.ViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recycleritem, p0, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, p1: Int) {
        val item = getItem(p1)
        holder.vDate.text = item?.date?.split("T")!![0]

        //holder.vDes.text = item?.excerpt?.rendered

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.vDes.text = Html.fromHtml(item?.excerpt?.rendered)
        } else {
            holder.vDes.text = Html.fromHtml(item?.excerpt?.rendered)
        }*/

        if(MDetect.isUnicode()){
            holder.vDes.text = Html.fromHtml(Rabbit.zg2uni(item.excerpt?.rendered))
            holder.vTitle.text = Rabbit.zg2uni(item.title?.rendered)
            holder.txtContinue.text = "ဆက်ဖက်ရန်"
        }else{
            holder.vDes.text = Html.fromHtml(item.excerpt?.rendered)
            holder.vTitle.text = item.title?.rendered
            holder.txtContinue.text = "ဆက္ဖက္ရန္"
        }

        val img: String?
        if(item.better_featured_image == null){
            //Log.i("Event ", item?.better_featured_image );
           // holder.vImage.visibility = View.GONE
            val requestOptions = RequestOptions()
            Glide.with(context)
                .load(R.mipmap.ic_launcher)
                .apply(requestOptions.centerCrop())
                .into(holder.vImage)
            Glide.with(context)
                .load(R.mipmap.ic_launcher)
                .apply(requestOptions.centerCrop())
                .into(holder.profileimg)
        }else{
            holder.vImage.visibility = View.VISIBLE
            img = item.better_featured_image?.media_details?.sizes?.medium?.source_url.toString()
            val requestOptions = RequestOptions()
            requestOptions.placeholder(R.mipmap.ic_launcher)
            Log.d("eVEN", img)
                Glide.with(context)
                    .load(img)
                    .apply(requestOptions.centerCrop())
                    .into(holder.vImage)
                Glide.with(context)
                    .load(img)
                    .apply(requestOptions.centerCrop())
                    .into(holder.profileimg)
        }
        holder.vDes.setOnClickListener {

        }
        holder.txtContinue.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("data", item)
            context.startActivity(intent)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItem>() {
            override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem.id== newItem.id
            }
        }
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val vImage: ImageView = v.findViewById(R.id.imgPhoto)
        val vTitle: TextView = v.findViewById(R.id.txtTitle)
        val vDes : TextView = v.findViewById(R.id.txtDes)
        val vDate : TextView = v.findViewById(R.id.txtDate)
        val profileimg: CircleImageView = v.findViewById(R.id.profile_image)
        val txtContinue: TextView = v.findViewById(R.id.txtcontinue)
        //var context: Context? = null
    }
}