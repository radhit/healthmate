package com.healthmate.menu.reusable.data

import androidx.lifecycle.ViewModel
import com.healthmate.api.Payload
import javax.inject.Inject

class MasterViewModel @Inject constructor(private val masterRepository: MasterRepository): ViewModel(){
    fun getLocation(payload: Payload) = masterRepository.getLocation(payload)
    fun getHospital(payload: Payload) = masterRepository.getHospital(payload)
}