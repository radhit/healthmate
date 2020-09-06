package com.healthmate.di

import android.app.Application
import android.content.Context
import com.healthmate.BuildConfig
import com.healthmate.api.AppInterceptor
import com.healthmate.api.AppService
import com.healthmate.common.constant.Urls
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class AppModule(val application: Application) {

    @Singleton
    @Provides
    fun provideApplicationContext(): Context = application.applicationContext

    @Singleton
    @Provides
    fun provideAppService(okHttpClient: OkHttpClient): AppService {
        return Retrofit.Builder()
                .baseUrl(Urls.getServer())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addCallAdapterFactory(
                        RxJava2CallAdapterFactory.createWithScheduler(
                                Schedulers.io()
                        )
                )
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AppService::class.java)
    }

    @Singleton
    @Provides
    fun provideOkhttpClient(appInterceptor: AppInterceptor): OkHttpClient {
        val builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            builder.addInterceptor(loggingInterceptor)
            builder.addNetworkInterceptor(StethoInterceptor())
        }
        builder.addInterceptor(appInterceptor)
        builder.writeTimeout(10, TimeUnit.MINUTES)
        builder.readTimeout(10, TimeUnit.MINUTES)
        builder.connectTimeout(10, TimeUnit.MINUTES)
        return builder.build()
    }
}