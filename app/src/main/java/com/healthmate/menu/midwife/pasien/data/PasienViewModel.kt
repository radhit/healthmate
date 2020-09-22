package com.healthmate.menu.midwife.pasien.data

import androidx.lifecycle.ViewModel
import com.healthmate.api.Payload
import javax.inject.Inject

class PasienViewModel @Inject constructor(private val pasienRepository: PasienRepository): ViewModel(){
    fun listMother(payload: Payload) = pasienRepository.listMother(payload)

    fun postDataAncsHistory(payload: Payload) = pasienRepository.postDataAncsHistory(payload)

    fun getDataKala(payload: Payload) = pasienRepository.getDataKala(payload)

    fun getInc(payload: Payload) = pasienRepository.getInc(payload)

    fun getDataSummary(payload: Payload) = pasienRepository.getDataSummary(payload)

    fun getDataBabyNote(payload: Payload) = pasienRepository.getDataBabyNote(payload)

    fun getListPnc(payload: Payload) = pasienRepository.getListPnc(payload)
}