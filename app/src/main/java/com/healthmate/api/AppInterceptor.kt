package com.healthmate.api

import android.content.Context
import android.content.Intent
import com.artcak.starter.common.sharedpreferences.UserPref
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import org.json.JSONObject
import javax.inject.Inject


class AppInterceptor @Inject constructor(
    private val userPref: UserPref
) : Interceptor {
    val gson: Gson

    init {
        gson = GsonBuilder().serializeNulls().create()
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(chain.request()
            .newBuilder()
            .addHeader("Authorization","${userPref.getUser().access.token_type} ${userPref.getUser().access.access_token}")
            .addHeader("latitude",userPref.getUser().latitude.toString())
            .addHeader("longitude",userPref.getUser().longitude.toString())
            .addHeader("device","android")
            .addHeader("Content-Type","application/json")
            .addHeader("Accept","application/json")
            .build()
        )
    }

}