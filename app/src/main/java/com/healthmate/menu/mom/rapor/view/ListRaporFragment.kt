package com.healthmate.menu.mom.rapor.view

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.artcak.starter.modules.reusable.adapter.RaporAdapter
import com.healthmate.R
import com.healthmate.api.Payload
import com.healthmate.api.Result
import com.healthmate.common.adapter.RecyclerViewClickListener
import com.healthmate.common.base.BaseFragment
import com.healthmate.common.constant.Urls
import com.healthmate.common.functions.Fun
import com.healthmate.commons.helper.EndlessScrollListener
import com.healthmate.di.injector
import com.healthmate.menu.mom.rapor.data.AncModel
import kotlinx.android.synthetic.main.fragment_list_rapor.*
import kotlin.collections.ArrayList
import androidx.lifecycle.Observer
import com.healthmate.menu.reusable.data.MasterViewModel

class ListRaporFragment : BaseFragment() {

    override fun getViewId(): Int = R.layout.fragment_list_rapor

    companion object {
        fun newInstance(): ListRaporFragment = ListRaporFragment()
    }
    lateinit var raporAdapter: RaporAdapter
    var list: ArrayList<AncModel> = ArrayList()
    var cursor: String = ""

    private val viewModel by lazy {
        ViewModelProviders.of(this, injector.masterVM()).get(MasterViewModel::class.java)
    }

    override fun onFragmentCreated(savedInstanceState: Bundle?) {
        setRecycleView()
        getData("awal")
    }

    private fun getData(keterangan: String) {
        val payload = Payload()
        if (keterangan.equals("load")){
            payload.url = "${Urls.ancsMom}?mother_id=${userPref.getUser().id}&cursor=${cursor}"
        } else{
            payload.url = "${Urls.ancsMom}?mother_id=${userPref.getUser().id}"
        }
        viewModel.getAncs(payload)
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
                                        raporAdapter.lists.add(data)
                                    }
                                    list.clear()
                                    list.addAll(raporAdapter.lists)
                                }
                                raporAdapter.notifyDataSetChanged()
                            } else{
                                raporAdapter.lists.clear()
                                raporAdapter.lists.addAll(result.data!!)
                                list.clear()
                                list.addAll(raporAdapter.lists)
                                raporAdapter.notifyDataSetChanged()
                                if (raporAdapter.lists.size>0){
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
                            Fun.handleError(activity!!,result)
                        }
                    }
                })
    }

    fun setRecycleView(){
        raporAdapter = RaporAdapter(activity!!)
        rv_list.adapter = raporAdapter
        initiateLinearLayoutRecyclerView(rv_list,object : RecyclerViewClickListener {
            override fun onClick(view: View, position: Int) {

            }
            override fun onLongClick(view: View, position: Int) {
            }
        })
        rv_list.addOnScrollListener(
                object : EndlessScrollListener(){
                    override fun onLoadMore() {
                        if (list.size>19){
                            println("addOnScrollListener nextPage")
                            getData("load")
                        }
                    }

                }
        )
    }
}