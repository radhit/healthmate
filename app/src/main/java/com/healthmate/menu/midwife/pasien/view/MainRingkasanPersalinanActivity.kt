package com.healthmate.menu.midwife.pasien.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.healthmate.R
import com.healthmate.api.Payload
import com.healthmate.api.Result
import com.healthmate.common.base.BaseActivity
import com.healthmate.common.constant.Urls
import com.healthmate.common.functions.replaceEmpty
import com.healthmate.di.injector
import com.healthmate.menu.midwife.pasien.data.PasienViewModel
import com.healthmate.menu.midwife.pasien.data.Summary
import com.healthmate.menu.reusable.data.User
import kotlinx.android.synthetic.main.activity_main_ringkasan_persalinan.*

class MainRingkasanPersalinanActivity : BaseActivity() {
    companion object {
        val EXTRA = "EXTRA"
        @JvmStatic
        fun getCallingIntent(activity: Activity, data: String): Intent {
            val intent = Intent(activity, MainRingkasanPersalinanActivity::class.java)
            intent.putExtra(EXTRA,data)
            return intent
        }
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this, injector.pasienVM()).get(PasienViewModel::class.java)
    }

    override fun getView(): Int = R.layout.activity_main_ringkasan_persalinan
    var summary: Summary = Summary()
    var dataMother: User = User()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        dataMother = gson.fromJson(intent.getStringExtra(EXTRA),User::class.java)
        getData()
        btn_edit.setOnClickListener {
            navigator.formSummaryInc(this,gson.toJson(summary))
        }
    }

    private fun getData() {
        val payload = Payload()
        payload.url = "${Urls.registerMother}/${dataMother.id}/incs/summary"
        viewModel.getDataSummary(payload)
                .observe(this, Observer {result ->
                    when(result.status){
                        Result.Status.LOADING->{
                            showLoadingDialog()
                        }
                        Result.Status.SUCCESS->{
                            closeLoadingDialog()
                            summary = result.data!!
                            setData()
                        }
                        Result.Status.ERROR->{
                            closeLoadingDialog()
                            createDialog(result.message!!)
                        }
                    }
                })
    }

    private fun setData() {
        if (summary.date_birth!!.equals("")){
            tv_tanggal_persalinan.text = "-"
        } else{
            tv_tanggal_persalinan.text = summary.date_birth!!.split("T")[0]
        }
        tv_waktu_kelahiran.text = summary.time_birth!!.replaceEmpty("-")
        tv_umur_kehamilan.text = "${summary.age_pregnancy!!.replaceEmpty("-")} Minggu"
        tv_penolong_persalinan.text = summary.birth_attendant!!.replaceEmpty("-")
        tv_cara_persalinan.text = summary.mode_delivery!!.replaceEmpty("-")
        tv_keadaan_ibu.text = summary.mother_condition!!.replaceEmpty("-")
        tv_keterangan.text = summary.information!!.replaceEmpty("-")
    }
}