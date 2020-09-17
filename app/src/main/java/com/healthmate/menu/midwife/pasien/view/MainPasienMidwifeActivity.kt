package com.healthmate.menu.midwife.pasien.view

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.healthmate.R
import com.healthmate.common.base.BaseActivity
import com.healthmate.common.utils.replaceEmpty
import com.healthmate.commons.adapter.ViewPagerAdapter
import com.healthmate.menu.auth.view.SigninActivity
import com.healthmate.menu.mom.rapor.view.GrafikBbFragment
import com.healthmate.menu.mom.rapor.view.ListRaporFragment
import kotlinx.android.synthetic.main.activity_main_pasien_midwife.*

class MainPasienMidwifeActivity : BaseActivity() {
    companion object {
        val EXTRA = "EXTRA"
        @JvmStatic
        fun getCallingIntent(activity: Activity): Intent {
            val intent = Intent(activity, MainPasienMidwifeActivity::class.java)
            return intent
        }
    }

    override fun getView(): Int = R.layout.activity_main_pasien_midwife
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
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
        Glide.with(this).applyDefaultRequestOptions(requestOptionsMidwife).load(userPref.getUser().profile_picture).into(iv_foto)
        tv_nama.text = userPref.getUser().name
        tv_status.text = "Nomor STR : ${userPref.getUser().str_number}"
    }

    private fun setupViewPager(){
        viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        viewPagerAdapter.addFragment(ListPasienHistoryFragment.newInstance(),"Daftar Pasien")
        viewPagerAdapter.addFragment(SearchPasienFragment.newInstance(),"Cari Pasien")
        viewPagerAdapter.addFragment(CreatePasienFragment.newInstance(),"Registrasi Baru")
        viewPager.adapter = viewPagerAdapter
    }
}