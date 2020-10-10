package com.artcak.starter.modules.reusable.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.healthmate.R
import com.healthmate.menu.midwife.rujukan.data.Rujukan
import com.healthmate.menu.reusable.data.User
import kotlinx.android.synthetic.main.item_list_pasien.view.*
import kotlinx.android.synthetic.main.item_pasien_rujukan.view.*
import kotlinx.android.synthetic.main.item_pasien_rujukan.view.iv_foto
import kotlinx.android.synthetic.main.item_pasien_rujukan.view.tv_nama
import kotlinx.android.synthetic.main.item_pasien_rujukan.view.tv_status

class PasienRujukanAdapter(private val mContext: Context, private val keterangan: String) : RecyclerView.Adapter<PasienRujukanAdapter.ViewHolder>(){
    var lists: ArrayList<Rujukan> = arrayListOf()
    var gson: Gson = Gson()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_pasien_rujukan,parent,false))

    override fun getItemCount(): Int = lists.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val value = lists[position]
        holder.view.tv_status.text = value.mother!!.diagnostic_color
        holder.view.tv_nama.text = value.mother!!.name
        if (keterangan.equals("penerimaan")){
            holder.view.tv_rujukan.text = "Dirujuk oleh ${value.asal_perujuk!!.name}"
            holder.view.tv_tanggal.text = "Dirujuk tanggal ${value.datetime.split("T")[0]}"
        } else{
            holder.view.tv_rujukan.text = "Merujuk ke ${value.asal_perujuk!!.name}"
            holder.view.tv_tanggal.text = "Merujuk tanggal ${value.datetime.split("T")[0]}"
        }
        if (value.mother.diagnostic_color.equals("")){
            holder.view.iv_foto.borderColor = ContextCompat.getColor(mContext, R.color.colorPrimary)
            holder.view.tv_status.text = "-"
        } else if (value.mother.diagnostic_color.equals("red")){
            holder.view.iv_foto.borderColor = ContextCompat.getColor(mContext, R.color.md_red_500)
        } else if (value.mother.diagnostic_color.equals("yellow")){
            holder.view.iv_foto.borderColor = ContextCompat.getColor(mContext, R.color.md_yellow_500)
        } else if (value.mother.diagnostic_color.equals("green")){
            holder.view.iv_foto.borderColor = ContextCompat.getColor(mContext, R.color.md_green_500)
        }
        Glide.with(mContext).load(value.mother!!.profile_picture).into(holder.view.iv_foto)
    }

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view){
        var data: User? = null
        set(value){field = value}
    }
}