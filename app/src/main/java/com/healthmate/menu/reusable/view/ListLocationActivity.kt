package com.healthmate.menu.reusable.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.artcak.starter.modules.reusable.adapter.ListHospitalAdapter
import com.artcak.starter.modules.reusable.adapter.ListLocationAdapter
import com.healthmate.R
import com.healthmate.api.Payload
import com.healthmate.api.Result
import com.healthmate.common.adapter.RecyclerViewClickListener
import com.healthmate.common.base.BaseActivity
import com.healthmate.common.constant.Urls
import com.healthmate.common.functions.Fun
import com.healthmate.common.functions.setOnClick
import com.healthmate.commons.helper.EndlessScrollListener
import com.healthmate.di.injector
import com.healthmate.menu.reusable.data.Hospital
import com.healthmate.menu.reusable.data.Location
import com.healthmate.menu.reusable.data.MasterViewModel
import kotlinx.android.synthetic.main.activity_list_location.rv_list
import kotlinx.android.synthetic.main.activity_list_location.tv_loading

class ListLocationActivity : BaseActivity() {
    companion object {
        val EXTRA = "EXTRA"
        @JvmStatic
        fun getCallingIntent(activity: Activity, keterangan: String): Intent {
            val intent = Intent(activity, ListLocationActivity::class.java)
            intent.putExtra(EXTRA, keterangan)
            return intent
        }
    }
    private val viewModel by lazy {
        ViewModelProviders.of(this, injector.masterVM()).get(MasterViewModel::class.java)
    }
    override fun getView(): Int = R.layout.activity_list_location
    var adapter: ListLocationAdapter = ListLocationAdapter()
    var adapterHospital: ListHospitalAdapter = ListHospitalAdapter()
    var list: ArrayList<Location> = ArrayList()
    var listHospital: ArrayList<Hospital> = ArrayList()
    var cursor: String = ""
    var level = ""

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        this.setTitle("Daftar ${intent.getStringExtra(EXTRA)}")
        level = intent.getStringExtra(EXTRA)
        setRecycleView()
        if(level.equals("kabupaten") || level.equals("kecamatan")){
            getData()
        } else{
            getHospital()
        }
    }

    private fun getHospital(keterangan: String = "awal") {
        val payload = Payload()
        var level = ""
        if (!intent.getStringExtra(EXTRA).equals("bidan")){
            level = intent.getStringExtra(EXTRA)
        }
        if (keterangan.equals("awal")){
            payload.url = "${Urls.hospital}?num=50&level=${level}&city=${userPref.getUser().city!!.name}&district=${userPref.getUser().district!!.name}"
        } else{
            payload.url = "${Urls.hospital}?num=50&level=${level}&city=${userPref.getUser().city!!.name}&district=${userPref.getUser().district!!.name}&cursor=${cursor}"
        }
        viewModel.getHospital(payload)
                .observe(this, Observer {result ->
                    when(result.status){
                        Result.Status.LOADING->{
                            tv_loading.visibility = View.VISIBLE
                            rv_list.visibility = View.GONE
                        }
                        Result.Status.SUCCESS->{
                            cursor = result.cursor
                            if (keterangan.equals("load")){
                                if (result.data!!.size>0){
                                    for (data in result.data!!){
                                        adapterHospital.lists.add(data)
                                    }
                                }
                                listHospital.clear()
                                listHospital.addAll(adapterHospital.lists)

                                adapterHospital.notifyDataSetChanged()
                            } else{
                                adapterHospital.lists.clear()
                                adapterHospital.lists.addAll(result.data!!)
                                listHospital.clear()
                                listHospital.addAll(adapterHospital.lists)

                                adapterHospital.notifyDataSetChanged()
                                if (adapterHospital.lists.size>0){
                                    rv_list.visibility = View.VISIBLE
                                    tv_loading.visibility = View.GONE
                                } else{
                                    rv_list.visibility = View.GONE
                                    tv_loading.visibility = View.VISIBLE
                                    tv_loading.text = "Data Kosong"
                                }
                            }
                        }
                        Result.Status.ERROR->{
                            tv_loading.visibility = View.VISIBLE
                            rv_list.visibility = View.GONE
                            tv_loading.text = "Data Kosong"
                            Fun.handleError(this,result)
                        }
                    }
                })
    }

    private fun getData(keterangan: String= "awal") {
        val payload = Payload()
        if (level.equals("kabupaten")){
            if (keterangan.equals("awal")){
                payload.url = "${Urls.location}?num=50&level=city"
            } else{
                payload.url = "${Urls.location}?num=50&level=city&cursor=${cursor}"
            }
        } else{
            if (keterangan.equals("awal")){
                payload.url = "${Urls.location}?num=50&level=district"
            } else{
                payload.url = "${Urls.location}?num=50&level=district&cursor=${cursor}"
            }
        }
        viewModel.getLocation(payload)
                .observe(this, Observer {result ->
                    when(result.status){
                        Result.Status.LOADING->{
                            tv_loading.visibility = View.VISIBLE
                            rv_list.visibility = View.GONE
                        }
                        Result.Status.SUCCESS->{
                            cursor = result.cursor
                            if (keterangan.equals("load")){
                                if (result.data!!.size>0){
                                    for (data in result.data!!){
                                        adapter.lists.add(data)
                                    }
                                }
                                list.clear()
                                list.addAll(adapter.lists)

                                adapter.notifyDataSetChanged()
                            } else{
                                adapter.lists.clear()
                                adapter.lists.addAll(result.data!!)
                                list.clear()
                                list.addAll(adapter.lists)

                                adapter.notifyDataSetChanged()
                                if (adapter.lists.size>0){
                                    rv_list.visibility = View.VISIBLE
                                    tv_loading.visibility = View.GONE
                                } else{
                                    rv_list.visibility = View.GONE
                                    tv_loading.visibility = View.VISIBLE
                                    tv_loading.text = "Data Kosong"
                                }
                            }
                        }
                        Result.Status.ERROR->{
                            tv_loading.visibility = View.VISIBLE
                            rv_list.visibility = View.GONE
                            tv_loading.text = "Data Kosong"
                            Fun.handleError(this,result)
                        }
                    }
                })
    }

    private fun setRecycleView() {
        if(level.equals("kabupaten") || level.equals("kecamatan")){
            rv_list.adapter = adapter
        } else{
            rv_list.adapter = adapterHospital
        }
        rv_list.setOnClick(object : RecyclerViewClickListener {
            override fun onClick(view: View, position: Int) {
                val intent = Intent()
                if(level.equals("kabupaten") || level.equals("kecamatan")){
                    intent.putExtra("data",gson.toJson(adapter.lists.get(position)))
                } else{
                    intent.putExtra("data",gson.toJson(adapterHospital.lists.get(position)))
                }
                setResult(Activity.RESULT_OK,intent)
                finish()
            }
            override fun onLongClick(view: View, position: Int) {

            }
        })
        rv_list.addOnScrollListener(
                object : EndlessScrollListener(){
                    override fun onLoadMore() {
                        if(level.equals("kabupaten") || level.equals("kecamatan")){
                            if (list.size>19){
                                println("addOnScrollListener nextPage")
                                getData("load")
                            }
                        } else{
                            if (listHospital.size>19){
                                println("addOnScrollListener nextPage")
                                getHospital("load")
                            }
                        }

                    }

                }
        )
    }
}