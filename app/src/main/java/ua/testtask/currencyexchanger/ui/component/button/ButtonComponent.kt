package ua.testtask.currencyexchanger.ui.component.button

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.testtask.currencyexchanger.R
import ua.testtask.currencyexchanger.ui.theme.RootTheme
import ua.testtask.currencyexchanger.ui.theme.color.Palette

@Composable
fun ButtonComponent(modifier: Modifier = Modifier, @StringRes textRes: Int, onClick: () -> Unit) {
    ButtonComponent(
        modifier = modifier,
        text = stringResource(textRes),
        onClick = onClick,
    )
}

@Composable
fun ButtonComponent(modifier: Modifier = Modifier, text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier,
    ) {
        Text(
            text = text,
            color = Palette.WHITE,
        )
    }
}

@Preview(
    showBackground = true,
    backgroundColor = Palette.WHITE_LONG,
)
@Composable
private fun ButtonComponentPreview() {
    RootTheme {
        ButtonComponent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            text = stringResource(R.string.main_balance_value_pattern, 100f, "EUR"),
        ) {}
    }
}
