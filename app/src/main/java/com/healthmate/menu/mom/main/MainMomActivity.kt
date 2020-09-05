package com.healthmate.menu.mom.main

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.healthmate.BuildConfig
import com.healthmate.R
import com.healthmate.common.base.BaseActivity
import com.healthmate.menu.mom.home.view.BerandaFragment
import com.healthmate.menu.mom.profile.view.ProfileFragment
import com.healthmate.menu.mom.schedule.view.JadwalFragment
import kotlinx.android.synthetic.main.activity_main_mom.*

class MainMomActivity : BaseActivity() {
    companion object {
        @JvmStatic
        fun getCallingIntent(activity: Activity): Intent {
            return Intent(activity, MainMomActivity::class.java)
        }
    }

    override fun getView(): Int = R.layout.activity_main_mom

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        setBottomNavigation()
        tv_versi.text = "v.${BuildConfig.VERSION_NAME}"
    }

    private fun setBottomNavigation(){
        openFragment(BerandaFragment.newInstance())
        bottom_navigation.setOnNavigationItemSelectedListener {
            item ->
            when (item.itemId) {
                R.id.action_home -> {
                    println("masuk home")
                    openFragment(BerandaFragment.newInstance())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.action_schedule -> {
                    println("masuk history")
                    openFragment(JadwalFragment.newInstance())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.action_profile -> {
                    openFragment(ProfileFragment.newInstance())
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onBackPressed() {
//        val homeIntent = Intent(Intent.ACTION_MAIN)
//        homeIntent.addCategory(Intent.CATEGORY_HOME)
//        startActivity(homeIntent)
        finish()
    }
}