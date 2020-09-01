package com.artcak.starter.di

import android.content.Context
import timber.log.Timber
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.artcak.starter.BuildConfig
import com.artcak.starter.R
import com.artcak.starter.di.room.RoomModule
import uk.co.chrisjenx.calligraphy.CalligraphyConfig
import androidx.core.content.ContextCompat.getSystemService
import com.facebook.stetho.Stetho

class App : MultiDexApplication(),DaggerComponentProvider {
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
                .setDefaultFontPath("fonts/OpenSans-Regular.ttf")
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
}