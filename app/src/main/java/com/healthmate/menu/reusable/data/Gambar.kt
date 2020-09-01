package com.healthmate.menu.reusable.data

import com.google.gson.annotations.SerializedName

data class Gambar(

    @field:SerializedName("path")
    val path: String = "",

    @field:SerializedName("updated_at")
    val updatedAt: String = "",

    @field:SerializedName("created_at")
    val createdAt: String = "",

    @field:SerializedName("id")
    val id: Int = 0
)