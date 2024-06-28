package ua.testtask.currencyexchanger.data.repository.impl

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ua.testtask.currencyexchanger.data.database.dao.WalletDAO
import ua.testtask.currencyexchanger.data.database.entity.WalletDBO
import ua.testtask.currencyexchanger.data.network.api.AppApi
import ua.testtask.currencyexchanger.data.repository.CurrencyRepository
import ua.testtask.currencyexchanger.data.source.TaxStore
import ua.testtask.currencyexchanger.domain.entity.CurrencyDomainEntity
import ua.testtask.currencyexchanger.domain.entity.WalletDomainEntity
import ua.testtask.currencyexchanger.domain.entity.WalletEntity
import javax.inject.Inject

// In an ideal world with more complex logic
// you might consider creating a DataSource for each data source type
class CurrencyRepositoryImpl @Inject constructor(
    private val api: AppApi,
    private val walletDAO: WalletDAO,
    private val taxStore: TaxStore,
) : CurrencyRepository {

    private val priceOfCurrenciesState = MutableStateFlow(emptyMap<String, CurrencyDomainEntity>())

    // In an ideal world this is stored on the server
    override fun getWallets(): Flow<List<WalletDomainEntity>> =
        walletDAO.getAll().map { list -> list.map(WalletDBO::toDomainEntity) }

    override suspend fun getPriceOfCurrencies(
        baseCurrency: String?,
        force: Boolean,
    ): Flow<Map<String, CurrencyDomainEntity>> {
        if (priceOfCurrenciesState.value.isEmpty() || force) {
            // Uncomment in ideal world
            // val baseCurrency =
            when (baseCurrency) {
                null -> WalletEntity.DEFAULT_BASE_CURRENCY
                WalletEntity.DEFAULT_BASE_CURRENCY -> WalletEntity.DEFAULT_BASE_CURRENCY
                else -> throw UnsupportedOperationException()
            }

            val dto = api.getCurrencies()
            // Uncomment in ideal world
            // val dto = api.getCurrencies(baseCurrency)

            coroutineScope {
                launch { walletDAO.insertAll(dto.toDBOList()) }

                priceOfCurrenciesState.emit(dto.toDomainMap())
            }
        }

        return priceOfCurrenciesState
    }

    override suspend fun updateWallet(base: WalletDomainEntity, target: WalletDomainEntity): Float =
        taxStore.consume().apply {
            walletDAO.update(base.toDBO(), target.toDBO())
        }

    override fun getTaxCoefficient() = taxStore.getTaxCoefficient()
}
