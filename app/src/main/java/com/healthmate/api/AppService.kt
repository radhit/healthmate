package com.healthmate.api

import com.healthmate.menu.mom.home.data.CheckUpModel
import com.healthmate.menu.mom.rapor.data.RaporModel
import com.healthmate.menu.reusable.data.User
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface AppService {
    @POST
    suspend fun login(@Url url: String, @Body requestBody: RequestBody): Response<DataResponse<User>>

    @POST
    suspend fun register(@Url url: String, @Body requestBody: RequestBody): Response<DataResponse<Any>>

    @GET
    suspend fun statusCheckup(@Url url: String): Response<DataResponse<List<CheckUpModel>>>

    @PUT
    suspend fun updateDataMom(@Url url: String, @Body requestBody: RequestBody): Response<DataResponse<Any>>

    @GET
    suspend fun getAncs(@Url url: String): Response<DataResponse<List<RaporModel>>>
}