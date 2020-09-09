package com.healthmate.menu.reusable.data

import com.healthmate.api.AppService
import com.healthmate.api.BaseDataSource
import com.healthmate.api.Payload
import javax.inject.Inject

class MasterRemoteDataSource @Inject constructor(private val appService: AppService): BaseDataSource(){
    suspend fun getLocation(payload: Payload) = getResult {
        appService.getLocation(payload.url)
    }

    suspend fun getHospital(payload: Payload) = getResult {
        appService.getHospital(payload.url)
    }
}
