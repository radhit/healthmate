package com.healthmate.menu.mom.home.data

import com.healthmate.api.AppService
import com.healthmate.api.BaseDataSource
import com.healthmate.api.Payload
import com.healthmate.common.constant.Urls
import javax.inject.Inject

class BerandaRemoteDataSource @Inject constructor(private val appService: AppService): BaseDataSource(){
    suspend fun statusCheckUp(payload: Payload) = getResult {
        appService.statusCheckup(payload.url)
    }

    suspend fun postRating(payload: Payload) = getResult {
        appService.postRatingMother(payload.url,payload.getRequestBody())
    }
}