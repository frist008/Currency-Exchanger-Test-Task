package ua.testtask.currencyexchanger.ui.component.dialog

import androidx.annotation.StringRes
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import ua.testtask.currencyexchanger.R
import ua.testtask.currencyexchanger.ui.theme.RootTheme
import ua.testtask.currencyexchanger.ui.theme.color.Palette

@Composable
fun TextFieldDialog(
    @StringRes titleRes: Int,
    @StringRes hintRes: Int,
    oldValue: String = "",
    @StringRes confirmButtonRes: Int? = R.string.edit,
    onConfirmButton: (newValue: String) -> Unit,
    onDismissRequest: () -> Unit,
) {
    var newValue by remember { mutableStateOf(oldValue) }

    TwoActionDialog(
        titleRes = titleRes,
        onDismissRequest = onDismissRequest,
        confirmButtonRes = confirmButtonRes,
        onConfirmButton = { onConfirmButton(newValue) },
    ) {
        TextField(
            value = newValue,
            label = { Text(stringResource(hintRes)) },
            onValueChange = { newValue = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            colors = TextFieldDefaults.colors().copy(unfocusedLabelColor = Palette.WHITE_DISABLE),
        )
    }
}

@Preview
@Composable
private fun TextFieldDialogPreview() {
    RootTheme {
        TextFieldDialog(
            titleRes = R.string.main_exchange_sell,
            hintRes = R.string.enter_amount_hint,
            oldValue = "100.00",
            onConfirmButton = {},
        ) {}
    }
}

@Preview
@Composable
private fun TextFieldDialogEmptyPreview() {
    RootTheme {
        TextFieldDialog(
            titleRes = R.string.main_exchange_receive,
            hintRes = R.string.enter_amount_hint,
            onConfirmButton = {},
        ) {}
    }
}
