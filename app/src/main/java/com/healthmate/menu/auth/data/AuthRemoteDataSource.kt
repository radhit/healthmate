package com.healthmate.menu.auth.data

import com.healthmate.api.AppService
import com.healthmate.api.BaseDataSource
import com.healthmate.api.Payload
import com.healthmate.common.constant.Urls
import com.healthmate.common.sharedpreferences.UserPref
import javax.inject.Inject

class AuthRemoteDataSource @Inject constructor(private val  appService: AppService): BaseDataSource(){
    suspend fun login(payload: Payload) = getResult {
        appService.login(Urls.login, payload.getRequestBody())
    }

    suspend fun register(payload: Payload) = getResult {
        appService.register(payload.url, payload.getRequestBody())
    }

    suspend fun verifikasi(payload: Payload) = getResult {
        appService.verifikasi(Urls.verifikasi, payload.getRequestBody())
    }
}