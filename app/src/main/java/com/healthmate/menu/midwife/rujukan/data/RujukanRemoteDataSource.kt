package com.healthmate.menu.midwife.rujukan.data

import com.healthmate.api.AppService
import com.healthmate.api.BaseDataSource
import com.healthmate.api.Payload
import javax.inject.Inject

class RujukanRemoteDataSource @Inject constructor(private val appService: AppService): BaseDataSource(){
    suspend fun listReferences(payload: Payload) = getResult {
        appService.getListReferences(payload.url)
    }
}