package ua.testtask.currencyexchanger.ui.component.root

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

@Composable
fun DefaultScaffold(
    @StringRes titleRes: Int,
    content: @Composable (PaddingValues) -> Unit,
) {
    DefaultScaffold(
        title = stringResource(titleRes),
        content = content,
    )
}

@Composable
fun DefaultScaffold(
    title: String,
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        topBar = { DefaultTopAppBar(title) },
        content = content,
    )
}
