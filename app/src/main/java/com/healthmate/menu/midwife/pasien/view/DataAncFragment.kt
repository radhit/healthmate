package com.healthmate.menu.midwife.pasien.view

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.afollestad.materialdialogs.MaterialDialog
import com.artcak.starter.modules.reusable.adapter.AncListAdapter
import com.artcak.starter.modules.reusable.adapter.PasienListAdapter
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
import com.healthmate.menu.mom.rapor.data.AncModel
import com.healthmate.menu.reusable.data.MasterViewModel
import com.healthmate.menu.reusable.data.User
import kotlinx.android.synthetic.main.fragment_data_anc.*

class DataAncFragment : BaseFragment() {
    override fun getViewId(): Int = R.layout.fragment_data_anc

    companion object {
        val EXTRA_DATA = "EXTRA_DATA"
        fun newInstance(data: String) = DataAncFragment().apply {
            arguments = Bundle().apply {
                putString(EXTRA_DATA,data)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.getString(EXTRA_DATA)?.let {
            dataPasien = gson.fromJson(it,User::class.java)
        }
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this, injector.pasienVM()).get(PasienViewModel::class.java)
    }

    private val viewModelMaster by lazy {
        ViewModelProviders.of(this, injector.masterVM()).get(MasterViewModel::class.java)
    }

    var materialDialog: MaterialDialog? = null
    var dataPasien = User()
    lateinit var adapter: AncListAdapter
    var listAnc: ArrayList<AncModel> = ArrayList()
    var cursor: String = ""

    override fun onFragmentCreated(savedInstanceState: Bundle?) {
        setRecycleView()
        getDataMother()
        btn_data_riwayat.setOnClickListener {
            if (dataPasien.anc_history.id.equals("")){
                navigator.formHistoryAnc(activity!!, "insert",gson.toJson(dataPasien))
            } else{
                navigator.formHistoryAnc(activity!!, "edit",gson.toJson(dataPasien))
            }
        }
        btn_pemeriksaan.setOnClickListener {
            navigator.formInputAnc(activity!!, gson.toJson(dataPasien))
        }
    }

    private fun getDataMother() {
        val payload = Payload()
        payload.url = "${Urls.registerMother}/${dataPasien.id}"
        viewModelMaster.getDataMe(payload)
                .observe(this, Observer {result ->
                    when(result.status){
                        Result.Status.LOADING->{
                            startLoading()
                        }
                        Result.Status.SUCCESS->{
                            finishLoading()
                            dataPasien = result.data!!
                            if (dataPasien.anc_history.id.equals("")){
                                btn_data_riwayat.text = "Tambah Data Riwayat"
                            } else{
                                btn_data_riwayat.text = "Ubah Data Riwayat"
                            }
                            setHistoryView()
                            getData("awal")
                        }
                        Result.Status.ERROR->{
                            finishLoading()
                            createDialog(result.message!!)
                        }
                    }
                })
    }

    private fun setHistoryView() {
        tv_hpht.text = dataPasien.anc_history.hpht.replaceEmpty("-")
        tv_hpl.text = dataPasien.anc_history.hml.replaceEmpty("-")
        tv_kehamilan_ke.text = dataPasien.anc_history.preg_num.replaceEmpty("-")
        tv_total_persalinan.text = dataPasien.anc_history.labor_num.replaceEmpty("-")
        tv_total_keguguran.text = dataPasien.anc_history.miscarriage_num.replaceEmpty("-")
        tv_total_hidup.text = dataPasien.anc_history.live_child_num.replaceEmpty("-")
        tv_jarak.text = dataPasien.anc_history.prev_child_difference.replaceEmpty("-")
    }

    private fun getData(keterangan: String) {
        val payload = Payload()
        if (keterangan.equals("load")){
            payload.url = "${Urls.ancsMom}?mother_id=${dataPasien.id}&midwife_id=${userPref.getUser().id}&cursor=${cursor}"
        } else{
            payload.url = "${Urls.ancsMom}?mother_id=${dataPasien.id}&midwife_id=${userPref.getUser().id}"
        }
        viewModelMaster.getAncs(payload)
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
                                    listAnc.clear()
                                    listAnc.addAll(adapter.lists)
                                }
                                adapter.notifyDataSetChanged()
                            } else{
                                adapter.lists.clear()
                                adapter.lists.addAll(result.data!!)
                                listAnc.clear()
                                listAnc.addAll(adapter.lists)
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

    fun setRecycleView(){
        adapter = AncListAdapter(activity!!)
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
                        if (listAnc.size>19){
                            println("addOnScrollListener nextPage")
                            getData("load")
                        }
                    }

                }
        )
    }

    override fun onResume() {
        super.onResume()
        getDataMother()
    }

}