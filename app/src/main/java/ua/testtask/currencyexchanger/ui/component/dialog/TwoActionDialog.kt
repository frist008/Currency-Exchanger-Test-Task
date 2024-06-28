package ua.testtask.currencyexchanger.ui.component.dialog

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
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
fun TwoActionDialog(
    @StringRes titleRes: Int,
    onDismissRequest: () -> Unit,
    @StringRes confirmButtonRes: Int? = R.string.ok,
    onConfirmButton: () -> Unit = { onDismissRequest() },
    text: @Composable (() -> Unit),
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            if (confirmButtonRes == null) return@AlertDialog

            Text(
                text = stringResource(confirmButtonRes),
                color = Palette.WHITE,
                modifier = Modifier
                    .clickable { onConfirmButton() }
                    .padding(horizontal = 8.dp, vertical = 4.dp),
            )
        },
        dismissButton = {
            Text(
                text = stringResource(R.string.cancel),
                color = Palette.WHITE,
                modifier = Modifier
                    .clickable { onDismissRequest() }
                    .padding(horizontal = 8.dp, vertical = 4.dp),
            )
        },
        title = { Text(text = stringResource(id = titleRes)) },
        text = text,
    )
}

@Preview
@Composable
private fun TextFieldDialogPreview() {
    RootTheme {
        TwoActionDialog(
            titleRes = R.string.main_exchange_sell,
            onDismissRequest = {},
            text = {},
        )
    }
}

@Preview
@Composable
private fun TextFieldDialogEmptyPreview() {
    RootTheme {
        TwoActionDialog(
            titleRes = R.string.main_exchange_receive,
            onDismissRequest = {},
            text = {},
        )
    }
}
