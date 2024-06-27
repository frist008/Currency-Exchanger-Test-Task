package ua.testtask.currencyexchanger.ui.component.root

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import ua.testtask.currencyexchanger.ui.theme.color.Palette

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DefaultTopAppBar(
    title: String,
) {
    TopAppBar(
        title = { Text(title) },
        colors = defaultTopAppBarColors(),
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun defaultTopAppBarColors(): TopAppBarColors =
    TopAppBarDefaults.topAppBarColors(containerColor = Palette.BLUE_LIGHT)
