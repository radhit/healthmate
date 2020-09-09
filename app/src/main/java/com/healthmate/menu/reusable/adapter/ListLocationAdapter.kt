package com.artcak.starter.modules.reusable.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.healthmate.R
import com.healthmate.menu.reusable.data.Location
import com.healthmate.menu.reusable.data.MasterListModel
import com.healthmate.menu.reusable.data.Menu
import kotlinx.android.synthetic.main.item_master_list.view.*

class ListLocationAdapter() : RecyclerView.Adapter<ListLocationAdapter.ViewHolder>(){
    var lists: ArrayList<Location> = arrayListOf()
    fun updateLists(newData: ArrayList<Location>){
        lists.clear()
        lists.addAll(newData)
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_master_list,parent,false))

    override fun getItemCount(): Int = lists.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val value = lists[position]
        holder.view.tv_nama.text = value.name
    }

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view){
        var data: MasterListModel? = null
        set(value){field = value}
    }
}