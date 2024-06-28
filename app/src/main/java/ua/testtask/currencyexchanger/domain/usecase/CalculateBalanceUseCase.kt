package ua.testtask.currencyexchanger.domain.usecase

import kotlinx.coroutines.flow.first
import ua.testtask.currencyexchanger.data.repository.CurrencyRepository
import ua.testtask.currencyexchanger.domain.entity.WalletEntity
import javax.inject.Inject

class CalculateBalanceUseCase @Inject constructor(
    private val repository: CurrencyRepository,
) {

    suspend fun calc(base: WalletEntity, target: WalletEntity, sum: Float): Float {
        val priceOfCurrencies = repository.getPriceOfCurrencies().first()
        val sumAfterTax = sum - sum * repository.getTaxCoefficient()

        val result = when {
            base.name == WalletEntity.DEFAULT_BASE_CURRENCY -> {
                val buyPrice = priceOfCurrencies.getValue(target.name).buyPrice
                sumAfterTax * buyPrice
            }

            target.name == WalletEntity.DEFAULT_BASE_CURRENCY -> {
                val sellPrice = priceOfCurrencies.getValue(base.name).sellPrice
                sumAfterTax / sellPrice
            }

            else -> {
                val sellPrice = priceOfCurrencies.getValue(base.name).sellPrice
                val buyPrice = priceOfCurrencies.getValue(target.name).buyPrice
                sumAfterTax / sellPrice * buyPrice
            }
        }

        return (result.toDouble() * 100).toLong() / 100f
    }
}
