package com.healthmate.common.constant

import androidx.databinding.library.BuildConfig

object Urls {
    val SERVER_DEV = "https://7a1eed560266.ngrok.io"
    val SERVER_PROD = "http://128.199.237.120:20017"
    val SERVER_API = "api/v1"

    fun getServer(): String{
        return SERVER_DEV
    }

    //AUTH
    val login = "/login"
    val registerMother = "/mothers"
    val registerMidwife = "/midwives"


}