package com.healthmate.menu.midwife.pasien.view

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.afollestad.materialdialogs.MaterialDialog
import com.artcak.starter.modules.reusable.adapter.AncListAdapter
import com.artcak.starter.modules.reusable.adapter.PasienListAdapter
import com.artcak.starter.modules.reusable.adapter.PncListAdapter
import com.artcak.starter.modules.reusable.adapter.RaporAdapter
import com.healthmate.R
import com.healthmate.api.Payload
import com.healthmate.api.Result
import com.healthmate.common.adapter.RecyclerViewClickListener
import com.healthmate.common.base.BaseFragment
import com.healthmate.common.constant.Urls
import com.healthmate.common.functions.Fun
import com.healthmate.common.functions.replaceEmpty
import com.healthmate.commons.helper.EndlessScrollListener
import com.healthmate.di.injector
import com.healthmate.menu.midwife.pasien.data.PasienViewModel
import com.healthmate.menu.midwife.pasien.data.PncModel
import com.healthmate.menu.mom.rapor.data.AncModel
import com.healthmate.menu.reusable.data.User
import kotlinx.android.synthetic.main.fragment_list_rapor.*
import kotlinx.android.synthetic.main.fragment_list_rapor.rv_list
import kotlinx.android.synthetic.main.fragment_list_rapor.tv_loading
import kotlinx.android.synthetic.main.fragment_main_pnc.*

class MainPncFragment : BaseFragment() {
    override fun getViewId(): Int = R.layout.fragment_main_pnc

    companion object {
        val EXTRA_DATA = "EXTRA_DATA"
        fun newInstance(data: String) = MainPncFragment().apply {
            arguments = Bundle().apply {
                putString(EXTRA_DATA,data)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.getString(EXTRA_DATA)?.let {
            dataMother = gson.fromJson(it,User::class.java)
        }
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this, injector.pasienVM()).get(PasienViewModel::class.java)
    }

    var materialDialog: MaterialDialog? = null
    var dataMother = User()
    lateinit var adapter: PncListAdapter
    var list: ArrayList<PncModel> = ArrayList()
    var cursor: String = ""

    override fun onFragmentCreated(savedInstanceState: Bundle?) {
        setRecycleView()
        getData("awal")
        btn_tambah.setOnClickListener {
            navigator.formPnc(activity!!, gson.toJson(dataMother))
        }
    }

    fun setRecycleView(){
        adapter = PncListAdapter(activity!!)
        rv_list.adapter = adapter
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

    private fun getData(keterangan: String) {
        val payload = Payload()
        if (keterangan.equals("load")){
            payload.url = "${Urls.pnc}?mother_id=${dataMother.id}&midwife_id=${userPref.getUser().id}&cursor=${cursor}"
        } else{
            payload.url = "${Urls.pnc}?mother_id=${dataMother.id}&midwife_id=${userPref.getUser().id}"
        }
        viewModel.getListPnc(payload)
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
                                    list.clear()
                                    list.addAll(adapter.lists)
                                }
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
                            createDialog(result.message!!)
                        }
                    }
                })
    }

    override fun onResume() {
        super.onResume()
        getData("awal")
    }

}