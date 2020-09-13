package com.healthmate.common.constant

import androidx.databinding.library.BuildConfig

object Urls {
    val SERVER_DEV = "http://128.199.227.212:3000"
    val SERVER_PROD = "http://128.199.237.120:20017"
    val SERVER_API = "api/v1"

    fun getServer(): String{
        return SERVER_DEV
    }

    //AUTH
    val login = "/login"
    val registerMother = "/mothers"
    val registerMidwife = "/midwives"
    val verifikasi = "/otp"

    //MASTER
    val location = "/locations"
    val hospital = "/hospitals"
    val upload = "/upload"

    //MOTHER
    val action = "/actions"
    val covid = ""
    val ancsMom = "/ancs"

    //MIDWIFE
    val pasien = "/pasien"


}