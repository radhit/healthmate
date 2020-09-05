package com.healthmate.menu.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.healthmate.R
import com.healthmate.common.base.BaseActivity
import com.healthmate.menu.intro.view.IntroActivity
import java.util.*

class SplashActivity : BaseActivity() {
    override fun getView(): Int = R.layout.activity_splash

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Handler().postDelayed({
            if (appPref.isNeedIntro()){
                val intent = Intent(this, IntroActivity::class.java)
                startActivity(intent)
            } else{
                if (userPref.isLoggedin){
                    if (userPref.getUser().type.equals("mother")){
                        navigator.mainMom(this,true)
                    }
                } else{
                    navigator.signin(this, true)
                }
            }
        }, 3000)
    }
}