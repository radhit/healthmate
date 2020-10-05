package com.healthmate.menu.midwife.pasien.view

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.healthmate.R
import com.healthmate.api.Payload
import com.healthmate.api.Result
import com.healthmate.common.adapter.RecyclerViewClickListener
import com.healthmate.common.base.BaseFragment
import com.healthmate.common.constant.Urls
import com.healthmate.commons.helper.EndlessScrollListener
import com.healthmate.di.injector
import kotlinx.android.synthetic.main.fragment_list_rapor.*
import kotlin.collections.ArrayList
import androidx.lifecycle.Observer
import com.artcak.starter.modules.reusable.adapter.PasienListAdapter
import com.artcak.starter.modules.reusable.adapter.PasienRujukanAdapter
import com.healthmate.menu.midwife.pasien.data.PasienViewModel
import com.healthmate.menu.reusable.data.User

class ListPasienHistoryFragment : BaseFragment() {

    override fun getViewId(): Int = R.layout.fragment_list_rapor

    companion object {
        fun newInstance(): ListPasienHistoryFragment = ListPasienHistoryFragment()
    }
    lateinit var pasienListAdapter: PasienListAdapter
    var listPasien: ArrayList<User> = ArrayList()
    var cursor: String = ""
    private val viewModel by lazy {
        ViewModelProviders.of(this, injector.pasienVM()).get(PasienViewModel::class.java)
    }

    override fun onFragmentCreated(savedInstanceState: Bundle?) {
        setRecycleView()
        getData("awal")
    }

    private fun getData(keterangan: String) {
        val payload = Payload()
        if (keterangan.equals("load")){
            payload.url = "${Urls.registerMidwife}/${userPref.getUser().id}/mothers&cursor=${cursor}"
        } else{
            payload.url = "${Urls.registerMidwife}/${userPref.getUser().id}/mothers"
        }
        viewModel.listMother(payload)
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
                                        pasienListAdapter.lists.add(data)
                                    }
                                    listPasien.clear()
                                    listPasien.addAll(pasienListAdapter.lists)
                                }
                                pasienListAdapter.notifyDataSetChanged()
                            } else{
                                pasienListAdapter.lists.clear()
                                pasienListAdapter.lists.addAll(result.data!!)
                                listPasien.clear()
                                listPasien.addAll(pasienListAdapter.lists)
                                pasienListAdapter.notifyDataSetChanged()
                                if (pasienListAdapter.lists.size>0){
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
        pasienListAdapter = PasienListAdapter(activity!!)
        rv_list.adapter = pasienListAdapter
        initiateLinearLayoutRecyclerView(rv_list,object : RecyclerViewClickListener {
            override fun onClick(view: View, position: Int) {
                navigator.mainDetilPasien(activity!!,gson.toJson(pasienListAdapter.lists[position]))
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
}