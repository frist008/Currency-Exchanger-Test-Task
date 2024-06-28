package ua.testtask.currencyexchanger.data.repository

import kotlinx.coroutines.flow.Flow
import ua.testtask.currencyexchanger.domain.entity.CurrencyDomainEntity
import ua.testtask.currencyexchanger.domain.entity.WalletDomainEntity

interface CurrencyRepository {

    fun getWallets(): Flow<List<WalletDomainEntity>>

    suspend fun getPriceOfCurrencies(
        baseCurrency: String? = null,
        force: Boolean = false,
    ): Flow<Map<String, CurrencyDomainEntity>>

    suspend fun updateWallet(base: WalletDomainEntity, target: WalletDomainEntity)
}
