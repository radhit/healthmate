package com.artcak.starter.modules.reusable.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.healthmate.R
import com.healthmate.menu.reusable.data.User
import kotlinx.android.synthetic.main.item_pasien_rujukan.view.*

class PasienRujukanAdapter(private val mContext: Context, private val keterangan: String) : RecyclerView.Adapter<PasienRujukanAdapter.ViewHolder>(){
    var lists: ArrayList<User> = arrayListOf()
    var gson: Gson = Gson()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_pasien_rujukan,parent,false))

    override fun getItemCount(): Int = lists.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val value = lists[position]
        holder.view.tv_status.text = value.diagnostic_color
        holder.view.tv_nama.text = value.name
//        if (keterangan.equals("penerima")){
//            holder.view.tv_rujukan.text = "Dirujuk oleh ${value.rujukan!!.asal_perujuk!!.name}"
//            holder.view.tv_tanggal.text = "Dirujuk tanggal ${value.rujukan!!.date.split("T")[0]}"
//        } else{
//            holder.view.tv_rujukan.text = "Merujuk ke ${value.rujukan!!.asal_perujuk!!.name}"
//            holder.view.tv_tanggal.text = "Merujuk tanggal ${value.rujukan!!.date.split("T")[0]}"
//        }
        Glide.with(mContext).load(value.profile_picture).into(holder.view.iv_foto)
    }

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view){
        var data: User? = null
        set(value){field = value}
    }
}