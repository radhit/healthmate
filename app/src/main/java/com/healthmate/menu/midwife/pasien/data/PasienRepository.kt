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

    fun getDataKala(payload: Payload) = resultLiveDataNoDao (
            networkCall = {pasienRemoteDataSource.getDataKala(payload)}
    )

    fun getInc(payload: Payload) = resultLiveDataNoDao (
            networkCall = {pasienRemoteDataSource.getDataInc(payload)}
    )

    fun getDataSummary(payload: Payload) = resultLiveDataNoDao (
            networkCall = {pasienRemoteDataSource.getDataSummary(payload)}
    )

    fun getDataBabyNote(payload: Payload) = resultLiveDataNoDao (
            networkCall = {pasienRemoteDataSource.getDataBabyNote(payload)}
    )

    fun getListPnc(payload: Payload) = resultLiveDataNoDao (
            networkCall = {pasienRemoteDataSource.getListPnc(payload)}
    )
}