package com.healthmate.api

import com.healthmate.menu.reusable.data.User
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url

interface AppService {
    @POST
    suspend fun login(@Url url: String, @Body requestBody: RequestBody): Response<DataResponse<User>>

    @POST
    suspend fun register(@Url url: String, @Body requestBody: RequestBody): Response<DataResponse<Any>>

    @POST
    suspend fun postUser(@Url url: String, @Body requestBody: RequestBody): Response<DataResponse<User>>
}