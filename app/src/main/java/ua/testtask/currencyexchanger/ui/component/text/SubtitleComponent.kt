package ua.testtask.currencyexchanger.ui.component.text

import androidx.annotation.StringRes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import ua.testtask.currencyexchanger.R
import ua.testtask.currencyexchanger.ui.theme.RootTheme
import ua.testtask.currencyexchanger.ui.theme.color.Palette

@Composable
fun SubtitleComponent(modifier: Modifier = Modifier, @StringRes textRes: Int) {
    SubtitleComponent(modifier, stringResource(textRes))
}

@Composable
fun SubtitleComponent(modifier: Modifier = Modifier, text: String) {
    Text(
        modifier = modifier,
        text = text,
        fontWeight = FontWeight.Medium,
        color = Palette.GRAY,
        fontSize = 18.sp,
    )
}

@Preview(
    showBackground = true,
    backgroundColor = Palette.WHITE_LONG,
)
@Composable
private fun SubtitleComponentPreview() {
    RootTheme {
        SubtitleComponent(textRes = R.string.main_balance_title)
    }
}
