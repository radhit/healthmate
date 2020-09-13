package com.healthmate.menu.midwife.pasien.data

import com.healthmate.api.AppService
import com.healthmate.api.BaseDataSource
import com.healthmate.api.Payload
import javax.inject.Inject

class PasienRemoteDataSource @Inject constructor(private val appService: AppService): BaseDataSource(){
    suspend fun listMother(payload: Payload) = getResult {
        appService.listMothers(payload.url)
    }
}