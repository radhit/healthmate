package com.healthmate.menu.mom.rapor.data

import androidx.lifecycle.ViewModel
import com.healthmate.api.Payload
import javax.inject.Inject

class RaporViewModel @Inject constructor(private val raporRepository: RaporRepository):ViewModel(){
    fun getAncs(payload: Payload) = raporRepository.getAncs(payload)
}