package ua.testtask.currencyexchanger.util.exception

import timber.log.Timber
import ua.testtask.currencyexchanger.BuildConfig

fun unsupportedUI() {
    if (BuildConfig.DEBUG) {
        throw UnsupportedOperationException()
    } else {
        Timber.e(UnsupportedOperationException())
    }
}
