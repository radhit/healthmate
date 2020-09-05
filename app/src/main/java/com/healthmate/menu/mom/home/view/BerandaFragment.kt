package com.healthmate.menu.mom.home.view

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.artcak.starter.modules.reusable.adapter.MenuAdapter
import com.bumptech.glide.Glide
import com.healthmate.BuildConfig
import com.healthmate.R
import com.healthmate.common.adapter.GridSpacingItemDecoration
import com.healthmate.common.adapter.RecyclerViewClickListener
import com.healthmate.common.adapter.RecyclerViewTouchListener
import com.healthmate.common.base.BaseFragment
import com.healthmate.menu.reusable.data.Menu
import kotlinx.android.synthetic.main.fragment_beranda.*
import kotlinx.android.synthetic.main.fragment_profile.*
import java.util.*

class BerandaFragment : BaseFragment() {
    lateinit var materialDialog: MaterialDialog

    override fun getViewId(): Int = R.layout.fragment_beranda
    lateinit var adapter: MenuAdapter
    var menus: ArrayList<Menu> = ArrayList()

    companion object {
        fun newInstance(): BerandaFragment = BerandaFragment()
    }

    override fun onFragmentCreated(savedInstanceState: Bundle?) {
        setRecycleView()
        if (userPref.getUser()!=null){
            if (userPref.getUser().location!=null && userPref.getUser().location!!.subdistrict.equals("")){
                ll_profile_done.visibility = View.GONE
                ll_profile_not.visibility = View.VISIBLE
                tv_nama_not.text = "Hei, ${userPref.getUser().name}"
            } else{
                ll_profile_done.visibility = View.VISIBLE
                ll_profile_not.visibility = View.GONE
                tv_nama_done.text = "Pasien\n${userPref.getUser().name}"

            }
        }
    }

    private fun setRecycleView() {
        adapter = MenuAdapter(context!!)
        rv_menu.layoutManager = LinearLayoutManager(context!!)
        rv_menu.adapter = adapter
        val mLayoutManager = GridLayoutManager(context, 3)
        rv_menu.setLayoutManager(mLayoutManager)
        rv_menu.addItemDecoration(GridSpacingItemDecoration(3, activity!!,0 , true))
        rv_menu.addOnItemTouchListener(RecyclerViewTouchListener(context!!, rv_menu, object :
                RecyclerViewClickListener {
            override fun onClick(view: View, position: Int) {
                val menu = adapter.lists[position]
                if (menu.nama.equals("Rapor")){

                } else if (menu.nama.equals("Modul")){

                } else if (menu.nama.equals("Home Care")){

                }
            }

            override fun onLongClick(view: View, position: Int) {

            }

        }))
        adapter.lists.clear()

        adapter.lists.addAll(generateMenus())
        adapter.notifyDataSetChanged()
    }

    fun generateMenus(): ArrayList<Menu>{
        val arrays: ArrayList<Menu> = arrayListOf()
        arrays.add(Menu("Rapor",R.drawable.rapor))
        arrays.add(Menu("Modul",R.drawable.modul))
        arrays.add(Menu("Home Care",R.drawable.homecare))
        return arrays
    }
}