package ua.testtask.currencyexchanger.ui.theme.color

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ua.testtask.currencyexchanger.ui.theme.RootTheme

val AppColorScheme: ColorScheme = darkColorScheme(
    primary = Palette.BLUE_LIGHT,
    onPrimary = Palette.WHITE,
    primaryContainer = Palette.BLUE_LIGHT,
    onPrimaryContainer = Palette.WHITE,
    inversePrimary = Palette.BLUE_DARK,
    secondary = Palette.BLUE_DARK,
    onSecondary = Palette.WHITE,
    secondaryContainer = Palette.BLUE_DARK,
    onSecondaryContainer = Palette.WHITE,
    tertiary = Palette.BLUE_DARK,
    onTertiary = Palette.WHITE,
    tertiaryContainer = Palette.BLUE_DARK,
    onTertiaryContainer = Palette.WHITE,
    background = Palette.WHITE,
    onBackground = Palette.BLACK,
    surface = Palette.BLUE_LIGHT,
    onSurface = Palette.WHITE,
    surfaceVariant = Palette.BLUE_DARK,
    onSurfaceVariant = Palette.WHITE,
    surfaceTint = Palette.WHITE,
    inverseSurface = Palette.BLUE_DARK,
    inverseOnSurface = Palette.WHITE,
    error = Palette.RED,
    onError = Palette.WHITE,
    errorContainer = Palette.RED,
    onErrorContainer = Palette.WHITE,
    outline = Palette.TRANSPARENT,
    outlineVariant = Palette.TRANSPARENT,
    scrim = Palette.WHITE,
    surfaceBright = Palette.WHITE,
    surfaceContainerHighest = Palette.WHITE,
    surfaceContainerHigh = Palette.WHITE,
    surfaceContainer = Palette.WHITE,
    surfaceContainerLow = Palette.WHITE,
    surfaceContainerLowest = Palette.WHITE,
    surfaceDim = Palette.WHITE,
)

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RootThemePreview() {
    RootTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Title") },
                )
            },
            modifier = Modifier.fillMaxSize(),
        ) {
            Text(text = "Hello World", modifier = Modifier.padding(it))
        }
    }
}
