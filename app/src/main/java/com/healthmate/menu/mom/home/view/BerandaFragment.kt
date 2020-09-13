package com.healthmate.menu.mom.home.view

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
import com.bumptech.glide.Glide
import com.healthmate.common.constant.Urls
import com.healthmate.di.injector
import com.healthmate.menu.mom.home.data.BerandaViewModel
import com.healthmate.menu.mom.home.data.CheckUpModel
import com.healthmate.menu.reusable.data.Menu
import com.healthmate.menu.reusable.data.User
import kotlinx.android.synthetic.main.fragment_beranda.*
import java.util.*

class BerandaFragment : BaseFragment() {
    lateinit var materialDialog: MaterialDialog

    override fun getViewId(): Int = R.layout.fragment_beranda
    lateinit var adapter: MenuAdapter
    var status_api: Boolean = false //false = api belum selesai
    var status_checkup: Boolean = false //false = tidak ada checkup/belum rating, true = ada check up
    var status_rating: Boolean = false //false = tidak ada tanggungan rating, true = ada tanggungan rating
    var checkUp = CheckUpModel()

    private val viewModel by lazy {
        ViewModelProviders.of(this, injector.homeVM()).get(BerandaViewModel::class.java)
    }


    companion object {
        fun newInstance(): BerandaFragment = BerandaFragment()
    }

    override fun onFragmentCreated(savedInstanceState: Bundle?) {
        setRecycleView()
        if (userPref.getUser()!=null){
            if (userPref.getUser().city!=null && userPref.getUser().city!!.name.equals("")){
                ll_profile_done.visibility = View.GONE
                ll_profile_not.visibility = View.VISIBLE
                tv_nama_not.text = "Hei, ${user.name}"
                iv_banner.visibility = View.GONE
                checkup_inprogress.visibility = View.GONE
            } else{
                ll_profile_done.visibility = View.VISIBLE
                ll_profile_not.visibility = View.GONE
                tv_nama_done.text = "Pasien\n${user.name}"
                checkup_inprogress.visibility = View.GONE
                Glide.with(this).applyDefaultRequestOptions(requestOptionsMom).load(userPref.getUser().profil_picture).into(iv_profile_done)
                if (!userPref.getUser().covid_status.equals("")){
                    iv_banner.visibility = View.GONE
                    if (!userPref.getUser().hospital!!.id.equals("")){
                        tv_checkup.text = "Anda sudah memilih lokasi checkup"
                        checkup_inprogress.visibility = View.VISIBLE
                    } else{
                        checkup_inprogress.visibility = View.GONE
                        tv_checkup.text = "Periksa Sekarang / Check Up"
                    }
                } else{
                    iv_banner.visibility = View.VISIBLE
                }
            }
        }
        tv_update.setOnClickListener {
            navigator.dataKiaMom(activity!!)
        }
        iv_banner.setOnClickListener {
            navigator.screeningCovid(activity!!)
        }
        ll_checkup.setOnClickListener {
            if (userPref.getUser().city!=null && userPref.getUser().city!!.name.equals("")){
                createDialog("Anda belum menambah data KIA!")
            } else{
                if (!userPref.getUser().covid_status.equals("")){
                    if (userPref.getUser().hospital!!.id.equals("")){
                        navigator.checkUp(activity!!)
                    } else{
                        if (tv_checkup.text.toString().equals("Anda sedang melakukan pemeriksaan")){
                            createDialog("Anda sedang menjalani pemeriksaan")
                        } else if (tv_checkup.text.toString().equals("Anda belum memberikan rating")){
                            createDialog("Anda belum melakukan penilaian",{
                                openDialogRating()
                            })
                        } else if (tv_checkup.text.toString().equals("Anda sudah memilih lokasi checkup")){
                            createDialog("Anda sudah memilih lokasi checkup")
                        }
                    }
                } else{
                    createDialog("Anda belum melakukan cek status covid!")
                }
            }
        }
    }

    private fun openDialogRating() {

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
                    navigator.rapor(activity!!)
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

    override fun onResume() {
        super.onResume()
        if (userPref.getUser()!=null){
            if (userPref.getUser().city!=null && userPref.getUser().city!!.name.equals("")){
                ll_profile_done.visibility = View.GONE
                ll_profile_not.visibility = View.VISIBLE
                tv_nama_not.text = "Hei, ${userPref.getUser().name}"
                iv_banner.visibility = View.GONE
            } else{
                ll_profile_done.visibility = View.VISIBLE
                ll_profile_not.visibility = View.GONE
                tv_nama_done.text = "Pasien\n${userPref.getUser().name}"
                if (!userPref.getUser().covid_status.equals("")){
                    iv_banner.visibility = View.GONE
                    if (!userPref.getUser().hospital!!.id.equals("")){
                        checkup_inprogress.visibility = View.VISIBLE
                        tv_checkup.text = "Anda sudah memilih lokasi checkup"
                        getDataCheckup()
                    } else{
                        tv_checkup.text = "Periksa Sekarang / Check Up"
                        checkup_inprogress.visibility = View.GONE
                    }
                } else{
                    iv_banner.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun getDataCheckup() {
        val payload = Payload()
        payload.url = "${Urls.action}?num=1&mother_id=${userPref.getUser().id}"
        viewModel.statusCheckUp(payload)
                .observe(this, Observer {result ->
                    when(result.status){
                        Result.Status.LOADING->{
                        }
                        Result.Status.SUCCESS->{
                            status_api = true
                            var data = result.data!!
                            if (data.size>0){
                                checkUp = data.get(0)
                                if (checkUp.in_progress){
                                    tv_checkup.text = "Anda sedang melakukan pemeriksaan"
                                } else{
                                    if (checkUp.rating.equals("0")){
                                        tv_checkup.text = "Anda belum memberikan rating"
                                    }
                                }
                            } else{
                                status_checkup = false
                            }
                        }
                        Result.Status.ERROR->{
                            Fun.handleError(activity!!,result)
                        }
                    }
                })
    }
}