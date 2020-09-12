package com.healthmate.menu.auth.data

import androidx.lifecycle.ViewModel
import com.healthmate.api.Payload
import javax.inject.Inject

class AuthViewModel @Inject constructor(private val authRepository: AuthRepository): ViewModel(){
    fun login(payload: Payload) = authRepository.login(payload)

    fun register(payload: Payload) = authRepository.register(payload)

    fun verifikasi(payload: Payload) = authRepository.verifikasi(payload)
}