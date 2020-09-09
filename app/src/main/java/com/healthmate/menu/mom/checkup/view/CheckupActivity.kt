package com.healthmate.menu.mom.checkup.view

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.healthmate.R
import com.healthmate.api.DataResponse
import com.healthmate.common.base.BaseActivity
import com.healthmate.common.constant.Urls
import com.healthmate.menu.mom.kia.view.MainKiaActivity
import com.healthmate.menu.reusable.data.Hospital
import com.healthmate.menu.reusable.data.MasterListModel
import kotlinx.android.synthetic.main.activity_checkup.*
import kotlinx.android.synthetic.main.activity_main_kia.*
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckupActivity : BaseActivity() {
    companion object {
        val EXTRA = "EXTRA"
        @JvmStatic
        fun getCallingIntent(activity: Activity): Intent {
            val intent = Intent(activity, CheckupActivity::class.java)
            return intent
        }
    }
    var currentHospital: String = ""
    var choosedHospital: String = ""
    override fun getView(): Int = R.layout.activity_checkup
    var hospital = Hospital()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        this.setTitle("Check Up")
        cv_bpm.setOnClickListener {
            currentHospital = "bpm"
            setPilihan()
        }
        cv_pkm.setOnClickListener {
            currentHospital = "puskesmas"
            setPilihan()
        }
        cv_rs.setOnClickListener {
            currentHospital = "rs"
            setPilihan()
        }
        btn_next.setOnClickListener {
            if (hospital.name.equals("")){
                fieldHospital.setError("Anda belum memilih lokasi pemeriksaan")
            } else{
                cv_pilihan.visibility = View.GONE
                cv_final.visibility = View.VISIBLE
                tv_hospital_name.text = hospital.name
            }
        }
        fieldHospital.setOnClickListener {
            navigator.listLocation(this,currentHospital,1)
        }
        btn_change_hospital.setOnClickListener {
            cv_final.visibility = View.GONE
            cv_pilihan.visibility = View.VISIBLE
        }
        btn_submit.setOnClickListener {
            var user = userPref.getUser()
            user.hospital = hospital
            userPref.setUser(user)
            updateData()
        }
    }

    private fun updateData() {
        startLoading()
        val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson.toJson(userPref.getUser()))
        println("req body : ${requestBody}")
        val call: Call<DataResponse<Any>> = baseApi.updateDataMom("${Urls.registerMother}/${userPref.getUser().id}",requestBody)
        call.enqueue(object : Callback<DataResponse<Any>> {
            override fun onResponse(call: Call<DataResponse<Any>>?, response: Response<DataResponse<Any>>?) {
                finishLoading()
                if (response!!.isSuccessful) {
                    if (response!!.body()!!.responseCode in 200..299){
                        createDialog(response.body()!!.message,{
                            finish()
                        })
                    } else{
                        createDialog(response.body()!!.message)
                    }
                } else {
                    Toast.makeText(this@CheckupActivity,"Terjadi kesalahan", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<DataResponse<Any>>?, t: Throwable?) {
                finishLoading()
                Toast.makeText(this@CheckupActivity,t!!.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun setPilihan() {
        ll_pemilihan_tempat.visibility = View.GONE
        cv_pilihan.visibility = View.VISIBLE
        tv_judul.text = "Pilihan ${currentHospital}"
        inputHospital.hint = "Daftar ${currentHospital}"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==1){
            if (resultCode==Activity.RESULT_OK){
                var dataMaster = gson.fromJson(data!!.getStringExtra("data"), Hospital::class.java)
                fieldHospital.setText(dataMaster.name)
                hospital = dataMaster
            }
        }
    }

    override fun startLoading() {
        super.startLoading()
        btn_submit.isEnabled = false
        btn_submit.text = "Mohon Tunggu..."
    }

    override fun finishLoading() {
        super.finishLoading()
        btn_submit.isEnabled = true
        btn_submit.text = "Lanjutkan"
    }
}