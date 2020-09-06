package com.healthmate.menu.auth.view

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.healthmate.R
import com.healthmate.api.Payload
import com.healthmate.api.PayloadEntry
import com.healthmate.api.Result
import com.healthmate.common.base.BaseActivity
import com.healthmate.common.constant.Urls
import com.healthmate.common.functions.Fun
import com.healthmate.di.injector
import com.healthmate.menu.auth.data.AuthViewModel
import com.healthmate.menu.reusable.data.User
import kotlinx.android.synthetic.main.activity_signin.*
import java.util.*

class SigninActivity : BaseActivity() {
    companion object {
        val EXTRA = "EXTRA"
        @JvmStatic
        fun getCallingIntent(activity: Activity, keterangan: String): Intent {
            val intent = Intent(activity, SigninActivity::class.java)
            intent.putExtra(EXTRA,keterangan)
            return intent
        }
    }
    private val viewModel by lazy {
        ViewModelProviders.of(this, injector.authVM()).get(AuthViewModel::class.java)
    }

    override fun getView(): Int = R.layout.activity_signin
    var currentChar: String = "mom"

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        iv_mom.setOnClickListener {
            currentChar = "mom"
            changeImage()
        }
        iv_midwife.setOnClickListener {
            currentChar = "midwife"
            changeImage()
        }
        iv_doctor.setOnClickListener {
            createDialog("Masih dalam masa pengembangan")
        }
        btn_login.setOnClickListener {
            viewLogin()
        }
        btn_register.setOnClickListener {
            viewRegister()
        }

        btn_signin.setOnClickListener {
            if (isValidLogin()){
//                var dataJson = gson.fromJson(getString(R.string.testing_user),User::class.java)
//                userPref.setUser(dataJson)
//                navigator.mainMom(this,true)
                login()
            }
        }
        btn_signup.setOnClickListener {
            if (isValidRegister()){
                register()
            }
        }
    }

    private fun viewRegister() {
        btn_register.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.bg_primary))
        btn_register.setTextColor(resources.getColor(R.color.white))
        btn_login.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.bg_stroke_primary))
        btn_login.setTextColor(resources.getColor(R.color.colorPrimary))
        ll_login.visibility = View.GONE
        ll_register.visibility = View.VISIBLE
    }

    private fun viewLogin() {
        btn_login.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.bg_primary))
        btn_login.setTextColor(resources.getColor(R.color.white))
        btn_register.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.bg_stroke_primary))
        btn_register.setTextColor(resources.getColor(R.color.colorPrimary))
        ll_login.visibility = View.VISIBLE
        ll_register.visibility = View.GONE
    }

    private fun isValidRegister(): Boolean {
        if (fieldNama.text.toString().equals("")){
            fieldNama.setError("Wajib diisi")
            return false
        } else if (fieldNomorHpRegister.text.toString().equals("")){
            fieldNomorHpRegister.setError("Wajib diisi")
            return false
        } else if (fieldPasswordRegister.text.toString().equals("")){
            fieldPasswordRegister.setError("Wajib diisi")
            return false
        } else if (fieldKonfirmasiPassword.text.toString().equals("")){
            fieldKonfirmasiPassword.setError("Wajib diisi")
            return false
        }
        if (!fieldKonfirmasiPassword.text.toString().equals("") && !fieldPasswordRegister.text.toString().equals("")){
            if (!fieldKonfirmasiPassword.text.toString().equals(fieldPasswordRegister.text.toString())){
                fieldKonfirmasiPassword.setError("Konfirmasi password salah")
                return false
            }
        }
        return true
    }

    private fun register() {
        val payload = Payload()
        payload.payloads.add(PayloadEntry("name",fieldNama.text.toString()))
        payload.payloads.add(PayloadEntry("phone_number",fieldNomorHpRegister.text.toString()))
        payload.payloads.add(PayloadEntry("password",fieldPasswordRegister.text.toString()))
        if (currentChar.equals("mom")){
            payload.url = Urls.registerMother
        } else{
            payload.url = Urls.registerMidwife
        }
        viewModel.register(payload)
                .observe(this, Observer {result ->
                    when(result.status){
                        Result.Status.LOADING->{
                            startLoading()
                        }
                        Result.Status.SUCCESS->{
                            finishLoading()
                            createDialog("Pendaftaran berhasil",{
                                navigator.verifikasi(this,fieldNomorHpRegister.text.toString())
                            })
                        }
                        Result.Status.ERROR->{
                            finishLoading()
                            Fun.handleError(this,result)
                        }
                    }
                })
    }

    private fun login() {
        val payload = Payload()
        payload.payloads.add(PayloadEntry("phone_number",fieldNomorHp.text.toString()))
        payload.payloads.add(PayloadEntry("password",fieldPassword.text.toString()))
        viewModel.login(payload)
                .observe(this, Observer {result ->
                    when(result.status){
                        Result.Status.LOADING->{
                            startLoading()
                        }
                        Result.Status.SUCCESS->{
                            finishLoading()
                            val user = result.data!!
                            var dataJson = gson.fromJson(getString(R.string.testing_user),User::class.java)
                            userPref.setUser(user)
                            println("data user : ${userPref.getUser()}")
                            navigator.mainMom(this,true)
                        }
                        Result.Status.ERROR->{
                            finishLoading()
                            Fun.handleError(this,result)
                        }
                    }
                })
    }

    private fun isValidLogin(): Boolean {
        if (fieldNomorHp.text.toString().equals("")){
            fieldNomorHp.setError("Wajib diisi")
            return false
        } else if (fieldPassword.text.toString().equals("")){
            fieldPassword.setError("Wajib diisi")
            return false
        }
        return true
    }

    private fun changeImage() {
        if (currentChar.equals("mom")){
            Glide.with(this).applyDefaultRequestOptions(requestOptions).load(getDrawable(R.drawable.bumil_on)).into(iv_mom)
            Glide.with(this).applyDefaultRequestOptions(requestOptions).load(getDrawable(R.drawable.bidan_off)).into(iv_midwife)
        } else{
            Glide.with(this).applyDefaultRequestOptions(requestOptions).load(getDrawable(R.drawable.bumil_off)).into(iv_mom)
            Glide.with(this).applyDefaultRequestOptions(requestOptions).load(getDrawable(R.drawable.bidan_on)).into(iv_midwife)
        }
    }
}