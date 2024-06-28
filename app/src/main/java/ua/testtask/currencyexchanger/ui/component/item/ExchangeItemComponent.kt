package ua.testtask.currencyexchanger.ui.component.item

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.testtask.currencyexchanger.R
import ua.testtask.currencyexchanger.ui.entity.main.entity.ExchangeUIEntity
import ua.testtask.currencyexchanger.ui.entity.main.entity.WalletUIEntity
import ua.testtask.currencyexchanger.ui.svg.ArrowDown
import ua.testtask.currencyexchanger.ui.svg.ArrowUp
import ua.testtask.currencyexchanger.ui.theme.RootTheme
import ua.testtask.currencyexchanger.ui.theme.color.Palette
import kotlin.math.min

@Composable
fun ExchangeItemComponent(
    modifier: Modifier = Modifier,
    horizontalPadding: Dp,
    icon: ImageVector,
    iconBackgroundColor: Color,
    @StringRes textRes: Int,
    exchangeEntity: ExchangeUIEntity,
    isError: Boolean,
    onValueChange: (String) -> Unit,
    onCurrencyClicked: () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = { focusRequester.requestFocus() })
            .padding(start = horizontalPadding)
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ExchangeItemStart(
            icon = icon,
            iconBackgroundColor = iconBackgroundColor,
            textRes = textRes,
        )
        ExchangeItemValue(
            focusRequester = focusRequester,
            exchangeEntity = exchangeEntity,
            isError = isError,
            onValueChange = onValueChange,
        )
        ExchangeItemCurrency(
            horizontalPadding = horizontalPadding,
            walletEntity = exchangeEntity.walletEntity,
            onSelectCurrencyClicked = onCurrencyClicked,
        )
    }
}

@Composable
private fun ExchangeItemValue(
    focusRequester: FocusRequester,
    exchangeEntity: ExchangeUIEntity,
    isError: Boolean,
    onValueChange: (String) -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    var editText by remember { mutableStateOf(exchangeEntity.exchangeValueForEditStr) }

    if (!isFocused && editText != exchangeEntity.exchangeValueForViewStr) {
        editText = exchangeEntity.exchangeValueForViewStr
    }

    BasicTextField(
        value = editText,
        onValueChange = { newText ->
            editText = newText.toFloatOrNull()?.let {
                val minValue = min(it, exchangeEntity.maxBalance)
                if (minValue == it) {
                    newText
                } else {
                    ExchangeUIEntity.exchangeValueEditFormat.format(minValue)
                }
            } ?: ""
            onValueChange(editText)
        },
        modifier = Modifier.focusRequester(focusRequester),
        textStyle = TextStyle.Default.copy(
            fontSize = 16.sp,
            color = if (isError) Palette.RED else exchangeEntity.color,
            textAlign = TextAlign.End,
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        singleLine = true,
        interactionSource = interactionSource,
    ) {
        Box(contentAlignment = Alignment.CenterEnd) {
            if (editText.isEmpty()) {
                Text(
                    text = stringResource(R.string.enter_amount_hint),
                    fontSize = 16.sp,
                    color = Palette.BLACK_LIGHT_DISABLE,
                    letterSpacing = 0.sp,
                )
            } else if (exchangeEntity.prefix.isNotEmpty()) {
                Row {
                    Text(
                        text = exchangeEntity.prefix,
                        fontSize = 16.sp,
                        color = exchangeEntity.color,
                    )
                    // TODO fix double drawing
                    Text(
                        text = editText,
                        fontSize = 16.sp,
                        color = Palette.TRANSPARENT,
                        letterSpacing = 0.sp,
                    )
                }
            }

            it()
        }
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
        fontSize = 16.sp,
    )
    Spacer(modifier = Modifier.width(12.dp))
}

@Composable
private fun ExchangeItemCurrency(
    horizontalPadding: Dp,
    walletEntity: WalletUIEntity,
    onSelectCurrencyClicked: () -> Unit,
) {
    val layoutPadding = (horizontalPadding / 2)

    Row(
        modifier = Modifier
            .padding(horizontal = layoutPadding)
            .clip(CircleShape)
            .clickable(onClick = onSelectCurrencyClicked)
            .padding(layoutPadding),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = walletEntity.name,
            fontSize = 16.sp,
        )
        Image(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = walletEntity.name,
            colorFilter = ColorFilter.tint(Palette.BLACK_LIGHT),
        )
    }
}

@Preview(
    showSystemUi = true,
    showBackground = true,
    backgroundColor = Palette.WHITE_LONG,
)
@Composable
private fun ExchangeItemComponentPreview() {
    RootTheme {
        Column {
            ExchangeItemComponent(
                horizontalPadding = 16.dp,
                icon = Icons.ArrowUp,
                iconBackgroundColor = Palette.RED,
                textRes = R.string.main_exchange_sell,
                exchangeEntity = ExchangeUIEntity.preview("EUR", "", Palette.BLACK_DARK),
                onValueChange = {},
                isError = true,
                onCurrencyClicked = {},
            )
            ExchangeItemComponent(
                horizontalPadding = 16.dp,
                icon = Icons.ArrowUp,
                iconBackgroundColor = Palette.RED,
                textRes = R.string.main_exchange_sell,
                exchangeEntity = ExchangeUIEntity.preview("EUR", "", Palette.BLACK_DARK),
                onValueChange = {},
                isError = false,
                onCurrencyClicked = {},
            )
            ExchangeItemComponent(
                horizontalPadding = 16.dp,
                icon = Icons.ArrowDown,
                iconBackgroundColor = Palette.GREEN,
                textRes = R.string.main_exchange_receive,
                exchangeEntity = ExchangeUIEntity.preview("USD", "+", Palette.GREEN),
                onValueChange = {},
                isError = false,
                onCurrencyClicked = {},
            )
            ExchangeItemComponent(
                horizontalPadding = 16.dp,
                icon = Icons.ArrowDown,
                iconBackgroundColor = Palette.GREEN,
                textRes = R.string.main_exchange_receive,
                exchangeEntity = ExchangeUIEntity.preview("USD", "+", Palette.GREEN)
                    .copy(exchangeValue = 0f),
                onValueChange = {},
                isError = false,
                onCurrencyClicked = {},
            )
        }
    }
}
