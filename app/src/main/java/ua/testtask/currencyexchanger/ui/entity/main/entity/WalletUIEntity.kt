package ua.testtask.currencyexchanger.ui.entity.main.entity

import android.content.res.Resources
import androidx.compose.runtime.Stable
import ua.testtask.currencyexchanger.R
import ua.testtask.currencyexchanger.domain.entity.WalletDomainEntity
import ua.testtask.currencyexchanger.domain.entity.WalletEntity
import kotlin.random.Random

@Stable data class WalletUIEntity(
    override val id: Long,
    override val name: String,
    override val balance: Float,
    val balanceWithName: String,
) : WalletEntity {

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

    companion object {

        private var previewId = 0L

        fun preview(name: String): WalletUIEntity {
            val balance = Random.nextInt(0, 1000000)
            return WalletUIEntity(
                id = previewId++,
                name = name,
                balance = balance.toFloat(),
                balanceWithName = "$name $balance.00",
            )
        }
    }
}
