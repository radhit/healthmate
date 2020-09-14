package com.healthmate.menu.midwife.pasien.view

import android.os.Bundle
import com.healthmate.R
import com.healthmate.common.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_create_pasien.*

class CreatePasienFragment : BaseFragment() {

    override fun getViewId(): Int = R.layout.fragment_create_pasien

    companion object {
        fun newInstance(): CreatePasienFragment = CreatePasienFragment()
    }

    override fun onFragmentCreated(savedInstanceState: Bundle?) {
        btn_register.setOnClickListener {
            navigator.dataKiaMom(activity!!,"midwife_create")
        }
    }
}