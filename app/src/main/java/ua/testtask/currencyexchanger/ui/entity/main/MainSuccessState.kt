package ua.testtask.currencyexchanger.ui.entity.main

import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import ua.testtask.currencyexchanger.ui.entity.base.UIState
import ua.testtask.currencyexchanger.ui.entity.main.entity.ExchangeTransactionUIEntity
import ua.testtask.currencyexchanger.ui.entity.main.entity.WalletUIEntity

@Stable data class MainSuccessState(
    val walletList: PersistentList<WalletUIEntity>,
    val exchangeTransactionEntity: ExchangeTransactionUIEntity,
    val isError: Boolean,
) : UIState.Success() {

    companion object {

        fun preview(sellName: String = "EUR", receiveName: String = "USD"): MainSuccessState {
            val exchangeTransactionEntity = ExchangeTransactionUIEntity.preview(
                sellName = sellName,
                receiveName = receiveName,
            )
            return MainSuccessState(
                walletList = persistentListOf(
                    exchangeTransactionEntity.sell.walletEntity,
                    exchangeTransactionEntity.receive.walletEntity,
                    WalletUIEntity.preview(sellName + exchangeTransactionEntity.sell.walletEntity.id),
                    WalletUIEntity.preview(receiveName + exchangeTransactionEntity.receive.walletEntity.id),
                    WalletUIEntity.preview(sellName + exchangeTransactionEntity.sell.walletEntity.id),
                    WalletUIEntity.preview(receiveName + exchangeTransactionEntity.receive.walletEntity.id),
                    WalletUIEntity.preview(sellName + exchangeTransactionEntity.sell.walletEntity.id),
                    WalletUIEntity.preview(receiveName + exchangeTransactionEntity.receive.walletEntity.id),
                    WalletUIEntity.preview(sellName + exchangeTransactionEntity.sell.walletEntity.id),
                    WalletUIEntity.preview(receiveName + exchangeTransactionEntity.receive.walletEntity.id),
                    WalletUIEntity.preview(sellName + exchangeTransactionEntity.sell.walletEntity.id),
                    WalletUIEntity.preview(receiveName + exchangeTransactionEntity.receive.walletEntity.id),
                ),
                exchangeTransactionEntity = exchangeTransactionEntity,
                isError = false,
            )
        }
    }
}
