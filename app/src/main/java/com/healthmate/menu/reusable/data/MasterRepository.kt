package com.healthmate.menu.reusable.data

import com.healthmate.api.Payload
import com.healthmate.api.resultLiveDataNoDao
import javax.inject.Inject

class MasterRepository @Inject constructor(private val masterRemoteDataSource: MasterRemoteDataSource){
    fun getLocation(payload: Payload) = resultLiveDataNoDao(
            networkCall = {masterRemoteDataSource.getLocation(payload)}
    )

    fun getHospital(payload: Payload) = resultLiveDataNoDao(
            networkCall = {masterRemoteDataSource.getHospital(payload)}
    )
}