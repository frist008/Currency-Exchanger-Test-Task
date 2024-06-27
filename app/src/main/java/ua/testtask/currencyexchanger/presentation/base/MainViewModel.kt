package ua.testtask.currencyexchanger.presentation.base

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.getAndUpdate
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import ua.testtask.currencyexchanger.data.repository.CurrencyRepository
import ua.testtask.currencyexchanger.ui.entity.base.UIState
import ua.testtask.currencyexchanger.ui.entity.main.ExchangeUIEntity
import ua.testtask.currencyexchanger.ui.entity.main.MainSuccessState
import ua.testtask.currencyexchanger.ui.mapper.WalletDomainToExchangeUIMapper
import ua.testtask.currencyexchanger.ui.mapper.WalletToUIMapper
import javax.inject.Inject

@HiltViewModel class MainViewModel @Inject constructor(
    private val repository: CurrencyRepository,
    private val walletToExchangeUIMapper: WalletDomainToExchangeUIMapper,
    private val walletToUIMapper: WalletToUIMapper,
) : BaseViewModel() {

    private var listenChangesJob = atomic<Job?>(null)

    private val sellExchangeCurrencyState = MutableStateFlow<ExchangeUIEntity?>(null)
    private val receiveExchangeCurrencyState = MutableStateFlow<ExchangeUIEntity?>(null)
    private val walletsFlow = repository.getWallets().onEach { wallets ->
        if (sellExchangeCurrencyState.value == null) {
            val wallet = wallets.firstOrNull { it.isDefaultWallet() } ?: return@onEach
            sellExchangeCurrencyState.emit(
                walletToExchangeUIMapper(entity = wallet, isSell = true),
            )
        }
        if (receiveExchangeCurrencyState.value == null) {
            val wallet = wallets.firstOrNull { !it.isDefaultWallet() } ?: return@onEach
            receiveExchangeCurrencyState.emit(
                walletToExchangeUIMapper(entity = wallet, isSell = false),
            )
        }
    }

    init {
        listenChanges()
    }

    fun listenChanges() {
        listenChangesJob.getAndUpdate { oldJob ->
            oldJob?.cancel()

            launch {
                combine(
                    MutableStateFlow(repository.getPriceOfCurrencies()), // TODO add logic with ticker
                    walletsFlow,
                    sellExchangeCurrencyState.filterNotNull(),
                    receiveExchangeCurrencyState.filterNotNull(),
                ) { priceOfCurrencies, wallets, sellExchangeCurrency, receiveExchangeCurrency ->
                    MainSuccessState(
                        walletList = wallets.asSequence().map(walletToUIMapper).toPersistentList(),
                        sellExchangeEntity = sellExchangeCurrency,
                        receiveExchangeEntity = receiveExchangeCurrency,
                        isSubmitEnabled = false,
                    )
                }.collectLatest(mutableState::emit)
            }
        }
    }

    override fun onFailure(cause: Throwable) {
        Timber.w(cause)
        mutableState.tryEmit(UIState.Progress())
    }

    fun onSellItemClicked(currentState: MainSuccessState) {
        // TODO Add logic for click
    }

    fun onReceiveItemClicked(currentState: MainSuccessState) {
        // TODO Add logic for click
    }

    fun onSellSelectCurrencyClicked(currentState: MainSuccessState) {
        // TODO Add logic for click
    }

    fun onReceiveSelectCurrencyClicked(currentState: MainSuccessState) {
        // TODO Add logic for click
    }

    fun onSubmitClicked(currentState: MainSuccessState) {
        // TODO Add logic for click
    }
}
