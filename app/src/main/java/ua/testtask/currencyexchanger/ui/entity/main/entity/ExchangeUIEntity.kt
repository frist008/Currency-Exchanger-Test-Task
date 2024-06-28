package ua.testtask.currencyexchanger.ui.entity.main.entity

import android.icu.text.DecimalFormat
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import ua.testtask.currencyexchanger.ui.theme.color.Palette
import kotlin.math.roundToLong
import kotlin.random.Random

@Stable data class ExchangeUIEntity(
    val walletEntity: WalletUIEntity,
    val exchangeValue: Float,
    val maxBalance: Float,
    private val _prefix: String,
    private val _color: Color,
) {

    val exchangeValueForEditStr: String =
        if (exchangeValue == 0f) "" else exchangeValueEditFormat.format(exchangeValue)

    val exchangeValueForViewStr: String =
        if (exchangeValue == 0f) "" else exchangeValueViewFormat.format(exchangeValue)

    val prefix = if (exchangeValue == 0f) "" else _prefix
    val color = when {
        exchangeValue == 0f -> Palette.BLACK_DARK
        else -> _color
    }

    companion object {

        val exchangeValueEditFormat = DecimalFormat("0.##")
        val exchangeValueViewFormat = DecimalFormat("0.00")

        fun round(value: String) =
            if (value.isEmpty()) 0f else (value.toDouble() * 100).roundToLong() / 100f

        fun preview(name: String, prefix: String, color: Color): ExchangeUIEntity =
            ExchangeUIEntity(
                walletEntity = WalletUIEntity.preview(name),
                exchangeValue = Random.nextFloat(),
                maxBalance = Float.MAX_VALUE,
                _prefix = prefix,
                _color = color,
            )
    }
}
