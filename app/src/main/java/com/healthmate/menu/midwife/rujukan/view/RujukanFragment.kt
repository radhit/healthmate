package com.healthmate.menu.midwife.rujukan.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.artcak.starter.modules.reusable.adapter.PasienRujukanAdapter
import com.healthmate.R
import com.healthmate.api.Payload
import com.healthmate.api.Result
import com.healthmate.common.adapter.RecyclerViewClickListener
import com.healthmate.common.base.BaseFragment
import com.healthmate.common.constant.Urls
import com.healthmate.commons.helper.EndlessScrollListener
import com.healthmate.di.injector
import com.healthmate.menu.midwife.pasien.data.PasienViewModel
import com.healthmate.menu.midwife.rujukan.data.Rujukan
import com.healthmate.menu.midwife.rujukan.data.RujukanViewModel
import kotlinx.android.synthetic.main.fragment_list_rujukan.*

class RujukanFragment : BaseFragment() {

    override fun getViewId(): Int = R.layout.fragment_list_rujukan

    companion object {
        val EXTRA_DATA = "EXTRA_DATA"
        fun newInstance(keterangan: String) = RujukanFragment().apply {
            arguments = Bundle().apply {
                putString(EXTRA_DATA,keterangan)
            }
        }
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this, injector.rujukanVM()).get(RujukanViewModel::class.java)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.getString(EXTRA_DATA)?.let {
            keterangan = it
        }
    }
    var keterangan: String = ""
    lateinit var pasienRujukanAdapter: PasienRujukanAdapter
    var cursor: String = ""
    var listPasien: ArrayList<Rujukan> = arrayListOf()

    @SuppressLint("RestrictedApi")
    override fun onFragmentCreated(savedInstanceState: Bundle?) {
        setRecycleView()
        getData("awal")
        if (keterangan.equals("penerimaan")){
            fab_add.visibility = View.GONE
        } else{
            fab_add.visibility = View.VISIBLE
        }
        fab_add.setOnClickListener {
            navigator.listMotherAvailableReferences(activity!!)
        }
    }

    private fun getData(status: String) {
        var payload = Payload()
        var url = ""
        if (keterangan.equals("penerimaan")){
            url = "${Urls.rujukan}?to_hospital=${userPref.getUser().hospital!!.id}&num=100"
        } else{
            url = "${Urls.rujukan}?midwife_id=${userPref.getUser().id}&num=100"
        }
        if (status.equals("load")){
            url = "${url}&cursor=${cursor}"
        }
        payload.url = url
        viewModel.listReferences(payload)
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
                                        pasienRujukanAdapter.lists.add(data)
                                    }
                                    listPasien.clear()
                                    listPasien.addAll(pasienRujukanAdapter.lists)
                                }
                                pasienRujukanAdapter.notifyDataSetChanged()
                            } else{
                                pasienRujukanAdapter.lists.clear()
                                pasienRujukanAdapter.lists.addAll(result.data!!)
                                listPasien.clear()
                                listPasien.addAll(pasienRujukanAdapter.lists)
                                pasienRujukanAdapter.notifyDataSetChanged()
                                if (pasienRujukanAdapter.lists.size>0){
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
                            createDialog(result.message!!)
                        }
                    }
                })
    }

    fun setRecycleView(){
        pasienRujukanAdapter = PasienRujukanAdapter(activity!!, keterangan)
        rv_list.adapter = pasienRujukanAdapter
        initiateLinearLayoutRecyclerView(rv_list,object : RecyclerViewClickListener {
            override fun onClick(view: View, position: Int) {
                if (keterangan.equals("penerimaan")){
                    println("data rujukan : ${gson.toJson(pasienRujukanAdapter.lists[position])}")
                    navigator.formUmpanbalikRujukan(activity!!,gson.toJson(pasienRujukanAdapter.lists[position]))
                } else{
//                    navigator.formRujukan(activity!!,gson.toJson(pasienRujukanAdapter.lists[position]))
                }
            }
            override fun onLongClick(view: View, position: Int) {
            }
        })
        rv_list.addOnScrollListener(
                object : EndlessScrollListener(){
                    override fun onLoadMore() {
                        if (listPasien.size>19){
                            println("addOnScrollListener nextPage")
                            getData("load")
                        }
                    }

                }
        )
    }

    override fun onResume() {
        super.onResume()
        getData("awal")
    }
}