package com.healthmate.di

import android.content.Context
import com.healthmate.di.room.RoomModule
import com.healthmate.menu.auth.data.AuthViewModel
import com.healthmate.menu.mom.home.data.BerandaViewModel
import com.healthmate.menu.mom.kia.data.KiaViewModel
import com.healthmate.menu.mom.rapor.data.RaporViewModel
import com.healthmate.menu.reusable.data.MasterViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, RoomModule::class])

interface AppComponent {
    fun applicationContext(): Context
    fun authVM(): AppViewModelFactory<AuthViewModel>
    fun homeVM(): AppViewModelFactory<BerandaViewModel>
    fun kiaVM(): AppViewModelFactory<KiaViewModel>
    fun raporVM(): AppViewModelFactory<RaporViewModel>
    fun masterVM(): AppViewModelFactory<MasterViewModel>
}