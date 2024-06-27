package ua.testtask.currencyexchanger.ui.mapper

import android.content.res.Resources
import ua.testtask.currencyexchanger.domain.entity.WalletDomainEntity
import ua.testtask.currencyexchanger.ui.entity.main.ExchangeUIEntity
import ua.testtask.currencyexchanger.ui.theme.color.Palette
import javax.inject.Inject

class WalletDomainToExchangeUIMapper @Inject constructor(private val resources: Resources) :
    (WalletDomainEntity, Boolean) -> ExchangeUIEntity {

    override operator fun invoke(entity: WalletDomainEntity, isSell: Boolean): ExchangeUIEntity =
        ExchangeUIEntity(
            resources = resources,
            isSell = isSell,
            walletEntity = entity.toUI(resources),
            exchangeValue = 0.0f,
            color = if (isSell) Palette.BLACK_DARK else Palette.GREEN,
        )
}
