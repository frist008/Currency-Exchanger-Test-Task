package ua.testtask.currencyexchanger.presentation.base

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.getAndUpdate
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import ua.testtask.currencyexchanger.data.repository.CurrencyRepository
import ua.testtask.currencyexchanger.domain.usecase.CalculateBalanceUseCase
import ua.testtask.currencyexchanger.domain.usecase.UpdateWalletUseCase
import ua.testtask.currencyexchanger.ui.entity.base.UIState
import ua.testtask.currencyexchanger.ui.entity.main.MainProgressState
import ua.testtask.currencyexchanger.ui.entity.main.MainSuccessState
import ua.testtask.currencyexchanger.ui.entity.main.dialog.MainSubmitDialogState
import ua.testtask.currencyexchanger.ui.entity.main.entity.ExchangeTransactionUIEntity
import ua.testtask.currencyexchanger.ui.entity.main.entity.ExchangeUIEntity
import ua.testtask.currencyexchanger.ui.entity.main.entity.WalletUIEntity
import ua.testtask.currencyexchanger.ui.mapper.WalletDomainToExchangeUIMapper
import ua.testtask.currencyexchanger.ui.mapper.WalletToUIMapper
import javax.inject.Inject

@HiltViewModel class MainViewModel @Inject constructor(
    private val repository: CurrencyRepository,
    private val calculateBalanceUseCase: CalculateBalanceUseCase,
    private val updateWalletUseCase: UpdateWalletUseCase,
    private val walletToExchangeUIMapper: WalletDomainToExchangeUIMapper,
    private val walletToUIMapper: WalletToUIMapper,
) : BaseViewModel() {

    private var listenChangesJob = atomic<Job?>(null)

    private val exchangeTransactionState = MutableStateFlow<ExchangeTransactionUIEntity?>(null)
    private val walletsFlow = repository.getWallets().distinctUntilChanged().onEach { wallets ->
        val oldExchanges = exchangeTransactionState.value

        if (oldExchanges == null) {
            val sellWallet = wallets.firstOrNull { it.isDefaultWallet() } ?: return@onEach
            val receiveWallet = wallets.firstOrNull { !it.isDefaultWallet() } ?: return@onEach
            exchangeTransactionState.emit(
                ExchangeTransactionUIEntity(
                    sell = walletToExchangeUIMapper(entity = sellWallet, isSell = true),
                    receive = walletToExchangeUIMapper(entity = receiveWallet, isSell = false),
                ),
            )
        } else {
            val sellWallet = wallets.firstOrNull { it.id == oldExchanges.sell.walletEntity.id }
                ?: wallets.firstOrNull { it.isDefaultWallet() }
                ?: return@onEach
            val receiveWallet =
                wallets.firstOrNull { it.id == oldExchanges.receive.walletEntity.id }
                    ?: wallets.firstOrNull { !it.isDefaultWallet() }
                    ?: return@onEach
            exchangeTransactionState.emit(
                oldExchanges.copy(
                    sell = oldExchanges.sell.copy(
                        walletEntity = walletToUIMapper(sellWallet),
                        maxBalance = sellWallet.balance,
                    ),
                    receive = oldExchanges.receive.copy(
                        walletEntity = walletToUIMapper(receiveWallet),
                    ),
                ),
            )
        }
    }.map { list ->
        list.sortedByDescending { wallet -> wallet.balance }
    }

    fun listenChanges() {
        listenChangesJob.getAndUpdate { oldJob ->
            oldJob?.cancel()

            launch {
                repository.getPriceOfCurrencies() // TODO add logic with ticker
            }

            launch {
                combine(
                    walletsFlow,
                    exchangeTransactionState.filterNotNull(),
                ) { wallets, exchangeTransactionEntity ->
                    MainSuccessState(
                        walletList = wallets.asSequence().map(walletToUIMapper).toPersistentList(),
                        exchangeTransactionEntity = exchangeTransactionEntity,
                        isError = !exchangeTransactionEntity.isEnoughToExchange(),
                    )
                }.collectLatest(mutableState::emit)
            }
        }
    }

    override fun onFailure(cause: Throwable) {
        Timber.w(cause)
        mutableState.tryEmit(UIState.Progress())
    }

    fun onSellValueChanged(newValue: String) {
        launch(Dispatchers.Main.immediate) {
            val oldExchangeTransaction = exchangeTransactionState.value ?: return@launch
            val newSellValue = ExchangeUIEntity.round(newValue)

            val newReceiveValue = calculateBalanceUseCase.calc(
                base = oldExchangeTransaction.sell.walletEntity,
                target = oldExchangeTransaction.receive.walletEntity,
                sum = newSellValue,
            )
            emitExchangeTransaction(
                oldExchanges = oldExchangeTransaction,
                newSellValue = newSellValue,
                newReceiveValue = newReceiveValue,
            )
        }
    }

    fun onReceiveValueChanged(newValue: String) {
        launch(Dispatchers.Main.immediate) {
            val oldExchanges = exchangeTransactionState.value ?: return@launch
            val newReceiveValue = ExchangeUIEntity.round(newValue)

            val newSellValue = calculateBalanceUseCase.calc(
                base = oldExchanges.receive.walletEntity,
                target = oldExchanges.sell.walletEntity,
                sum = newReceiveValue,
            )
            emitExchangeTransaction(
                oldExchanges = oldExchanges,
                newSellValue = newSellValue,
                newReceiveValue = newReceiveValue,
            )
        }
    }

    fun onSellNewCurrencySelected(newSellWallet: WalletUIEntity) {
        launch(Dispatchers.Main.immediate) {
            val oldExchanges = exchangeTransactionState.value ?: return@launch
            val oldReceiveWaller = oldExchanges.receive.walletEntity
            val newReceiveWallet =
                if (oldReceiveWaller == newSellWallet) {
                    oldExchanges.sell.walletEntity
                } else {
                    oldReceiveWaller
                }

            val newReceiveValue = calculateBalanceUseCase.calc(
                base = newSellWallet,
                target = newReceiveWallet,
                sum = oldExchanges.sell.exchangeValue,
            )
            emitExchangeTransaction(
                oldExchanges = oldExchanges,
                newReceiveValue = newReceiveValue,
                newSellWallet = newSellWallet,
                newReceiveWallet = newReceiveWallet,
            )
        }
    }

    fun onReceiveNewCurrencySelected(newReceiveWallet: WalletUIEntity) {
        launch(Dispatchers.Main.immediate) {
            val oldExchanges = exchangeTransactionState.value ?: return@launch
            val oldSellWaller = oldExchanges.sell.walletEntity
            val newSellWallet =
                if (oldSellWaller == newReceiveWallet) {
                    oldExchanges.receive.walletEntity
                } else {
                    oldSellWaller
                }

            val newSellValue = calculateBalanceUseCase.calc(
                base = newReceiveWallet,
                target = newSellWallet,
                sum = oldExchanges.receive.exchangeValue,
            )
            emitExchangeTransaction(
                oldExchanges = oldExchanges,
                newSellValue = newSellValue,
                newSellWallet = newSellWallet,
                newReceiveWallet = newReceiveWallet,
            )
        }
    }

    private fun emitExchangeTransaction(
        oldExchanges: ExchangeTransactionUIEntity,
        newSellValue: Float = oldExchanges.sell.exchangeValue,
        newReceiveValue: Float = oldExchanges.receive.exchangeValue,
        newSellWallet: WalletUIEntity = oldExchanges.sell.walletEntity,
        newReceiveWallet: WalletUIEntity = oldExchanges.receive.walletEntity,
    ) {
        val newSellEntity = oldExchanges.sell.copy(
            exchangeValue = newSellValue,
            walletEntity = newSellWallet,
            maxBalance = newSellWallet.balance,
        )
        val newReceiveEntity = oldExchanges.receive.copy(
            exchangeValue = newReceiveValue,
            walletEntity = newReceiveWallet,
        )
        val newExchanges = oldExchanges.copy(sell = newSellEntity, receive = newReceiveEntity)

        exchangeTransactionState.tryEmit(newExchanges)
    }

    fun onSubmitClicked() {
        launch {
            mutableState.emit(MainProgressState)
            val oldExchangeTransaction = exchangeTransactionState.value ?: return@launch
            updateWalletUseCase.invoke(
                base = oldExchangeTransaction.sell.walletEntity.toDomain(),
                target = oldExchangeTransaction.receive.walletEntity.toDomain(),
                sum = oldExchangeTransaction.sell.exchangeValue,
            )
            alertDialogMutableState.emit(
                MainSubmitDialogState(
                    oldExchangeTransaction.sell.exchangeValue,
                    oldExchangeTransaction.sell.walletEntity.name,
                    oldExchangeTransaction.receive.exchangeValue,
                    oldExchangeTransaction.receive.walletEntity.name,
                ),
            )
        }
    }
}
