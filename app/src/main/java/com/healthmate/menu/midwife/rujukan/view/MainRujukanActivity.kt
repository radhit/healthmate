package com.healthmate.menu.midwife.rujukan.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.healthmate.R
import com.healthmate.common.base.BaseActivity
import com.healthmate.commons.adapter.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_main_rujukan.*

class MainRujukanActivity : BaseActivity() {
    companion object {
        val EXTRA = "EXTRA"
        @JvmStatic
        fun getCallingIntent(activity: Activity): Intent {
            val intent = Intent(activity, MainRujukanActivity::class.java)
            return intent
        }
    }
    override fun getView(): Int = R.layout.activity_main_rujukan

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
        setData()
    }

    private fun setData() {
        tvTitle.text = "Pasien"
        Glide.with(this).applyDefaultRequestOptions(requestOptionsMidwife).load(userPref.getUser().profile_picture).into(iv_foto)
        tv_nama.text = userPref.getUser().name
        tv_status.text = "No. STR ${userPref.getUser().str_number}"
    }

    private fun setupViewPager(){
        viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        viewPagerAdapter.addFragment(RujukanFragment.newInstance("penerimaan"),"Penerimaan")
        viewPagerAdapter.addFragment(RujukanFragment.newInstance("pengiriman"),"Pengiriman")
        viewPager.adapter = viewPagerAdapter
    }
}