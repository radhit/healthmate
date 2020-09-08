package com.healthmate.menu.mom.kia.data

import com.healthmate.api.Payload
import com.healthmate.api.resultLiveDataNoDao
import javax.inject.Inject

class KiaRepository @Inject constructor(private val kiaRemoteDataSource: KiaRemoteDataSource){
    fun updateDataMom(payload: Payload) = resultLiveDataNoDao (
            networkCall = {kiaRemoteDataSource.updateDataMom(payload)}
    )

}