package com.healthmate.menu.midwife.pasien.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.healthmate.R
import com.healthmate.api.Payload
import com.healthmate.api.Result
import com.healthmate.common.base.BaseActivity
import com.healthmate.common.constant.Urls
import com.healthmate.common.utils.replaceEmpty
import com.healthmate.commons.adapter.ViewPagerAdapter
import com.healthmate.di.injector
import com.healthmate.menu.reusable.data.MasterViewModel
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

    private val viewModel by lazy {
        ViewModelProviders.of(this, injector.masterVM()).get(MasterViewModel::class.java)
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
        setData()

    }

    private fun setData() {
        tvTitle.text = "Pasien"
        if (user.diagnostic_color.equals("red")){
            iv_foto.borderColor = ContextCompat.getColor(this, R.color.md_red_500)
        } else if (user.diagnostic_color.equals("yellow")){
            iv_foto.borderColor = ContextCompat.getColor(this, R.color.md_yellow_500)
        } else if (user.diagnostic_color.equals("green")){
            iv_foto.borderColor = ContextCompat.getColor(this, R.color.md_green_500)
        }
        Glide.with(this).applyDefaultRequestOptions(requestOptionsMom).load(user.profile_picture).into(iv_foto)
        tv_nama.text = user.name
        tv_status.text = "Status Terakhir : ${user.diagnostic_color.replaceEmpty("-")}"
    }


    private fun setupViewPager(){
        viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        viewPagerAdapter.addFragment(DetilPasienFragment.newInstance(gson.toJson(user)),"Data KIA")
        viewPagerAdapter.addFragment(DataAncFragment.newInstance(gson.toJson(user)),"Data ANC")
        viewPagerAdapter.addFragment(MainIncFragment.newInstance(gson.toJson(user)),"Data INC")
        viewPagerAdapter.addFragment(MainPncFragment.newInstance(gson.toJson(user)),"Data PNC")
        viewPager.adapter = viewPagerAdapter
    }

    internal fun getDataMother(){
        val payload = Payload()
        payload.url = "${Urls.registerMother}/${user.id}"
        viewModel.getDataMe(payload)
                .observe(this, Observer { result ->
                    when (result.status) {
                        Result.Status.LOADING -> {
                            showLoadingDialog()
                        }
                        Result.Status.SUCCESS -> {
                            closeLoadingDialog()
                            user = result.data!!
                            setData()
                        }
                        Result.Status.ERROR -> {
                            closeLoadingDialog()
                            createDialog(result.message!!)
                        }
                    }
                })
    }
}