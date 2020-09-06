package com.healthmate.di

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.healthmate.BuildConfig
import com.healthmate.R
import com.healthmate.di.room.RoomModule
import com.facebook.stetho.Stetho
import timber.log.Timber
import uk.co.chrisjenx.calligraphy.CalligraphyConfig

class App : MultiDexApplication(),DaggerComponentProvider {
    private var activityVisible = false
    companion object{
        var context: App? = null
    }
    override val component: AppComponent by lazy {
        DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .roomModule(RoomModule(this))
                .build()
    }

    override fun onCreate() {
        super.onCreate()
        context =this
        Stetho.initializeWithDefaults(this);
        setupTimber()
        CalligraphyConfig.initDefault(
                CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Roboto-Regular.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        )
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    private fun setupTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    fun isActivityVisible(): Boolean {
        return activityVisible
    }

    fun activityResumed() {
        activityVisible = true
    }

    fun activityPaused() {
        activityVisible = false
    }
}