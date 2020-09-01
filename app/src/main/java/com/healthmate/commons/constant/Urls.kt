package com.healthmate.common.constant

import androidx.databinding.library.BuildConfig

object Urls {
    val SERVER_DEV = "http://192.168.0.3:80/"
    val SERVER_PROD = "http://128.199.237.120:20017/"
    val SERVER_API = "api/v1"

    fun getServer(): String{
        if (Var.isProd) return SERVER_DEV
        else return SERVER_PROD
    }

    //AUTH
    val signin = SERVER_API + "/autentikasi/signin"
    val signout = SERVER_API + "/autentikasi/signout"
    val signup = SERVER_API + "/autentikasi/signup"
    val confirmation_send = SERVER_API + "/confirmation/send"
    val confirmation_verify = SERVER_API + "/confirmation/verify"

    //SURATJALAN
    val suratjalan_create = SERVER_API + "/surat-jalan/create"
    val suratjalan_update = SERVER_API + "/surat-jalan/update"
    val suratjalan_delete = SERVER_API + "/surat-jalan/delete"
    val suratjalan_list = SERVER_API + "/surat-jalan/list"
    val suratjalan_detail = SERVER_API + "/surat-jalan/detail"
    val suratjalan_master_jenistruk = SERVER_API + "/surat-jalan/master/jenis-truk"
    val suratjalan_master_jenistarif = SERVER_API + "/surat-jalan/master/jenis-tarif"
    val suratjalan_master_jeniscontainer = SERVER_API + "/surat-jalan/master/jenis-container"
    val suratjalan_master_armada = SERVER_API + "/surat-jalan/master/armada"

}