package com.healthmate.menu.midwife.pasien.view

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.afollestad.materialdialogs.MaterialDialog
import com.healthmate.R
import com.healthmate.api.Payload
import com.healthmate.api.Result
import com.healthmate.common.base.BaseFragment
import com.healthmate.common.constant.Urls
import com.healthmate.di.injector
import com.healthmate.menu.midwife.pasien.data.PasienViewModel
import com.healthmate.menu.reusable.data.User
import kotlinx.android.synthetic.main.fragment_main_inc.*

class MainIncFragment : BaseFragment() {
    override fun getViewId(): Int = R.layout.fragment_main_inc

    companion object {
        val EXTRA_DATA = "EXTRA_DATA"
        fun newInstance(data: String) = MainIncFragment().apply {
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

    override fun onFragmentCreated(savedInstanceState: Bundle?) {
        kala_1.setOnClickListener {
            navigator.mainKalaInc(activity!!,"kala-1",gson.toJson(dataMother))
        }
        kala_2.setOnClickListener {
            navigator.mainKalaInc(activity!!,"kala-2",gson.toJson(dataMother))
        }
        kala_3.setOnClickListener {
            navigator.mainKalaInc(activity!!,"kala-3",gson.toJson(dataMother))
        }
        kala_4.setOnClickListener {
            navigator.mainKalaInc(activity!!,"kala-4",gson.toJson(dataMother))
        }
        btn_ringkasan.setOnClickListener {
            navigator.mainSummaryInc(activity!!,gson.toJson(dataMother))
        }
        btn_catatan_bayi.setOnClickListener {
            navigator.mainBabyNoteInc(activity!!, gson.toJson(dataMother))
        }
        btn_rujukan.setOnClickListener {
            navigator.mainRujukan(activity!!, gson.toJson(dataMother))
        }
        btn_balikan_rujukan.setOnClickListener {
            navigator.mainBalikanRujukan(activity!!, gson.toJson(dataMother))
        }
//        getData()
    }

    private fun getData() {
        val payload = Payload()
        payload.url = "${Urls.registerMother}/${dataMother.id}/incs"
        viewModel.getInc(payload)
                .observe(this, Observer {result ->
                    when(result.status){
                        Result.Status.LOADING->{
                            startLoading()
                        }
                        Result.Status.SUCCESS->{
                            finishLoading()
                            var data = result.data!!
                            println("data inc: ${data}")
                        }
                        Result.Status.ERROR->{
                            finishLoading()
                            createDialog(result.message!!)
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

    override fun onResume() {
        super.onResume()
        (activity as MainDetilPasienActivity?)!!.getDataMother()
//        getData()
    }

}