package ua.testtask.currencyexchanger.domain.entity

import android.content.res.Resources
import ua.testtask.currencyexchanger.data.database.entity.WalletDBO
import ua.testtask.currencyexchanger.ui.entity.main.WalletUIEntity

data class WalletDomainEntity(
    val id: Long,
    val name: String,
    val balance: Float,
) {
    fun toDBO(): WalletDBO =
        WalletDBO(
            id = id,
            name = name,
            balance = balance,
        )

    fun toUI(resources: Resources): WalletUIEntity =
        WalletUIEntity(
            resources = resources,
            id = id,
            name = name,
            balance = balance,
        )

    fun isDefaultWallet() = name == DEFAULT_BASE_CURRENCY

    companion object {
        const val DEFAULT_BASE_CURRENCY = "EUR"
    }
}
