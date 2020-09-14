package com.healthmate.api

import com.healthmate.menu.reusable.data.User
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface BaseApi {
    @PUT
    fun updateDataMom(@Url url: String, @Body requestBody: RequestBody): Call<DataResponse<Any>>

    @POST
    fun createDataMom(@Url url: String, @Body requestBody: RequestBody): Call<DataResponse<Any>>

    @PUT
    fun updateDataMidwive(@Url url: String, @Body requestBody: RequestBody): Call<DataResponse<Any>>

    @PUT
    fun updateDataAncsHistory(@Url url: String, @Body requestBody: RequestBody): Call<DataResponse<Any>>

    @POST
    fun changeCovidStatus(@Url url: String, @Body requestBody: RequestBody): Call<DataResponse<User>>
}