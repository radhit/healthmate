package com.artcak.starter.common.template

import android.os.Bundle
import com.healthmate.R
import com.healthmate.common.base.BaseFragment

class TemplateFragment : BaseFragment() {

    override fun getViewId(): Int = R.layout.appintro_fragment_intro

    companion object {
        fun newInstance(): TemplateFragment = TemplateFragment()
    }

    override fun onFragmentCreated(savedInstanceState: Bundle?) {
    }
}