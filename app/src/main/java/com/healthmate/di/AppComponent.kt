package com.healthmate.di

import android.content.Context
import com.healthmate.di.room.RoomModule
import com.healthmate.menu.auth.data.AuthViewModel
import com.healthmate.menu.midwife.pasien.data.PasienViewModel
import com.healthmate.menu.midwife.rujukan.data.RujukanViewModel
import com.healthmate.menu.mom.home.data.BerandaViewModel
import com.healthmate.menu.reusable.data.MasterViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, RoomModule::class])

interface AppComponent {
    fun applicationContext(): Context
    fun authVM(): AppViewModelFactory<AuthViewModel>
    fun homeVM(): AppViewModelFactory<BerandaViewModel>
    fun masterVM(): AppViewModelFactory<MasterViewModel>
    fun pasienVM(): AppViewModelFactory<PasienViewModel>
    fun rujukanVM(): AppViewModelFactory<RujukanViewModel>
}