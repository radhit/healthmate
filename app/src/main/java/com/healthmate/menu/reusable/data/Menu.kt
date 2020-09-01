package com.healthmate.menu.reusable.data

import com.google.gson.annotations.SerializedName

data class Menu(
    @SerializedName("nama")
    var nama: String = "",

    @SerializedName("icon_drawable")
    var icon_drawable: Int = 0,

    @SerializedName("icon_url")
    var icon_url: String = "",

    @SerializedName("deskripsi")
    var deskripsi: String = ""
)