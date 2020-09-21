package com.healthmate.menu.mom.home.view

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
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
import com.healthmate.api.DataResponse
import com.healthmate.common.constant.Urls
import com.healthmate.di.injector
import com.healthmate.menu.mom.home.data.BerandaViewModel
import com.healthmate.menu.mom.home.data.CheckUpModel
import com.healthmate.menu.reusable.data.Hospital
import com.healthmate.menu.reusable.data.MasterViewModel
import com.healthmate.menu.reusable.data.Menu
import com.healthmate.menu.reusable.data.User
import kotlinx.android.synthetic.main.fragment_beranda.*
import kotlinx.android.synthetic.main.fragment_data_anc.*
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class BerandaFragment : BaseFragment() {
    lateinit var materialDialog: MaterialDialog

    override fun getViewId(): Int = R.layout.fragment_beranda
    lateinit var adapter: MenuAdapter
    var status_api: Boolean = false //false = api belum selesai
    var finishedCheckup: Boolean = true
    var checkUp = CheckUpModel()

    private val viewModel by lazy {
        ViewModelProviders.of(this, injector.homeVM()).get(BerandaViewModel::class.java)
    }

    companion object {
        fun newInstance(): BerandaFragment = BerandaFragment()
    }

    override fun onFragmentCreated(savedInstanceState: Bundle?) {
        setRecycleView()
        setTampilan()
        tv_update.setOnClickListener {
            navigator.dataKiaMom(activity!!)
        }
        iv_banner.setOnClickListener {
            navigator.screeningCovid(activity!!)
        }
        getDataCheckup()
        cv_checkup.setOnClickListener {
            println("masuk sini 0")
            if (userPref.getUser().city!=null && userPref.getUser().city!!.name.equals("")){
                createDialog("Anda belum menambah data KIA!")
            } else{
                if (!userPref.getUser().covid_status.equals("")){
                    if (status_api){
                        if (userPref.getUser().hospital!!.id.equals("")){
                            if (checkUp.next_hospital!!.id.equals("")){
                                println("masuk sini 1")
                                navigator.checkUp(activity!!)
                            } else{
                                dialogSuggestion()
                            }
                        } else{
                            if (tv_checkup.text.toString().equals("Anda sudah memilih lokasi checkup")){
                                createDialog("Anda sudah memilih lokasi checkup")
                            } else if (tv_checkup.text.toString().equals("Anda belum memberikan rating")){
                                createDialog("Anda belum melakukan penilaian",{
                                    openDialogRating()
                                },"HealthMate","Rating Sekarang")
                            } else if (tv_checkup.text.toString().equals("Anda sudah memilih lokasi checkup")){
                                createDialog("Anda sudah memilih lokasi checkup")
                            }
                        }
                    } else{
                        createDialog("Sedang mengambil data")
                    }
                } else{
                    createDialog("Anda belum melakukan cek status covid!")
                }
            }
        }
    }

    private fun dialogSuggestion() {
        val dialog = MaterialDialog(activity!!).title(null,"HealthMate")
                .message(null, "Apakah anda ingin melakukan pemeriksaan rujukan bidan sebelumnya?")
                .positiveButton(null,"Ya",{
                    var user = userPref.getUser()
                    user.hospital = checkUp.next_hospital
                    userPref.setUser(user)
                    submitHospital()
                })
                .negativeButton(null, "Tidak",{
                    navigator.checkUp(activity!!)
                }).cancelable(true)
        dialog.show()
    }

    private fun submitHospital() {
        startLoading()
        val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson.toJson(userPref.getUser()))
        println("req body : ${requestBody}")
        val call: Call<DataResponse<Any>> = baseApi.updateDataMom("${Urls.registerMother}/${userPref.getUser().id}",requestBody)
        call.enqueue(object : Callback<DataResponse<Any>> {
            override fun onResponse(call: Call<DataResponse<Any>>?, response: Response<DataResponse<Any>>?) {
                finishLoading()
                if (response!!.isSuccessful) {
                    if (response!!.body()!!.responseCode in 200..299){
                        createDialog(response.body()!!.message,{
                            setTampilan()
                        })
                    } else{
                        createDialog(response.body()!!.message)
                    }
                } else {
                    Toast.makeText(activity!!,"Terjadi kesalahan", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<DataResponse<Any>>?, t: Throwable?) {
                finishLoading()
                Toast.makeText(activity!!,t!!.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun openDialogRating() {
        val dialog = Dialog(activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.item_rating)
        val tv_name = dialog.findViewById(R.id.tv_name) as TextView
        val tv_hospital = dialog.findViewById(R.id.tv_hospital_name) as TextView
        val rating = dialog.findViewById(R.id.rating) as RatingBar
        val btn_rating = dialog.findViewById(R.id.btn_rating) as Button
        tv_name.text = "Bidan ${checkUp.midwive.name}"
        tv_hospital.text = checkUp.midwive.hospital!!.name
        btn_rating.setOnClickListener {
            checkUp.rating = rating.rating.toInt()
            submitRating()
//            rating.rating.toString()
//            finish()
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun submitRating() {
        println("data rating : ${checkUp}")
        val payload = Payload()
        payload.url = "${Urls.action}/${checkUp.id}/rating"
        payload.payloads.add(PayloadEntry("value",checkUp.rating.toString()))
        viewModel.postRating(payload)
                .observe(this, Observer {result ->
                    when(result.status){
                        Result.Status.LOADING->{
                            startLoading()
                        }
                        Result.Status.SUCCESS->{
                            finishLoading()
                            createDialog(result.message!!,{
                                var hospital = Hospital()
                                var user = userPref.getUser()
                                user.hospital = hospital
                                userPref.setUser(user)
                                checkup_inprogress.visibility = View.GONE
                                tv_checkup.text = "Periksa Sekarang / Check Up"
                            })
                        }
                        Result.Status.ERROR->{
                            finishLoading()
                            Fun.handleError(activity!!,result)
                        }
                    }
                })
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
        setTampilan()
    }

    private fun setTampilan() {
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
                Glide.with(this).applyDefaultRequestOptions(requestOptionsMom).load(userPref.getUser().profile_picture).into(iv_profile_done)
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
        status_api = false
        val payload = Payload()
        payload.url = "${Urls.action}?num=1&mother_id=${userPref.getUser().id}"
        //081456789123
//        tv_checkup.text = "Mohon Tunggu, sedang mengambil data..."
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
                                if (!checkUp.finished){
                                    finishedCheckup = false
                                    if (checkUp.rating==0){
                                        tv_checkup.text = "Anda belum memberikan rating"
                                        openDialogRating()
                                    }
                                }
                            }
                        }
                        Result.Status.ERROR->{
                            Fun.handleError(activity!!,result)
                        }
                    }
                })
    }

    private fun finishLoading() {
        materialDialog?.dismiss()
    }

    private fun startLoading() {
        materialDialog = MaterialDialog(activity!!)
                .title(null,"Mohon Tunggu...")
                .message(null,"")
                .noAutoDismiss()
                .cancelable(false)
    }

}