package ua.testtask.currencyexchanger.ui.entity.main

import android.content.res.Resources
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import ua.testtask.currencyexchanger.R

@Stable data class ExchangeUIEntity(
    val walletEntity: WalletUIEntity,
    val exchangeValue: Float,
    val exchangeValueStr: String,
    val color: Color,
) {

    constructor(
        resources: Resources,
        isSell: Boolean,
        walletEntity: WalletUIEntity,
        exchangeValue: Float,
        color: Color,
    ) : this(
        walletEntity = walletEntity,
        exchangeValue = exchangeValue,
        exchangeValueStr = createExchangeValueStr(resources, isSell, exchangeValue),
        color = color,
    )

    fun changeExchange(
        resources: Resources,
        newExchangeValue: Float,
        isSell: Boolean,
    ): ExchangeUIEntity =
        copy(
            exchangeValue = newExchangeValue,
            exchangeValueStr = createExchangeValueStr(resources, isSell, newExchangeValue),
        )

    companion object {

        private fun createExchangeValueStr(
            resources: Resources,
            isSell: Boolean,
            newExchangeValue: Float,
        ): String =
            resources.getString(
                if (isSell) {
                    R.string.main_exchange_value_pattern
                } else {
                    R.string.main_exchange_value_plus_pattern
                },
                newExchangeValue,
            )
    }
}
