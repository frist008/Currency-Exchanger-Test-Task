package ua.testtask.currencyexchanger.domain.entity

import android.content.res.Resources
import ua.testtask.currencyexchanger.data.database.entity.WalletDBO
import ua.testtask.currencyexchanger.ui.entity.main.entity.WalletUIEntity

data class WalletDomainEntity(
    override val id: Long,
    override val name: String,
    override val balance: Float,
) : WalletEntity {

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
}
