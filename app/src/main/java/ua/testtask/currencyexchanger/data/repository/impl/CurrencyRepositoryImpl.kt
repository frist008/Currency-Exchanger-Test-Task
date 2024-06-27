package ua.testtask.currencyexchanger.data.repository.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ua.testtask.currencyexchanger.data.database.dao.WalletDAO
import ua.testtask.currencyexchanger.data.database.entity.WalletDBO
import ua.testtask.currencyexchanger.data.network.api.AppApi
import ua.testtask.currencyexchanger.data.repository.CurrencyRepository
import ua.testtask.currencyexchanger.domain.entity.CurrencyDomainEntity
import ua.testtask.currencyexchanger.domain.entity.WalletDomainEntity
import ua.testtask.currencyexchanger.domain.entity.exception.IncorrectBalanceException
import javax.inject.Inject

// In an ideal world with more complex logic
// you might consider creating a DataSource for each data source type
class CurrencyRepositoryImpl @Inject constructor(
    private val api: AppApi,
    private val walletDAO: WalletDAO,
) : CurrencyRepository {

    // In an ideal world this is stored on the server
    override fun getWallets(): Flow<List<WalletDomainEntity>> =
        walletDAO.getAll().map { list -> list.map(WalletDBO::toDomainEntity) }

    override suspend fun getPriceOfCurrencies(baseCurrency: String?): Map<String, CurrencyDomainEntity> {
        // Uncomment in ideal world
        // val baseCurrency =
        when (baseCurrency) {
            null -> WalletDomainEntity.DEFAULT_BASE_CURRENCY
            WalletDomainEntity.DEFAULT_BASE_CURRENCY -> WalletDomainEntity.DEFAULT_BASE_CURRENCY
            else -> throw UnsupportedOperationException()
        }

        val dto = api.getCurrencies()
        // Uncomment in ideal world
        // val dto = api.getCurrencies(baseCurrency)

        return coroutineScope {
            launch(Dispatchers.IO) { walletDAO.insertAll(dto.toDBOList()) }

            dto.toDomainMap()
        }
    }

    override suspend fun updateWallet(
        priceOfCurrencies: Map<String, CurrencyDomainEntity>,
        base: WalletDomainEntity,
        target: WalletDomainEntity,
        sum: Float,
    ) {
        val newBase = base.copy(balance = base.balance - sum)
        val getPriceOfCurrency =
            { key: String -> priceOfCurrencies[key] ?: throw IllegalArgumentException() }

        if (newBase.balance < 0) {
            throw IncorrectBalanceException()
        }

        val newTarget = when {
            base.name == WalletDomainEntity.DEFAULT_BASE_CURRENCY -> {
                val buyPrice = getPriceOfCurrency(target.name).buyPrice
                target.copy(balance = target.balance + sum * buyPrice)
            }

            target.name == WalletDomainEntity.DEFAULT_BASE_CURRENCY -> {
                val sellPrice = getPriceOfCurrency(base.name).sellPrice
                target.copy(balance = target.balance + sum / sellPrice)
            }

            else -> {
                val sellPrice = getPriceOfCurrency(base.name).sellPrice
                val buyPrice = getPriceOfCurrency(target.name).buyPrice
                target.copy(balance = target.balance + sum / sellPrice * buyPrice)
            }
        }

        walletDAO.update(newBase.toDBO(), newTarget.toDBO())
    }
}
