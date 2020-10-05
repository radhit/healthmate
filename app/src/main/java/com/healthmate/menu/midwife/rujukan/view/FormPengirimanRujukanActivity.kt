package com.healthmate.menu.midwife.rujukan.view

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.healthmate.R
import com.healthmate.common.base.BaseActivity
import com.healthmate.menu.midwife.pasien.view.MainDetilPasienActivity

class FormPengirimanRujukanActivity : BaseActivity() {
    companion object {
        val EXTRA = "EXTRA"
        @JvmStatic
        fun getCallingIntent(activity: Activity, data: String): Intent {
            val intent = Intent(activity, FormPengirimanRujukanActivity::class.java)
            intent.putExtra(EXTRA, data)
            return intent
        }
    }
    override fun getView(): Int = R.layout.activity_form_pengiriman_rujukan

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        TODO("Not yet implemented")
    }
}