package com.healthmate.menu.reusable.data

import com.google.gson.annotations.SerializedName

data class Gambar(

    @field:SerializedName("url")
    val url: String = "",

    @field:SerializedName("mimetype")
    val mimetype: String = "",

    @field:SerializedName("size")
    val size: String = ""
)