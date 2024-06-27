package ua.testtask.currencyexchanger.ui.component.progress

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ua.testtask.currencyexchanger.ui.theme.color.Palette

@Composable
fun ColoredCircularProgressIndicator(modifier: Modifier = Modifier) {
    CircularProgressIndicator(
        modifier = modifier.size(64.dp),
        color = Palette.BLUE_LIGHT,
        trackColor = Palette.WHITE,
    )
}
