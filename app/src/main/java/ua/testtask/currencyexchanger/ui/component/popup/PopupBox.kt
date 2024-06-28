package ua.testtask.currencyexchanger.ui.component.popup

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup

@Composable
fun PopupBox(
    horizontalPadding: Dp = 8.dp,
    alignment: Alignment = Alignment.TopStart,
    onDismissRequest: (() -> Unit),
    content: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding),
    ) {
        Popup(
            alignment = alignment,
            onDismissRequest = onDismissRequest,
            content = content,
        )
    }
}
