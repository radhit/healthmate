package com.healthmate.common.constant

import androidx.databinding.library.BuildConfig

object Urls {
    val SERVER_DEV = "http://128.199.227.212:3000"
    val SERVER_PROD = "https://api.healthmate.web.id"
    val SERVER_API = "api/v1"

    fun getServer(): String{
        return SERVER_PROD
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
    val ancsMom = "/ancs"

    //MIDWIFE
    val ancsHistory = "/anc-histories"
    val kala = "/kala"
    val inc = "/incs"
    val pnc = "/pncs"
    val rujukan = "/references"


}