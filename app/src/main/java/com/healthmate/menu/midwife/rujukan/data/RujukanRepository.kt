package com.healthmate.menu.midwife.rujukan.data

import com.healthmate.api.Payload
import com.healthmate.api.resultLiveDataNoDao
import javax.inject.Inject

class RujukanRepository @Inject constructor(private val rujukanRemoteDataSource: RujukanRemoteDataSource){
    fun listReferences(payload: Payload) = resultLiveDataNoDao(
            networkCall = {rujukanRemoteDataSource.listReferences(payload)}
    )
}