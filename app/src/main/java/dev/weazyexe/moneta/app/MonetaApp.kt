package dev.weazyexe.moneta.app

import android.app.Application
import dev.weazyexe.moneta.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MonetaApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MonetaApp)
            modules(appModule)
        }
    }
}