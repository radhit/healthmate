package com.healthmate.menu.mom.kia.data

import androidx.lifecycle.ViewModel
import com.healthmate.api.Payload
import javax.inject.Inject

class KiaViewModel @Inject constructor(private val kiaRepository: KiaRepository): ViewModel(){
        fun updateDataMom(payload: Payload) = kiaRepository.updateDataMom(payload)
}