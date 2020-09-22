package com.healthmate.api

import com.healthmate.menu.midwife.pasien.data.*
import com.healthmate.menu.mom.home.data.CheckUpModel
import com.healthmate.menu.mom.rapor.data.AncModel
import com.healthmate.menu.reusable.data.Gambar
import com.healthmate.menu.reusable.data.Hospital
import com.healthmate.menu.reusable.data.Location
import com.healthmate.menu.reusable.data.User
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface AppService {
    @POST
    suspend fun login(@Url url: String, @Body requestBody: RequestBody): Response<DataResponse<User>>

    @POST
    suspend fun register(@Url url: String, @Body requestBody: RequestBody): Response<DataResponse<User>>

    @POST
    suspend fun verifikasi(@Url url: String, @Body requestBody: RequestBody): Response<DataResponse<Any>>

    @GET
    suspend fun getDataMe(@Url url: String): Response<DataResponse<User>>

    @GET
    suspend fun statusCheckup(@Url url: String): Response<DataResponse<List<CheckUpModel>>>

    @GET
    suspend fun getAncs(@Url url: String): Response<DataResponse<List<AncModel>>>

    @GET
    suspend fun getLocation(@Url url: String): Response<DataResponse<List<Location>>>

    @GET
    suspend fun getHospital(@Url url: String): Response<DataResponse<List<Hospital>>>

    @POST
    suspend fun uploadImage(@Url url: String, @Body requestBody: RequestBody): Response<DataResponse<Gambar>>

    @GET
    suspend fun listMothers(@Url url: String): Response<DataResponse<List<User>>>

    @POST
    suspend fun postDataHistoryAncs(@Url url: String, @Body requestBody: RequestBody): Response<DataResponse<Any>>

    @POST
    suspend fun postRatingMother(@Url url: String, @Body requestBody: RequestBody): Response<DataResponse<Any>>

    @GET
    suspend fun getInc(@Url url: String): Response<DataResponse<IncModel>>

    @GET
    suspend fun getKala(@Url url: String): Response<DataResponse<List<IncKalaModel>>>

    @GET
    suspend fun getDataSummary(@Url url: String): Response<DataResponse<Summary>>

    @GET
    suspend fun getDataBabyNote(@Url url: String): Response<DataResponse<BabyNote>>

    @GET
    suspend fun getListPnc(@Url url: String): Response<DataResponse<List<PncModel>>>
}