package com.artcak.starter.modules.reusable.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.healthmate.R
import com.healthmate.menu.reusable.data.Menu
import kotlinx.android.synthetic.main.item_menu.view.*

class MenuAdapter(private val context: Context) : RecyclerView.Adapter<MenuAdapter.ViewHolder>(){
    var lists: ArrayList<Menu> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_menu,parent,false))

    override fun getItemCount(): Int = lists.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val value = lists[position]
        holder.view.tv_title.text = value.nama
        Glide.with(context).load(value.icon_drawable).into(holder.view.iv_icon)
    }

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view){
        var data: Menu? = null
        set(value){field = value}
    }
}