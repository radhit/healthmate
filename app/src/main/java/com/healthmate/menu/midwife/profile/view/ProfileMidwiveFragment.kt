package com.healthmate.menu.midwife.profile.view

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.healthmate.BuildConfig
import com.healthmate.R
import com.healthmate.common.base.BaseFragment
import com.healthmate.common.utils.replaceEmpty
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.btn_logout
import kotlinx.android.synthetic.main.fragment_profile.btn_ubah
import kotlinx.android.synthetic.main.fragment_profile.tv_name
import kotlinx.android.synthetic.main.fragment_profile.tv_phone_number
import kotlinx.android.synthetic.main.fragment_profile.tv_versi
import kotlinx.android.synthetic.main.fragment_profile_midwive.*
import java.util.*

class ProfileMidwiveFragment : BaseFragment() {
    lateinit var materialDialog: MaterialDialog

    override fun getViewId(): Int = R.layout.fragment_profile_midwive

    companion object {
        fun newInstance(): ProfileMidwiveFragment = ProfileMidwiveFragment()
    }

    override fun onFragmentCreated(savedInstanceState: Bundle?) {
        tv_versi.text = "v.${BuildConfig.VERSION_NAME}"
        tv_name.text = userPref.getUser().name
        tv_phone_number.text = userPref.getUser().phone_number
        tv_nomor_str.text = userPref.getUser().str_number
        tv_tempat_praktik.text = userPref.getUser().hospital!!.name.replaceEmpty("-")
        btn_logout.setOnClickListener {
            signout()
        }
        btn_ubah.setOnClickListener {
            navigator.ubahProfileMidwife(activity!!)
        }
    }
}