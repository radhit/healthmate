package com.healthmate.menu.midwife.pasien.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.healthmate.R
import com.healthmate.common.base.BaseActivity
import com.healthmate.common.utils.replaceEmpty
import com.healthmate.commons.adapter.ViewPagerAdapter
import com.healthmate.menu.reusable.data.User
import kotlinx.android.synthetic.main.activity_main_detil_pasien.*

class MainDetilPasienActivity : BaseActivity() {
    companion object {
        val EXTRA = "EXTRA"
        @JvmStatic
        fun getCallingIntent(activity: Activity, data: String): Intent {
            val intent = Intent(activity, MainDetilPasienActivity::class.java)
            intent.putExtra(EXTRA, data)
            return intent
        }
    }
    override fun getView(): Int = R.layout.activity_main_detil_pasien
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    var user = User()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        user = gson.fromJson(intent.getStringExtra(EXTRA),User::class.java)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.tabMode = TabLayout.MODE_FIXED
        setupViewPager()
        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }

        })
        tvTitle.text = "Pasien"
        if (user.diagnostics_color.equals("red")){
            iv_foto.borderColor = ContextCompat.getColor(this, R.color.md_red_500)
        } else if (user.diagnostics_color.equals("yellow")){
            iv_foto.borderColor = ContextCompat.getColor(this, R.color.md_yellow_500)
        } else if (user.diagnostics_color.equals("green")){
            iv_foto.borderColor = ContextCompat.getColor(this, R.color.md_green_500)
        }
        Glide.with(this).applyDefaultRequestOptions(requestOptionsMidwife).load(user.profil_picture).into(iv_foto)
        tv_nama.text = user.name
        tv_status.text = "Status Terakhir : ${user.diagnostics_color.replaceEmpty("-")}"
    }

    private fun setupViewPager(){
        viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        viewPagerAdapter.addFragment(DetilPasienFragment.newInstance(gson.toJson(user)),"Data KIA")
        viewPagerAdapter.addFragment(DataAncFragment.newInstance(gson.toJson(user)),"Data ANC")
        viewPager.adapter = viewPagerAdapter
    }
}