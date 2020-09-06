package com.healthmate.common.viewmodel

import android.app.Activity
import android.content.Intent
import com.afollestad.materialdialogs.MaterialDialog
import com.healthmate.common.utils.replaceEmpty
import java.util.*

data class Resource<out T>(val status: Status, val data: T?, val message: String = "", val responseCode: Int)  {
    companion object {
        fun <T> success(data: T?,message: String = ""): Resource<T> {
            return Resource(Status.SUCCESS, data, message,200)
        }

        fun <T> error(msg: String?, responseCode: Int = 0): Resource<T> {
            return Resource(Status.ERROR, null, msg.replaceEmpty("-"),responseCode)
        }

        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(Status.LOADING, data, "",0)
        }
    }

    fun handleError(activity: Activity,closeActivity: Boolean = false){
        if (!message.toLowerCase(Locale.getDefault()).contains("failed to connect")){
            val dialog = MaterialDialog(activity).title(null,"Info [${responseCode}]")
                .message(null, message)
                .positiveButton(null,"OK",{
//                    if (responseCode==401){
//                        val dataManager = UserSession(activity)
//                        dataManager.clear()
//                        activity.startActivity(Intent(activity, SigninActivity::class.java))
//                        activity.finish()
//                    }else{
//                        if (closeActivity) activity.finish()
//                    }
                    it.dismiss()
                }).noAutoDismiss().cancelable(false)
            dialog.show()
        }
    }
}