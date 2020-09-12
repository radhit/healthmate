package com.healthmate.menu.auth.data

import com.healthmate.api.Payload
import com.healthmate.api.resultLiveDataNoDao
import javax.inject.Inject

class AuthRepository @Inject constructor(private val authRemoteDataSource: AuthRemoteDataSource){
    fun login(payload: Payload) = resultLiveDataNoDao(
            networkCall ={authRemoteDataSource.login(payload)}
    )

    fun register(payload: Payload) = resultLiveDataNoDao (
            networkCall = {authRemoteDataSource.register(payload)}
    )

    fun verifikasi(payload: Payload) = resultLiveDataNoDao (
            networkCall = {authRemoteDataSource.verifikasi(payload)}
    )
}