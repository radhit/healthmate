package com.healthmate.menu.mom.kia.view

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.healthmate.R
import com.healthmate.common.base.BaseActivity
import com.healthmate.menu.mom.covid.view.ScreeningCovidActivity
import com.healthmate.menu.reusable.data.Kia
import kotlinx.android.synthetic.main.activity_main_kia.*

class MainKiaActivity : BaseActivity() {
    companion object {
        val EXTRA = "EXTRA"
        @JvmStatic
        fun getCallingIntent(activity: Activity): Intent {
            val intent = Intent(activity, MainKiaActivity::class.java)
            return intent
        }
    }
    override fun getView(): Int = R.layout.activity_main_kia
    var dataKia: Kia = Kia()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        if (userPref.getUser().kia!=null){
            dataKia = userPref.getUser().kia!!
            setData()
        }
        fieldNama.setText("${userPref.getUser().name}")
        btn_simpan.setOnClickListener {
            var user = userPref.getUser()
            user.kia = dataKia
            userPref.setUser(user)
            finish()
        }
    }

    private fun setData() {
        fieldNomorKtp.setText("${dataKia.nik}")
        fieldTempatLahir.setText("${dataKia.birth_place}")
        fieldTanggalLahir.setText("${dataKia.birth_date}")
        fieldHamilKeberapa.setText("${dataKia.number_of_pregnancy}")
        fieldUmurAnak.setText("${dataKia.last_child_age}")
        fieldPendidikan.setText("${dataKia.last_education}")
        fieldPekerjaanMom.setText("${dataKia.job}")
        fieldAgamaMom.setText("${dataKia.religion}")
        fieldNomorJkn.setText("${dataKia.jkn_number}")
        fieldGoldarMom.setText("${dataKia.blood_type}")
        if (dataKia.husband!=null){
            fieldNamaSuami.setText("${dataKia.husband!!.name}")
            fieldTempatLahirSuami.setText("${dataKia.husband!!.birth_place}")
            fieldTanggalLahirSuami.setText("${dataKia.husband!!.birth_date}")
            fieldNomorHpSuami.setText("${dataKia.husband!!.phone_number}")
            fieldAgamaSuami.setText("${dataKia.husband!!.religion}")
            fieldGoldarSuami.setText("${dataKia.husband!!.blood_type}")
            fieldAlamat.setText("${dataKia.husband!!.address}")
            fieldKabupaten.setText("${dataKia.husband!!.districts}")
            fieldKecamatan.setText("${dataKia.husband!!.sub_districts}")
        }
    }
}