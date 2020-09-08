package com.healthmate.menu.mom.rapor.data

import com.healthmate.api.AppService
import com.healthmate.api.BaseDataSource
import com.healthmate.api.Payload
import javax.inject.Inject

class RaporRemoteDataSource @Inject constructor(private val appService: AppService): BaseDataSource(){
    suspend fun getAncs(payload: Payload) = getResult {
        appService.getAncs(payload.url)
    }
}