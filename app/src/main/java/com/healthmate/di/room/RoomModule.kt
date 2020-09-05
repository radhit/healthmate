package com.artcak.starter.di.room

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule (application: Application){
    private val appDatabase: AppDatabase
    init {
        appDatabase = Room.databaseBuilder(application,AppDatabase::class.java,AppDatabase.DB_NAME).build()
    }

    @Singleton
    @Provides
    internal fun provideRoomDatabase(): AppDatabase{
        return appDatabase
    }

}