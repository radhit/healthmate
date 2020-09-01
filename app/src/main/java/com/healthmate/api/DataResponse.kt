package com.healthmate.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DataResponse<T> {

    @SerializedName(value = "data")
    @Expose
    var data: T? = null

    @SerializedName(value = "response_code")
    @Expose
    var responseCode: Int = 0

    @SerializedName(value = "message")
    @Expose
    var message: String = ""

    @SerializedName(value = "errors")
    @Expose
    var errors: List<String> = listOf()
}