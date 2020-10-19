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
import com.healthmate.common.functions.Fun
import com.healthmate.di.injector
import com.healthmate.menu.reusable.data.MasterViewModel
import com.healthmate.menu.reusable.data.User
import kotlinx.android.synthetic.main.fragment_detil_pasien.*

class DetilPasienFragment : BaseFragment() {
    override fun getViewId(): Int = R.layout.fragment_detil_pasien

    companion object {
        val EXTRA_DATA = "EXTRA_DATA"
        fun newInstance(data: String) = DetilPasienFragment().apply {
            arguments = Bundle().apply {
                putString(EXTRA_DATA, data)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.getString(EXTRA_DATA)?.let {
            dataPasien = gson.fromJson(it, User::class.java)
        }
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this, injector.masterVM()).get(MasterViewModel::class.java)
    }

    var dataPasien = User()
    var materialDialog: MaterialDialog? = null

    override fun onFragmentCreated(savedInstanceState: Bundle?) {
        getData()
        btn_ubah.setOnClickListener {
            navigator.dataKiaMom(activity!!, "midwife_edit", gson.toJson(dataPasien))
        }
    }

    private fun getData() {
        val payload = Payload()
        payload.url = "${Urls.registerMother}/${dataPasien.id}"
        viewModel.getDataMe(payload)
                .observe(this, Observer { result ->
                    when (result.status) {
                        Result.Status.LOADING -> {
                            startLoading()
                        }
                        Result.Status.SUCCESS -> {
                            finishLoading()
                            dataPasien = result.data!!
                            setData()
                        }
                        Result.Status.ERROR -> {
                            finishLoading()
                            if (result.response_code==401){
                                result.message = "Token expired"
                            }
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
                .title(null, "Mohon Tunggu...")
                .message(null, "")
                .noAutoDismiss()
                .cancelable(false)
    }

    private fun setData() {
        tv_domisili.text = dataPasien.kia!!.domisili
        tv_nik.text = dataPasien.kia!!.nik
        tv_ttl.text = "${dataPasien.kia!!.birth_place}, ${dataPasien.kia!!.birth_date.split("T")[0]}"
        tv_hamil_ke.text = dataPasien.kia!!.number_of_pregnancy
        tv_umur_anak.text = "${dataPasien.kia!!.last_child_age} Tahun"
        tv_jkn.text = dataPasien.kia!!.jkn_number
        tv_pekerjaan.text = dataPasien.kia!!.job
        tv_agama.text = dataPasien.kia!!.religion
        tv_pendidikan.text = dataPasien.kia!!.last_education
        tv_goldar.text = dataPasien.kia!!.blood_type

        //data suami
        tv_nama_suami.text = dataPasien.kia!!.husband!!.name
        tv_domisili_suami.text = "${dataPasien.kia!!.husband!!.city!!.name} - ${dataPasien.kia!!.husband!!.district!!.name}"
        tv_ttl_suami.text = "${dataPasien.kia!!.husband!!.birth_place}, ${dataPasien.kia!!.husband!!.birth_date.split("T")[0]}"
        tv_pekerjaan_suami.text = dataPasien.kia!!.husband!!.job
        tv_agama_suami.text = dataPasien.kia!!.husband!!.religion
        tv_pendidikan_suami.text = dataPasien.kia!!.husband!!.last_education
        tv_goldar_suami.text = dataPasien.kia!!.husband!!.blood_type
        tv_nomor_suami.text = dataPasien.kia!!.husband!!.phone_number

        //data persalinan
        tv_penolong_persalinan.text = dataPasien.kia!!.persalinan!!.helper
        tv_dana_persalinan.text = dataPasien.kia!!.persalinan!!.funds
        tv_jenis_kendaraan.text = dataPasien.kia!!.persalinan!!.vehicle
        tv_metode.text = dataPasien.kia!!.persalinan!!.metode_kb
        tv_pendonor_darah.text = dataPasien.kia!!.persalinan!!.pendonor
        tv_kontak_pendonor.text = dataPasien.kia!!.persalinan!!.kontak_pendonor
    }

    override fun onResume() {
        super.onResume()
        getData()
        (activity as MainDetilPasienActivity?)!!.getDataMother()
    }

}