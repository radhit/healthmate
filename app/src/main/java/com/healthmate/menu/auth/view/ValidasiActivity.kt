package com.healthmate.menu.auth.view

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.healthmate.R
import com.healthmate.common.base.BaseActivity
import com.healthmate.common.utils.Tools
import kotlinx.android.synthetic.main.activity_validasi.*

class ValidasiActivity : BaseActivity() {
    companion object {
        val EXTRA = "EXTRA"
        @JvmStatic
        fun getCallingIntent(activity: Activity, data: String): Intent {
            val intent = Intent(activity, ValidasiActivity::class.java)
            intent.putExtra(EXTRA,data)
            return intent
        }
    }

    override fun getView(): Int = R.layout.activity_validasi

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        tv_info.text = "OTP sudah dikirim ke nomor \n${Tools().getAsterik(intent.getStringExtra(EXTRA))}"
        btn_confirm.setOnClickListener {
            createDialog("Verifikasi berhasil",{
                navigator.signin(this,true)
            })
        }
        tv_kirim_ulang.setOnClickListener {
            createDialog("OTP berhasil dikirim ulang ke nomor ${Tools().getAsterik(intent.getStringExtra(EXTRA))}")
        }

    }

    override fun onBackPressed() {
        createDialogWithBackButton("HealthMate","Nomor anda tidak akan bisa digunakan bila tidak diverifikasi!",{
            finish()
        })
    }
}