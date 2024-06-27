package ua.testtask.currencyexchanger.domain.entity

data class CurrencyDomainEntity(
    val baseCurrency: String,
    val targetCurrency: String,
    val sellPrice: Float,
    val buyPrice: Float,
)
