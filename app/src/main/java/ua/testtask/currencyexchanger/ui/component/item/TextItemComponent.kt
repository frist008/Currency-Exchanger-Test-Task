package ua.testtask.currencyexchanger.ui.component.item

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.testtask.currencyexchanger.R
import ua.testtask.currencyexchanger.ui.theme.RootTheme
import ua.testtask.currencyexchanger.ui.theme.color.Palette

@Composable
fun TextItemComponent(
    modifier: Modifier = Modifier,
    @StringRes textRes: Int,
    isEnabled: Boolean = true,
) {
    TextItemComponent(modifier, stringResource(textRes), isEnabled)
}

@Composable
fun TextItemComponent(modifier: Modifier = Modifier, text: String, isEnabled: Boolean = true) {
    Text(
        modifier = modifier.padding(vertical = 8.dp),
        text = text,
        color = if (isEnabled) Palette.BLACK_DARK else Palette.BLACK_LIGHT_DISABLE,
        fontSize = 16.sp,
        textAlign = TextAlign.Center,
    )
}

@Preview(
    showBackground = true,
    backgroundColor = Palette.WHITE_LONG,
)
@Composable
private fun TextItemComponentPreview() {
    RootTheme {
        Column {
            TextItemComponent(
                text = stringResource(R.string.main_balance_value_pattern, 100f, "EUR"),
            )
            TextItemComponent(
                text = stringResource(R.string.main_balance_value_pattern, 100f, "EUR"),
                isEnabled = false,
            )
        }
    }
}
