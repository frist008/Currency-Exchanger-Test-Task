package ua.testtask.currencyexchanger

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.crashlytics
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import ua.testtask.currencyexchanger.util.logger.CrashlyticsTree
import ua.testtask.currencyexchanger.util.logger.TagDebugTree

@HiltAndroidApp class App : Application() {

    override fun onCreate() {
        super.onCreate()

        setupTimber()
        initFirebase()
    }

    private fun setupTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(CrashlyticsTree(), TagDebugTree())
        } else {
            Timber.plant(CrashlyticsTree())
        }

        Timber.i("Application.onCreate")
    }

    private fun initFirebase() {
        Firebase.crashlytics.setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)
        Firebase.crashlytics.setCustomKey("DEBUG", BuildConfig.DEBUG)
    }
}
