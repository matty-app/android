package com.matryoshka.projectx

import android.app.Application
import com.yandex.mapkit.MapKitFactory
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ProjectxApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        setUpMapKit()
    }

    private fun setUpMapKit() {
        MapKitFactory.setApiKey(BuildConfig.MAPS_API_KEY)
    }
}