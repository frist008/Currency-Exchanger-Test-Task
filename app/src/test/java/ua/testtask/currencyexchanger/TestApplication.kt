package ua.testtask.currencyexchanger

import timber.log.Timber
import ua.testtask.currencyexchanger.util.logger.TagDebugTree

class TestApplication : App() {

    override fun setupTimber() {
        Timber.plant(TagDebugTree())
    }

    override fun initFirebase() {
        // none
    }
}
