package com.healthmate.menu.midwife.rujukan.data

import androidx.lifecycle.ViewModel
import com.healthmate.api.Payload
import javax.inject.Inject

class RujukanViewModel @Inject constructor(private val rujukanRepository: RujukanRepository): ViewModel(){
    fun listReferences(payload: Payload) = rujukanRepository.listReferences(payload)
}