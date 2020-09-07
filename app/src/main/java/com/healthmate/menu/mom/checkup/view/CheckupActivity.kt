package com.healthmate.menu.mom.checkup.view

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.healthmate.R
import com.healthmate.common.base.BaseActivity
import com.healthmate.menu.mom.kia.view.MainKiaActivity
import com.healthmate.menu.reusable.data.Hospital
import com.healthmate.menu.reusable.data.MasterListModel
import kotlinx.android.synthetic.main.activity_checkup.*
import kotlinx.android.synthetic.main.activity_main_kia.*

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        this.setTitle("Check Up")
        cv_bpm.setOnClickListener {
            currentHospital = "Bidan Praktik Mandiri"
            setPilihan()
        }
        cv_pkm.setOnClickListener {
            currentHospital = "Puskesmas"
            setPilihan()
        }
        cv_rs.setOnClickListener {
            currentHospital = "Rumah Sakit"
            setPilihan()
        }
        btn_next.setOnClickListener {
            if (choosedHospital.equals("")){
                fieldHospital.setError("Anda belum memilih ${currentHospital}")
            } else{
                cv_pilihan.visibility = View.GONE
                cv_final.visibility = View.VISIBLE
                tv_hospital_name.text = choosedHospital
            }
        }
        fieldHospital.setOnClickListener {
            navigator.dataMaster(this,"hospital",1)
        }
        btn_change_hospital.setOnClickListener {
            cv_final.visibility = View.GONE
            cv_pilihan.visibility = View.VISIBLE
        }
        btn_submit.setOnClickListener {
            var hospital = Hospital()
            hospital.id = "1"
            hospital.level = currentHospital
            hospital.name = choosedHospital
            var user = userPref.getUser()
            user.hospital = hospital
            userPref.setUser(user)
            finish()
        }
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
                var dataMaster = gson.fromJson(data!!.getStringExtra("data"), MasterListModel::class.java)
                fieldHospital.setText(dataMaster.name)
                choosedHospital = dataMaster.name
            }
        }

    }
}