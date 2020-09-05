package com.healthmate.menu.mom.profile.view

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.healthmate.R
import com.healthmate.common.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_profile.*
import java.util.*

class ProfileFragment : BaseFragment() {
    lateinit var materialDialog: MaterialDialog

    override fun getViewId(): Int = R.layout.fragment_profile

    companion object {
        fun newInstance(): ProfileFragment = ProfileFragment()
    }

    override fun onFragmentCreated(savedInstanceState: Bundle?) {

    }
}