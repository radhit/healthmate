package com.healthmate.api

import com.healthmate.menu.mom.home.data.CheckUpModel
import com.healthmate.menu.mom.rapor.data.RaporModel
import com.healthmate.menu.reusable.data.User
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface BaseApi {
    @PUT
    fun updateDataMom(@Url url: String, @Body requestBody: RequestBody): Call<DataResponse<Any>>
}