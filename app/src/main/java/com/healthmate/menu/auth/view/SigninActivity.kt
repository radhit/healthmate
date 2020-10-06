package com.healthmate.menu.auth.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
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
    var currentChar: String = "mother"
    var currentPage: String = "login"

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        iv_mom.setOnClickListener {
            inputStr.visibility = View.GONE
            currentChar = "mother"
            changeImage()
        }
        iv_midwife.setOnClickListener {
            currentChar = "midwife"
            if (currentPage.equals("register")){
                inputStr.visibility = View.VISIBLE
            } else{
                inputStr.visibility = View.GONE
            }
            changeImage()
        }
        btn_login.setOnClickListener {
            currentPage = "login"
            viewLogin()
        }
        btn_register.setOnClickListener {
            currentPage = "register"
            viewRegister()
        }

        btn_signin.setOnClickListener {
            //0811234567890, c2VjcmV0 midwife
            //085789456123, c2VjcmV0  midwife okezone
            //089789456123, c2VjcmV0 mother
            //081567891234, c2VjcmV0 mother
            if (isValidLogin()){
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
        if (currentChar.equals("midwife")){
            inputStr.visibility = View.VISIBLE
        } else{
            inputStr.visibility = View.GONE
        }
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
        if (currentChar.equals("midwife")){
            payload.payloads.add(PayloadEntry("str_number",fieldNomorStr.text.toString()))
        }
        payload.payloads.add(PayloadEntry("name",fieldNama.text.toString()))
        payload.payloads.add(PayloadEntry("phone_number",fieldNomorHpRegister.text.toString()))
        payload.payloads.add(PayloadEntry("password", Base64.getEncoder().encodeToString(fieldPasswordRegister.text.toString().toByteArray())))
        if (currentChar.equals("mother")){
            payload.url = Urls.registerMother
        } else{
            payload.url = Urls.registerMidwife
        }
        viewModel.register(payload)
                .observe(this, Observer {result ->
                    when(result.status){
                        Result.Status.LOADING->{
                            startLoading("register")
                        }
                        Result.Status.SUCCESS->{
                            var user = result.data!!
                            navigator.verifikasi(this, gson.toJson(user),currentChar)
                            finish()
                        }
                        Result.Status.ERROR->{
                            finishLoading("register")
                            createDialog(result.message!!)
                        }
                    }
                })
    }

    private fun login() {
        val payload = Payload()
        payload.payloads.add(PayloadEntry("phone_number",fieldNomorHp.text.toString()))
        payload.payloads.add(PayloadEntry("password", Base64.getEncoder().encodeToString(fieldPassword.text.toString().toByteArray())))
        viewModel.login(payload)
                .observe(this, Observer {result ->
                    when(result.status){
                        Result.Status.LOADING->{
                            startLoading("login")
                        }
                        Result.Status.SUCCESS->{
                            finishLoading("login")
                            val user = result.data!!
                            user.password = Base64.getEncoder().encodeToString(fieldPassword.text.toString().toByteArray())
                            userPref.setUser(user)
                            println("data user : ${userPref.getUser()}")
                            if (userPref.getUser().type.equals("midwife")){
                                navigator.mainMidwive(this,true)
                            } else{
                                navigator.mainMom(this,true)
                            }
                        }
                        Result.Status.ERROR->{
                            finishLoading("login")
                            createDialog(result.message!!)
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
        if (currentChar.equals("mother")){
            Glide.with(this).applyDefaultRequestOptions(requestOptions).load(getDrawable(R.drawable.bumil_on)).into(iv_mom)
            Glide.with(this).applyDefaultRequestOptions(requestOptions).load(getDrawable(R.drawable.bidan_off)).into(iv_midwife)
        } else{
            Glide.with(this).applyDefaultRequestOptions(requestOptions).load(getDrawable(R.drawable.bumil_off)).into(iv_mom)
            Glide.with(this).applyDefaultRequestOptions(requestOptions).load(getDrawable(R.drawable.bidan_on)).into(iv_midwife)
        }
    }

    fun startLoading(keterangan: String){
        if (keterangan.equals("login")){
            btn_signin.isEnabled = false
            btn_signin.text = "Mohon Tunggu..."
        } else{
            btn_signup.isEnabled = false
            btn_signup.text = "Mohon Tunggu..."
        }
    }

    fun finishLoading(keterangan: String){
        if (keterangan.equals("login")){
            btn_signin.isEnabled = true
            btn_signin.text = "Masuk"
        } else{
            btn_signup.isEnabled = true
            btn_signup.text = "Daftar"
        }
    }
}