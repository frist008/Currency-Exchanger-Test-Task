package ua.testtask.currencyexchanger.ui.entity.main

import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.PersistentList
import ua.testtask.currencyexchanger.ui.entity.base.UIState

@Stable data class MainSuccessState(
    val walletList: PersistentList<WalletUIEntity>,
    val sellExchangeEntity: ExchangeUIEntity,
    val receiveExchangeEntity: ExchangeUIEntity,
    val isSubmitEnabled: Boolean,
) : UIState.Success()
