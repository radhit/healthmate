package com.healthmate.menu.auth.view

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.healthmate.R
import com.healthmate.api.Payload
import com.healthmate.api.PayloadEntry
import com.healthmate.api.Result
import com.healthmate.common.base.BaseActivity
import com.healthmate.common.constant.Urls
import com.healthmate.common.functions.Fun
import com.healthmate.common.utils.Tools
import com.healthmate.di.injector
import com.healthmate.menu.auth.data.AuthViewModel
import com.healthmate.menu.reusable.data.User
import kotlinx.android.synthetic.main.activity_signin.*
import kotlinx.android.synthetic.main.activity_validasi.*
import java.util.*

class ValidasiActivity : BaseActivity() {
    companion object {
        val EXTRA = "EXTRA"
        val EXTRA_KETERANGAN = "EXTRA_KETERANGAN"
        @JvmStatic
        fun getCallingIntent(activity: Activity, data: String, keterangan:String): Intent {
            val intent = Intent(activity, ValidasiActivity::class.java)
            intent.putExtra(EXTRA,data)
            intent.putExtra(EXTRA_KETERANGAN,keterangan)
            return intent
        }
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this, injector.authVM()).get(AuthViewModel::class.java)
    }

    override fun getView(): Int = R.layout.activity_validasi
    var user = User()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        user = gson.fromJson(intent.getStringExtra(EXTRA),User::class.java)
        tv_info.text = "OTP sudah dikirim ke nomor \n${Tools().getAsterik(user.phone_number)}"
        btn_confirm.setOnClickListener {
            verifikasi()

        }
        tv_kirim_ulang.setOnClickListener {
            createDialog("OTP berhasil dikirim ulang ke nomor ${Tools().getAsterik(intent.getStringExtra(EXTRA))}")
        }

    }

    private fun verifikasi() {
        val payload = Payload()
        payload.payloads.add(PayloadEntry("number",fieldKode.text.toString()))
        payload.payloads.add(PayloadEntry("user_id",user.id))
        payload.payloads.add(PayloadEntry("user_type",user.type))
        viewModel.verifikasi(payload)
                .observe(this, Observer {result ->
                    when(result.status){
                        Result.Status.LOADING->{
                            showLoadingDialog()
                        }
                        Result.Status.SUCCESS->{
                            createDialog("Verifikasi berhasil",{
                                navigator.signin(this,true)
                            })
                        }
                        Result.Status.ERROR->{
                            closeLoadingDialog()
                            Fun.handleError(this,result)
                        }
                    }
                })
    }

    override fun onBackPressed() {
        createDialogWithBackButton("HealthMate","Nomor anda tidak akan bisa digunakan bila tidak diverifikasi!",{
            finish()
        })
    }
}