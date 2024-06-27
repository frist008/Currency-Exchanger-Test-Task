package ua.testtask.currencyexchanger.domain.entity

import ua.testtask.currencyexchanger.data.database.entity.WalletDBO

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
}
