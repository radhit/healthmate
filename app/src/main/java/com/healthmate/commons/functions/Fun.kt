package com.healthmate.common.functions

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Base64
import com.healthmate.common.constant.Key
import java.security.KeyFactory
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import android.R.id.message
import android.R.attr.password
import android.app.Activity
import android.content.Intent
import com.afollestad.materialdialogs.MaterialDialog
import com.healthmate.common.sharedpreferences.UserPref
import com.healthmate.menu.reusable.data.User
import com.scottyab.aescrypt.AESCrypt
import java.security.GeneralSecurityException
import java.util.*
import com.healthmate.api.Result


object Fun{
    fun isConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }

    fun encrypt(value: String, key: String): String {
        var result = value
        try {
            result = AESCrypt.encrypt(key, value)
        } catch (e: GeneralSecurityException) {
            e.printStackTrace()
        }
        return result
    }

    fun decrypt(value: String, key: String): String {
        var result = value
        try {
            result = AESCrypt.decrypt(key, value)
        } catch (e: GeneralSecurityException) {
            e.printStackTrace()
        }
        return result
    }

    fun handleError(activity: Activity, response: Result<Any?>, showDialog: Boolean = true, isNeedClose: Boolean = false){
        if ((response.status == Result.Status.ERROR && showDialog)){
            val dialog = MaterialDialog(activity).title(null,"Failed")
                .message(null, response.message.replaceEmpty("Something Wrong."))
                .positiveButton(null,"OK",{
                    if (isNeedClose){
                        activity.finish()
                    }
                    it.dismiss()
                }).noAutoDismiss().cancelable(false)
            dialog.show()
        }
    }
}