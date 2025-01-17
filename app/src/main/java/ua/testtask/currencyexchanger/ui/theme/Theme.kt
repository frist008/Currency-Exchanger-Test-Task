package ua.testtask.currencyexchanger.ui.theme

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ua.testtask.currencyexchanger.ui.theme.color.AppColorScheme

@Composable
fun RootTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = getColorScheme(),
        content = content,
    )
}

@Composable @ReadOnlyComposable
private fun getColorScheme() = AppColorScheme

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
                    title = {
                        Text(text = "Title")
                    },
                )
            },
            modifier = Modifier.fillMaxSize(),
        ) {
            Text(text = "Hello World", modifier = Modifier.padding(it))
        }
    }
}
