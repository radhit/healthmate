package com.healthmate.menu.reusable.data

import com.google.gson.annotations.SerializedName

data class User(

    @field:SerializedName("address")
    val address: String = "",

    @field:SerializedName("is_email_verified")
    val isEmailVerified: Int = 0,

    @field:SerializedName("built_in")
    val builtIn: Int = 0,

    @field:SerializedName("photo")
    val photo: String? = null,

    @field:SerializedName("active")
    val active: Int = 0,

    @field:SerializedName("created_at")
    val createdAt: String = "",

    @field:SerializedName("token")
    val token: String = "",

    @field:SerializedName("name")
    val name: String = "",

    @field:SerializedName("updated_at")
    val updatedAt: String = "",

    @field:SerializedName("is_phone_number_verified")
    val isPhoneNumberVerified: Int = 0,

    @field:SerializedName("phone")
    val phone: String = "",

    @field:SerializedName("id")
    val id: Int = 0,

    @field:SerializedName("email")
    val email: String = "",

    @field:SerializedName("username")
    val username: String = "",

    @field:SerializedName("gambar")
    val gambar: Gambar = Gambar(),

    @field:SerializedName("access")
    val access: UserAccess = UserAccess(),

    @field:SerializedName("latitude")
    var latitude: Double = 0.0,

    @field:SerializedName("longitude")
    var longitude: Double = 0.0
)