package com.healthmate.menu.midwife.home.view

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.artcak.starter.modules.reusable.adapter.MenuAdapter
import com.healthmate.R
import com.healthmate.api.Payload
import com.healthmate.api.PayloadEntry
import com.healthmate.api.Result
import com.healthmate.common.adapter.GridSpacingItemDecoration
import com.healthmate.common.adapter.RecyclerViewClickListener
import com.healthmate.common.adapter.RecyclerViewTouchListener
import com.healthmate.common.base.BaseFragment
import com.healthmate.common.functions.Fun
import androidx.lifecycle.Observer
import com.healthmate.common.constant.Urls
import com.healthmate.di.injector
import com.healthmate.menu.mom.home.data.BerandaViewModel
import com.healthmate.menu.mom.home.data.CheckUpModel
import com.healthmate.menu.reusable.data.Menu
import com.healthmate.menu.reusable.data.User
import kotlinx.android.synthetic.main.activity_signin.*
import kotlinx.android.synthetic.main.fragment_beranda.*
import kotlinx.android.synthetic.main.fragment_beranda.iv_banner
import kotlinx.android.synthetic.main.fragment_beranda.rv_menu
import kotlinx.android.synthetic.main.fragment_beranda_midwive.*
import java.util.*

class BerandaMidwiveFragment : BaseFragment() {
    lateinit var materialDialog: MaterialDialog

    override fun getViewId(): Int = R.layout.fragment_beranda_midwive
    lateinit var adapter: MenuAdapter

    companion object {
        fun newInstance(): BerandaMidwiveFragment = BerandaMidwiveFragment()
    }

    override fun onFragmentCreated(savedInstanceState: Bundle?) {
        setRecycleView()
        iv_banner.visibility = View.GONE
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
                if (menu.nama.equals("Pasien")){

                } else if (menu.nama.equals("Modul")){

                } else if (menu.nama.equals("Rujukan")){

                } else if (menu.nama.equals("Konsultasi")){

                } else if (menu.nama.equals("Komunitas")){

                } else if (menu.nama.equals("Poin")){

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
        arrays.add(Menu("Pasien",R.drawable.pasien))
        arrays.add(Menu("Rujukan",R.drawable.rujukan))
        arrays.add(Menu("Konsultasi",R.drawable.konsultasi))
        arrays.add(Menu("Komunitas",R.drawable.komunitas))
        arrays.add(Menu("Modul",R.drawable.modul))
        arrays.add(Menu("Poin",R.drawable.poin))
        return arrays
    }
}