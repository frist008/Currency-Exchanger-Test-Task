package ua.testtask.currencyexchanger.ui.entity.main.dialog

import androidx.compose.runtime.Stable
import ua.testtask.currencyexchanger.ui.entity.base.UIState

@Stable data class MainSubmitWithCommissionDialogState(
    val base: Float,
    val baseName: String,
    val target: Float,
    val targetName: String,
    val commission: Float,
) : UIState.Success()
