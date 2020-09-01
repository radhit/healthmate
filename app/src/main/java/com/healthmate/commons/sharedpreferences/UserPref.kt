package com.healthmate.common.sharedpreferences

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.healthmate.api.Payload
import com.healthmate.api.PayloadEntry
import com.artcak.starter.modules.reusable.data.User
import com.google.gson.Gson
import okhttp3.RequestBody
import javax.inject.Inject

class UserPref @Inject constructor(context: Context){
    private val fileName = "USER_PREF"
    val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    private val appContext = context.applicationContext

    private val USER_DATA = "USER_DATA"

    protected val userPref = EncryptedSharedPreferences
        .create(
            fileName,
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

    fun setUser(value: User){
        userPref.edit().putString(USER_DATA,Gson().toJson(value)).apply()
    }

    fun getUser(): User{
        return Gson().fromJson(userPref.getString(USER_DATA,Gson().toJson(User()))!!,User::class.java)
    }

    fun getTokenRequestBody(): RequestBody {
        val payload = Payload(ArrayList(listOf(
                PayloadEntry("token", getUser().token.toString())
            )))
        return payload.getRequestBody()
    }

    fun addPayloadEntry(payloadEntries: ArrayList<PayloadEntry>): RequestBody {
        val payload = Payload(ArrayList(listOf(
                PayloadEntry("token", getUser().token.toString())
            )))
        payload.payloads.addAll(payloadEntries)
        return payload.getRequestBody()
    }
}