package com.healthmate.common.navigation

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import com.healthmate.common.template.TemplateActivity.Companion.getCallingIntent
import com.healthmate.menu.auth.view.SigninActivity
import com.healthmate.menu.mom.main.MainMomActivity
import javax.inject.Inject

class Navigator @Inject constructor() {

    fun goto(activity: Activity, intent: Intent) {
        try {
            activity.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
        }
    }

    fun gotoForResult(activity: Activity, intent: Intent, requestCode: Int) {
        try {
            activity.startActivityForResult(intent, requestCode)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
        }
    }

    fun signin(activity: Activity,clearTop:Boolean = false, keterangan: String = "awal"){
        val intent = SigninActivity.getCallingIntent(activity, keterangan)
        if (clearTop) intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        activity.startActivity(intent)
    }

//
//    fun signup(activity: Activity){
//        goto(activity, SignupActivity.getCallingIntent(activity))
//    }
//
    fun mainMom(activity: Activity,clearTop:Boolean = false){
        val intent = MainMomActivity.getCallingIntent(activity)
        if (clearTop) intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        activity.startActivity(intent)
    }
}