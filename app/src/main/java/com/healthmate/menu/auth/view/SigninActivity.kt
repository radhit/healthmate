package com.healthmate.menu.auth.view

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.artcak.starter.di.App.Companion.context
import com.bumptech.glide.Glide
import com.healthmate.R
import com.healthmate.common.base.BaseActivity
import com.healthmate.menu.reusable.data.User
import kotlinx.android.synthetic.main.activity_intro.*
import kotlinx.android.synthetic.main.activity_signin.*

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
        btn_signin.setOnClickListener {
            var dataJson = gson.fromJson(getString(R.string.testing_user),User::class.java)
            userPref.setUser(dataJson)
            navigator.mainMom(this,true)
        }
        btn_login.setOnClickListener {
            btn_login.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.bg_primary))
            btn_login.setTextColor(resources.getColor(R.color.white))
            btn_register.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.bg_stroke_primary))
            btn_register.setTextColor(resources.getColor(R.color.colorPrimary))
            ll_login.visibility = View.VISIBLE
            ll_register.visibility = View.GONE
        }
        btn_register.setOnClickListener {
            btn_register.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.bg_primary))
            btn_register.setTextColor(resources.getColor(R.color.white))
            btn_login.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.bg_stroke_primary))
            btn_login.setTextColor(resources.getColor(R.color.colorPrimary))
            ll_login.visibility = View.GONE
            ll_register.visibility = View.VISIBLE
        }
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