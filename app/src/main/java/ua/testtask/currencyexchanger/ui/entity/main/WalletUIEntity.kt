package ua.testtask.currencyexchanger.ui.entity.main

import android.content.res.Resources
import androidx.compose.runtime.Stable
import ua.testtask.currencyexchanger.R
import ua.testtask.currencyexchanger.domain.entity.WalletDomainEntity

@Stable data class WalletUIEntity(
    val id: Long,
    val name: String,
    val balance: Float,
    val balanceWithName: String,
) {

    constructor(
        resources: Resources,
        id: Long,
        name: String,
        balance: Float,
    ) : this(
        id = id,
        name = name,
        balance = balance,
        balanceWithName = resources.getString(R.string.main_balance_value_pattern, balance, name),
    )

    val isEnable = balance > 0f

    fun toDomain() = WalletDomainEntity(id, name, balance)
}
