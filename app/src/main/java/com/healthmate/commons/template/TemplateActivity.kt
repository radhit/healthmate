package com.healthmate.common.template

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.healthmate.R
import com.healthmate.common.base.BaseActivity

class TemplateActivity : BaseActivity() {

    companion object {
        @JvmStatic
        fun getCallingIntent(activity: Activity): Intent {
            val intent = Intent(activity, TemplateActivity::class.java)
            return intent
        }
    }

    override fun getView(): Int = R.layout.activity_signin

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        this.setTitle("-")
    }
}