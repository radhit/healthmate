package com.healthmate.menu.midwife.rujukan.view

import android.content.Context
import android.os.Bundle
import android.view.View
import com.artcak.starter.modules.reusable.adapter.PasienRujukanAdapter
import com.healthmate.R
import com.healthmate.common.adapter.RecyclerViewClickListener
import com.healthmate.common.base.BaseFragment
import com.healthmate.commons.helper.EndlessScrollListener
import kotlinx.android.synthetic.main.fragment_list_rapor.*

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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.getString(EXTRA_DATA)?.let {
            keterangan = it
        }
    }
    var keterangan: String = ""
    lateinit var pasienRujukanAdapter: PasienRujukanAdapter

    override fun onFragmentCreated(savedInstanceState: Bundle?) {
        setRecycleView()
        getData()

    }

    private fun getData() {
        if (keterangan.equals("penerimaan")){

        } else{

        }
    }

    fun setRecycleView(){
        pasienRujukanAdapter = PasienRujukanAdapter(activity!!, keterangan)
        rv_list.adapter = pasienRujukanAdapter
        initiateLinearLayoutRecyclerView(rv_list,object : RecyclerViewClickListener {
            override fun onClick(view: View, position: Int) {
                navigator.mainDetilPasien(activity!!,gson.toJson(pasienRujukanAdapter.lists[position]))
            }
            override fun onLongClick(view: View, position: Int) {
            }
        })
        rv_list.addOnScrollListener(
                object : EndlessScrollListener(){
                    override fun onLoadMore() {
//                        if (listPasien.size>19){
//                            println("addOnScrollListener nextPage")
//                            getData("load")
//                        }
                    }

                }
        )
    }
}