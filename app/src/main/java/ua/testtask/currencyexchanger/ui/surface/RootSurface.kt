package ua.testtask.currencyexchanger.ui.surface

import androidx.compose.runtime.Composable
import ua.testtask.currencyexchanger.ui.screen.main.MainScreen
import ua.testtask.currencyexchanger.ui.theme.RootTheme

@Composable
fun RootSurface() {
    // TODO Add navigation logic

    RootTheme {
        MainScreen()
    }
}
