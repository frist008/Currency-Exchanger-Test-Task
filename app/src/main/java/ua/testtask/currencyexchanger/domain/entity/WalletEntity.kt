package ua.testtask.currencyexchanger.domain.entity

interface WalletEntity {

    val id: Long

    val name: String

    val balance: Float

    fun isDefaultWallet() = name == DEFAULT_BASE_CURRENCY

    companion object {

        const val DEFAULT_BASE_CURRENCY = "EUR"
    }
}
