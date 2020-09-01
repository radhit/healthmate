package com.healthmate.menu.reusable.data

import com.google.gson.annotations.SerializedName

data class UserAccess(

    @field:SerializedName("token_type")
    val token_type: String = "Bearer",

    @field:SerializedName("expires_in")
    val expires_in: Int = 0,

    @field:SerializedName("access_token")
    val access_token: String? = null,

    @field:SerializedName("refresh_token")
    val refresh_token: String? = null
)