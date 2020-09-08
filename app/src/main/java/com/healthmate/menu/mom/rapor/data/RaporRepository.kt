package com.healthmate.menu.mom.rapor.data

import com.healthmate.api.Payload
import com.healthmate.api.resultLiveDataNoDao
import javax.inject.Inject

class RaporRepository @Inject constructor(private val raporRemoteDataSource: RaporRemoteDataSource){
    fun getAncs(payload: Payload) = resultLiveDataNoDao (
            networkCall = {raporRemoteDataSource.getAncs(payload)}
    )
}