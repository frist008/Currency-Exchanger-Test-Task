package ua.testtask.currencyexchanger.data.source.impl

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import ua.testtask.currencyexchanger.data.source.TaxStore
import javax.inject.Inject
import kotlin.math.max

class TaxStoreImpl @Inject constructor(
    @ApplicationContext context: Context,
) : TaxStore {

    private val preferences by lazy {
        context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
    }

    private var freeCountExchange: Int
        get() = preferences.getInt(FREE_COUNT_EXCHANGE, 5)
        set(value) = preferences.edit(true) { putInt(FREE_COUNT_EXCHANGE, value) }

    override fun getTaxCoefficient() = if (freeCountExchange > 0) 0f else 0.007f

    override fun consume(): Float {
        val tax = getTaxCoefficient()
        freeCountExchange = max(freeCountExchange - 1, 0)
        return tax
    }

    companion object {

        private const val FILE_NAME = "Taxs"

        private const val FREE_COUNT_EXCHANGE = "FREE_COUNT_EXCHANGE"
    }
}
