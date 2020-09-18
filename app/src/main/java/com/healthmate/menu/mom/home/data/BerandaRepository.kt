package com.healthmate.menu.mom.home.data

import com.healthmate.api.Payload
import com.healthmate.api.resultLiveDataNoDao
import javax.inject.Inject

class BerandaRepository @Inject constructor(private val berandaRemoteDataSource: BerandaRemoteDataSource){
    fun statusCheckUp(payload: Payload) = resultLiveDataNoDao(
            networkCall ={berandaRemoteDataSource.statusCheckUp(payload)}
    )

    fun postRating(payload: Payload) = resultLiveDataNoDao (
            networkCall = {berandaRemoteDataSource.postRating(payload)}
    )
}