package com.healthmate.menu.midwife.pasien.data

import com.healthmate.api.Payload
import com.healthmate.api.resultLiveDataNoDao
import javax.inject.Inject

class PasienRepository @Inject constructor(private val pasienRemoteDataSource: PasienRemoteDataSource){
    fun listMother(payload: Payload) = resultLiveDataNoDao(
            networkCall = {pasienRemoteDataSource.listMother(payload)}
    )

    fun postDataAncsHistory(payload: Payload) = resultLiveDataNoDao(
            networkCall = {pasienRemoteDataSource.postDataAncsHistory(payload)}
    )
}