package ua.testtask.currencyexchanger.ui.screen.main

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ua.testtask.currencyexchanger.R
import ua.testtask.currencyexchanger.ui.component.dialog.TwoActionDialog
import ua.testtask.currencyexchanger.ui.entity.base.UIState
import ua.testtask.currencyexchanger.ui.entity.main.dialog.MainSubmitDialogState
import ua.testtask.currencyexchanger.ui.entity.main.dialog.MainSubmitWithCommissionDialogState

@Composable
fun MainDialog(alertDialogState: UIState, onDismissRequest: () -> Unit) {
    when (alertDialogState) {
        is MainSubmitDialogState -> {
            TwoActionDialog(
                titleRes = R.string.main_action_dialog_title,
                onDismissRequest = onDismissRequest,
            ) {
                Text(
                    text = stringResource(
                        R.string.main_action_dialog_text_pattern,
                        alertDialogState.base,
                        alertDialogState.baseName,
                        alertDialogState.target,
                        alertDialogState.targetName,
                    ),
                )
            }
        }

        is MainSubmitWithCommissionDialogState -> {
            TwoActionDialog(
                titleRes = R.string.main_action_dialog_title,
                onDismissRequest = onDismissRequest,
            ) {
                Text(
                    text = stringResource(
                        R.string.main_action_dialog_text_with_commission_pattern,
                        alertDialogState.base,
                        alertDialogState.baseName,
                        alertDialogState.target,
                        alertDialogState.targetName,
                        alertDialogState.commission,
                    ),
                )
            }
        }

        else -> {
            // None
        }
    }
}
