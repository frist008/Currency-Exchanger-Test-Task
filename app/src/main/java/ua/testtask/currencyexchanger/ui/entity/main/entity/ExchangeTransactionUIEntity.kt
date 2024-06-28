package ua.testtask.currencyexchanger.ui.entity.main.entity

import androidx.compose.runtime.Stable
import ua.testtask.currencyexchanger.ui.theme.color.Palette

@Stable data class ExchangeTransactionUIEntity(
    val sell: ExchangeUIEntity,
    val receive: ExchangeUIEntity,
) {
    fun isEnoughToExchange(): Boolean = sell.exchangeValue <= sell.walletEntity.balance

    companion object {

        fun preview(
            sellName: String = "EUR",
            receiveName: String = "USD",
        ): ExchangeTransactionUIEntity =
            ExchangeTransactionUIEntity(
                sell = ExchangeUIEntity.preview(sellName, "", Palette.BLACK_DARK),
                receive = ExchangeUIEntity.preview(receiveName, "+", Palette.GREEN),
            )
    }
}
