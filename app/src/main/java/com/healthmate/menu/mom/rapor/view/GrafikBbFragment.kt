package com.healthmate.menu.mom.rapor.view

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.healthmate.R
import com.healthmate.api.Payload
import com.healthmate.api.Result
import com.healthmate.common.base.BaseFragment
import com.healthmate.common.constant.Urls
import com.healthmate.common.functions.Fun
import com.healthmate.di.injector
import androidx.lifecycle.Observer
import com.healthmate.menu.mom.rapor.data.AncModel
import com.healthmate.menu.reusable.data.MasterViewModel
import kotlinx.android.synthetic.main.fragment_grafik_bb.*
import kotlinx.android.synthetic.main.fragment_list_rapor.tv_loading

class GrafikBbFragment : BaseFragment() {

    override fun getViewId(): Int = R.layout.fragment_grafik_bb
    private val viewModel by lazy {
        ViewModelProviders.of(this, injector.masterVM()).get(MasterViewModel::class.java)
    }
    var dataAnc: AncModel = AncModel()

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