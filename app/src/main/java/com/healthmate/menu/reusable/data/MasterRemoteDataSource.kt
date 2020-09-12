package com.healthmate.menu.reusable.data

import com.healthmate.api.AppService
import com.healthmate.api.BaseDataSource
import com.healthmate.api.Payload
import com.healthmate.common.constant.Urls
import javax.inject.Inject

class MasterRemoteDataSource @Inject constructor(private val appService: AppService): BaseDataSource(){
    suspend fun getLocation(payload: Payload) = getResult {
        appService.getLocation(payload.url)
    }

    suspend fun getHospital(payload: Payload) = getResult {
        appService.getHospital(payload.url)
    }

    suspend fun uploadFoto(payload: Payload) = getResult {
        appService.uploadImage(Urls.upload, payload.getRequestBody())
    }
}
