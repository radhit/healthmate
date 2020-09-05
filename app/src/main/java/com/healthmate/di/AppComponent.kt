package com.artcak.starter.di

import android.content.Context
import com.artcak.starter.di.room.RoomModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class,RoomModule::class])

interface AppComponent {
    fun applicationContext(): Context
//    fun authVM(): AppViewModelFactory<AuthViewModel>
//    fun jenisTrukVM(): AppViewModelFactory<JenisTrukViewModel>
}