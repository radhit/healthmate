package com.healthmate.common.navigation

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import com.healthmate.common.template.TemplateActivity.Companion.getCallingIntent
import com.healthmate.menu.auth.view.SigninActivity
import com.healthmate.menu.auth.view.ValidasiActivity
import com.healthmate.menu.mom.checkup.view.CheckupActivity
import com.healthmate.menu.mom.covid.view.ScreeningCovidActivity
import com.healthmate.menu.mom.kia.view.MainKiaActivity
import com.healthmate.menu.mom.main.MainMomActivity
import com.healthmate.menu.mom.rapor.view.MainRaporActivity
import com.healthmate.menu.reusable.view.ListLocationActivity
import com.healthmate.menu.reusable.view.MasterListActivity
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

    fun verifikasi(activity: Activity, data: String){
        goto(activity,ValidasiActivity.getCallingIntent(activity,data))
    }

    fun mainMom(activity: Activity,clearTop:Boolean = false){
        val intent = MainMomActivity.getCallingIntent(activity)
        if (clearTop) intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        activity.startActivity(intent)
    }

    fun screeningCovid(activity: Activity){
        goto(activity, ScreeningCovidActivity.getCallingIntent(activity))
    }

    fun dataKiaMom(activity: Activity){
        goto(activity, MainKiaActivity.getCallingIntent(activity))
    }

    fun checkUp(activity: Activity){
        goto(activity,CheckupActivity.getCallingIntent(activity))
    }

    fun dataMaster(activity: Activity, keterangan:String, response_code: Int){
        gotoForResult(activity,MasterListActivity.getCallingIntent(activity,keterangan),response_code)
    }

    fun listLocation(activity: Activity, keterangan:String, response_code: Int){
        gotoForResult(activity,ListLocationActivity.getCallingIntent(activity,keterangan),response_code)
    }

    fun rapor(activity: Activity){
        goto(activity, MainRaporActivity.getCallingIntent(activity))
    }

}