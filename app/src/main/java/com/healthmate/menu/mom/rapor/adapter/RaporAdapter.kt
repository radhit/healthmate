package com.artcak.starter.modules.reusable.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.healthmate.R
import com.healthmate.menu.mom.rapor.data.AncModel
import kotlinx.android.synthetic.main.item_rapor.view.*

class RaporAdapter(context: Context) : RecyclerView.Adapter<RaporAdapter.ViewHolder>(){
    var lists: ArrayList<AncModel> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_rapor,parent,false))

    override fun getItemCount(): Int = lists.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val value = lists[position]
        holder.view.tv_minggu_ke.text = "Minggu ke ${value.week_of_pregnancy}"
        holder.view.tv_bb.text = "${value.weight} Kg"
        holder.view.tv_tanggal.text = value.date.split("T")[0]
    }

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view){
        var data: AncModel? = null
        set(value){field = value}
    }
}