package com.healthmate.menu.mom.home.data

import androidx.lifecycle.ViewModel
import com.healthmate.api.Payload
import javax.inject.Inject

class BerandaViewModel @Inject constructor(private val berandaRepository: BerandaRepository): ViewModel(){
    fun statusCheckUp(payload: Payload) = berandaRepository.statusCheckUp(payload)
}