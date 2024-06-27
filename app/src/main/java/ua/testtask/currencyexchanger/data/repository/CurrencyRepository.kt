package ua.testtask.currencyexchanger.data.repository

import kotlinx.coroutines.flow.Flow
import ua.testtask.currencyexchanger.domain.entity.CurrencyDomainEntity
import ua.testtask.currencyexchanger.domain.entity.WalletDomainEntity

interface CurrencyRepository {

    suspend fun getWallets(): Flow<List<WalletDomainEntity>>

    suspend fun getPriceOfCurrencies(baseCurrency: String? = null): Map<String, CurrencyDomainEntity>

    suspend fun updateWallet(
        priceOfCurrencies: Map<String, CurrencyDomainEntity>,
        base: WalletDomainEntity,
        target: WalletDomainEntity,
        sum: Float,
    )
}
