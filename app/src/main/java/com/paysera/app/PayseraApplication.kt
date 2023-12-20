package com.paysera.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PayseraApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}