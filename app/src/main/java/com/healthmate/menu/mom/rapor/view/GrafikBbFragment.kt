package com.healthmate.menu.mom.rapor.view

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.afollestad.materialdialogs.MaterialDialog
import com.artcak.starter.modules.reusable.adapter.RaporAdapter
import com.bumptech.glide.Glide
import com.healthmate.R
import com.healthmate.api.Payload
import com.healthmate.api.Result
import com.healthmate.common.base.BaseFragment
import com.healthmate.common.constant.Urls
import com.healthmate.common.functions.Fun
import com.healthmate.di.injector
import com.healthmate.menu.mom.rapor.data.RaporViewModel
import kotlinx.android.synthetic.main.fragment_list_rapor.*
import java.util.*
import androidx.lifecycle.Observer
import com.healthmate.menu.mom.rapor.data.RaporModel
import kotlinx.android.synthetic.main.fragment_grafik_bb.*
import kotlinx.android.synthetic.main.fragment_list_rapor.tv_loading

class GrafikBbFragment : BaseFragment() {

    override fun getViewId(): Int = R.layout.fragment_grafik_bb
    private val viewModel by lazy {
        ViewModelProviders.of(this, injector.raporVM()).get(RaporViewModel::class.java)
    }
    var dataAnc: RaporModel = RaporModel()

    companion object {
        fun newInstance(): GrafikBbFragment = GrafikBbFragment()
    }

    override fun onFragmentCreated(savedInstanceState: Bundle?) {
        getData()
    }

    private fun getData() {
        val payload = Payload()
        payload.url = "${Urls.ancsMom}?mother_id=${userPref.getUser().id}&num=1"
        viewModel.getAncs(payload)
                .observe(this, Observer {result ->
                    when(result.status){
                        Result.Status.LOADING->{
                            tv_loading.visibility = View.VISIBLE
                            ll_data.visibility = View.GONE
                        }
                        Result.Status.SUCCESS->{
                            if (result.data!!.size>0){
                                tv_loading.visibility = View.GONE
                                ll_data.visibility = View.VISIBLE
                                dataAnc = result.data!![0]
                            } else{
                                tv_loading.visibility = View.VISIBLE
                                ll_data.visibility = View.GONE
                                tv_loading.text = "Data Kosong"
                            }
                        }
                        Result.Status.ERROR->{
                            tv_loading.visibility = View.VISIBLE
                            ll_data.visibility = View.GONE
                            tv_loading.text = "Data Kosong"
                            Fun.handleError(activity!!,result)
                        }
                    }
                })
    }
}