package com.healthmate.menu.mom.rapor.view

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import com.healthmate.R
import com.healthmate.common.base.BaseActivity
import com.healthmate.commons.adapter.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_main_rapor.*

class MainRaporActivity : BaseActivity() {
    companion object {
        val EXTRA_TIPE = ""
        @JvmStatic
        fun getCallingIntent(activity: Activity): Intent {
            val intent = Intent(activity, MainRaporActivity::class.java)
            return intent
        }
    }
    override fun getView(): Int = R.layout.activity_main_rapor
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
        tvTitle.text = "Rapor Bumil"
    }

    private fun setupViewPager(){
        viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        viewPagerAdapter.addFragment(ListRaporFragment.newInstance(),"Data BB")
        viewPagerAdapter.addFragment(GrafikBbFragment.newInstance(),"Grafik BB")
        viewPager.adapter = viewPagerAdapter
    }
}