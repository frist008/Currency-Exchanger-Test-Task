package ua.testtask.currencyexchanger.ui.component.item

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.testtask.currencyexchanger.R
import ua.testtask.currencyexchanger.ui.svg.ArrowDown
import ua.testtask.currencyexchanger.ui.svg.ArrowUp
import ua.testtask.currencyexchanger.ui.theme.RootTheme
import ua.testtask.currencyexchanger.ui.theme.color.Palette

@Composable
fun ExchangeItemComponent(
    modifier: Modifier = Modifier,
    horizontalPadding: Int,
    icon: ImageVector,
    iconBackgroundColor: Color,
    @StringRes textRes: Int,
    value: String,
    valueColor: Color,
    currencyCode: String,
    itemClicked: () -> Unit,
    selectCurrencyClicked: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = itemClicked)
            .padding(start = horizontalPadding.dp)
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ExchangeItemStart(icon, iconBackgroundColor, textRes)
        ExchangeItemValue(value, valueColor)
        ExchangeItemCurrency(horizontalPadding, currencyCode, selectCurrencyClicked)
    }
}

@Composable
private fun RowScope.ExchangeItemStart(
    icon: ImageVector,
    iconBackgroundColor: Color,
    @StringRes textRes: Int,
) {
    val text = stringResource(textRes)
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(iconBackgroundColor),
    ) {
        Image(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            imageVector = icon,
            contentDescription = text,
            colorFilter = ColorFilter.tint(Palette.WHITE),
        )
    }
    Spacer(modifier = Modifier.width(12.dp))
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
        text = text,
        fontSize = 14.sp,
    )
    Spacer(modifier = Modifier.width(12.dp))
}

@Composable
private fun ExchangeItemValue(value: String, valueColor: Color) {
    Text(
        text = value,
        color = valueColor,
        fontSize = 14.sp,
    )
}

@Composable
private fun ExchangeItemCurrency(
    horizontalPadding: Int,
    currencyCode: String,
    selectCurrencyClicked: () -> Unit,
) {
    val layoutPadding = (horizontalPadding / 2).dp

    Row(
        modifier = Modifier
            .padding(horizontal = layoutPadding)
            .clip(CircleShape)
            .clickable(onClick = selectCurrencyClicked)
            .padding(layoutPadding),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = currencyCode,
            fontSize = 14.sp,
        )
        Image(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = currencyCode,
        )
    }
}

@Preview(
    showBackground = true,
    backgroundColor = Palette.WHITE_LONG,
)
@Composable
private fun ExchangeItemComponentPreview() {
    RootTheme {
        Column {
            ExchangeItemComponent(
                horizontalPadding = 16,
                icon = Icons.ArrowUp,
                iconBackgroundColor = Palette.RED,
                textRes = R.string.main_exchange_sell,
                value = "100.00",
                valueColor = Palette.BLACK_DARK,
                currencyCode = "EUR",
                itemClicked = {},
                selectCurrencyClicked = {},
            )
            ExchangeItemComponent(
                horizontalPadding = 16,
                icon = Icons.ArrowDown,
                iconBackgroundColor = Palette.GREEN,
                textRes = R.string.main_exchange_receive,
                value = "+100.00",
                valueColor = Palette.GREEN,
                currencyCode = "USD",
                itemClicked = {},
                selectCurrencyClicked = {},
            )
        }
    }
}
