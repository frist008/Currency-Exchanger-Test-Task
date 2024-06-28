package ua.testtask.currencyexchanger.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.TickerMode
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import ua.testtask.currencyexchanger.data.repository.CurrencyRepository
import ua.testtask.currencyexchanger.domain.entity.CurrencyDomainEntity
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

class SyncCurrenciesUseCase @Inject constructor(
    private val repository: CurrencyRepository,
) : () -> Flow<Map<String, CurrencyDomainEntity>> {

    @OptIn(ObsoleteCoroutinesApi::class)
    override operator fun invoke(): Flow<Map<String, CurrencyDomainEntity>> =
        ticker(
            delayMillis = 5.seconds.inWholeMilliseconds,
            initialDelayMillis = 0,
            context = Dispatchers.IO,
            mode = TickerMode.FIXED_PERIOD,
        ).receiveAsFlow().mapNotNull {
            repository.getPriceOfCurrencies(force = true).first()
        }
}
