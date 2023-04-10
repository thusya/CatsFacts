package com.thus.catsfacts

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import com.thus.catsfacts.di.appModule

class CatFactsApp : Application() {

    override fun onCreate() {
        super.onCreate()
        koinConfiguration()
    }

    private fun koinConfiguration() {
        startKoin {
            androidContext(this@CatFactsApp)
            modules(appModule)
        }
    }
}