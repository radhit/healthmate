package com.artcak.starter.modules.reusable.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.healthmate.R
import com.healthmate.menu.mom.rapor.data.AncModel
import com.healthmate.menu.reusable.data.User
import kotlinx.android.synthetic.main.activity_ubah_profile_midwive.*
import kotlinx.android.synthetic.main.item_anc_midwife.view.*
import kotlinx.android.synthetic.main.item_list_pasien.view.*

class AncListAdapter(private val mContext: Context) : RecyclerView.Adapter<AncListAdapter.ViewHolder>(){
    var lists: ArrayList<AncModel> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_anc_midwife,parent,false))

    override fun getItemCount(): Int = lists.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val value = lists[position]
        holder.view.tv_tanggal.text = "${value.created_time.split("T")[0]} - ${value.created_time.split("T")[1].split("+")[0]}"

    }

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view){
        var data: AncModel? = null
        set(value){field = value}
    }
}