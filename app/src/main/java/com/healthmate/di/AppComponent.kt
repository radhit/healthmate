package com.healthmate.di

import android.content.Context
import com.healthmate.di.room.RoomModule
import com.healthmate.menu.auth.data.AuthViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, RoomModule::class])

interface AppComponent {
    fun applicationContext(): Context
    fun authVM(): AppViewModelFactory<AuthViewModel>
}