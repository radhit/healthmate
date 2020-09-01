package com.healthmate.common.constant

object Key {
    val enc_key = "V7P0uMAyk1BfHLGOoHuAOzNxczwx3Wy3"
    val client_id_dev = "2"
    val client_secret_dev = "oFDKeizRImGTtiTPtMia4dK4GOWUJkOOjPjKWwXY"
    val client_id_prod = "2"
    val client_secret_prod = "QvzX1sfNGoWhXEYiTg6SSDQrG56uZLaTAgesyTYR"

    fun getClientId(): String{
        if (Var.isProd) return client_id_prod
        return client_id_dev
    }

    fun getClientSecret(): String{
        if (Var.isProd) return client_secret_prod
        return client_secret_dev
    }
}