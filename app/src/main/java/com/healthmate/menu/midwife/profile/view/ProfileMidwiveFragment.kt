package com.healthmate.menu.midwife.profile.view

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.healthmate.BuildConfig
import com.healthmate.R
import com.healthmate.common.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_profile.*
import java.util.*

class ProfileMidwiveFragment : BaseFragment() {
    lateinit var materialDialog: MaterialDialog

    override fun getViewId(): Int = R.layout.fragment_profile_midwive

    companion object {
        fun newInstance(): ProfileMidwiveFragment = ProfileMidwiveFragment()
    }

    override fun onFragmentCreated(savedInstanceState: Bundle?) {
        tv_versi.text = "v.${BuildConfig.VERSION_NAME}"
        tv_name.text = user.name
        tv_phone_number.text = user.phone_number
        btn_logout.setOnClickListener {
            signout()
        }
        btn_ubah.setOnClickListener {
            navigator.dataKiaMom(activity!!)
        }
    }
}