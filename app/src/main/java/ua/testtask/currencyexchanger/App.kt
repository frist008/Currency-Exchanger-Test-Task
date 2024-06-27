package ua.testtask.currencyexchanger

import android.app.Application
import androidx.annotation.OpenForTesting
import androidx.annotation.VisibleForTesting
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.crashlytics
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import ua.testtask.currencyexchanger.util.logger.CrashlyticsTree
import ua.testtask.currencyexchanger.util.logger.TagDebugTree

@OpenForTesting @HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        setupTimber()
        initFirebase()
    }

    @OpenForTesting
    @VisibleForTesting
    fun setupTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(CrashlyticsTree(), TagDebugTree())
        } else {
            Timber.plant(CrashlyticsTree())
        }

        Timber.i("Application.onCreate")
    }

    @OpenForTesting
    @VisibleForTesting
    fun initFirebase() {
        Firebase.crashlytics.setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)
        Firebase.crashlytics.setCustomKey("DEBUG", BuildConfig.DEBUG)
    }
}
