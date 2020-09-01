package com.healthmate.api

import okhttp3.MultipartBody
import okhttp3.RequestBody

data class Payload(
    val payloads: ArrayList<PayloadEntry> = ArrayList(),
    val payloadsMultipart: ArrayList<PayloadEntryMultipart> = ArrayList(),
    var url: String = "",
    var method: String = ""
){
    fun getRequestBody(): RequestBody {
        val formBodyMultipart = MultipartBody.Builder().setType(MultipartBody.FORM)
        for (payload in payloads){
            formBodyMultipart.addFormDataPart(payload.key,payload.value)
        }
        for (payload in payloadsMultipart){
            formBodyMultipart.addFormDataPart(payload.name,payload.filename,payload.body)
        }
        if (payloads.size == 0 && payloadsMultipart.size == 0){
            formBodyMultipart.addFormDataPart("a","a")
        }
        return formBodyMultipart.build()
    }
}

data class PayloadEntry(
    val key: String = "",
    val value: String = ""
)

data class PayloadEntryMultipart(
    var name: String = "",
    var filename: String = "",
    var body: RequestBody
)