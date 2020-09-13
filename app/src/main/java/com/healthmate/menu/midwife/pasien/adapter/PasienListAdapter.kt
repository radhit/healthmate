package com.artcak.starter.modules.reusable.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.healthmate.R
import com.healthmate.menu.reusable.data.User
import kotlinx.android.synthetic.main.item_list_pasien.view.*

class PasienListAdapter(private val mContext: Context) : RecyclerView.Adapter<PasienListAdapter.ViewHolder>(){
    var lists: ArrayList<User> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_pasien,parent,false))

    override fun getItemCount(): Int = lists.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val value = lists[position]
        holder.view.tv_status.text = value.diagnostics_color
        holder.view.tv_nama.text = value.name
        holder.view.tv_alamat.text = "${value.city!!.name} - ${value.district!!.name}"
        holder.view.tv_keterangan.text = "-"
        if (value.diagnostics_color.equals("")){
            holder.view.iv_foto.borderColor = getColor(mContext, R.color.colorPrimary)
            holder.view.tv_status.text = "-"
        } else if (value.diagnostics_color.equals("red")){
            holder.view.iv_foto.borderColor = getColor(mContext, R.color.md_red_500)
        } else if (value.diagnostics_color.equals("yellow")){
            holder.view.iv_foto.borderColor = getColor(mContext, R.color.md_yellow_500)
        } else if (value.diagnostics_color.equals("green")){
            holder.view.iv_foto.borderColor = getColor(mContext, R.color.md_green_500)
        }

    }

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view){
        var data: User? = null
        set(value){field = value}
    }
}