package com.healthmate.common.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import javax.inject.Inject

class AppPref @Inject constructor(context: Context){
    private val fileName = "APP_PREF"
    val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    private val appContext = context.applicationContext

    private val DEBUG_MODE = "DEBUG_MODE"
    private val SERVER = "SERVER"
    private val NEED_INTRO = "NEED_INTRO"

    protected val appPref = EncryptedSharedPreferences
        .create(
            fileName,
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

    fun setDebugMode(value: Boolean){
        appPref.edit().putBoolean(DEBUG_MODE,value).apply()
    }

    fun isDebugMode(): Boolean{
        return appPref.getBoolean(DEBUG_MODE,false)
    }

    fun setServer(value: String){
        appPref.edit().putString(SERVER,value).apply()
    }

    fun getServer(): String{
        return appPref.getString(SERVER,"")!!
    }

    fun setNeedIntro(value: Boolean){
        appPref.edit().putBoolean(NEED_INTRO,value).apply()
    }

    fun isNeedIntro(): Boolean{
        return appPref.getBoolean(NEED_INTRO,true)
    }
}