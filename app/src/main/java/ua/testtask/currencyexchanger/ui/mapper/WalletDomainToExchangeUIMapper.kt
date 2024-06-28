package ua.testtask.currencyexchanger.ui.mapper

import ua.testtask.currencyexchanger.domain.entity.WalletDomainEntity
import ua.testtask.currencyexchanger.ui.entity.main.entity.ExchangeUIEntity
import ua.testtask.currencyexchanger.ui.theme.color.Palette
import javax.inject.Inject

class WalletDomainToExchangeUIMapper @Inject constructor(private val mapper: WalletToUIMapper) :
    (WalletDomainEntity, Boolean) -> ExchangeUIEntity {

    override operator fun invoke(entity: WalletDomainEntity, isSell: Boolean): ExchangeUIEntity {
        val walletEntity = mapper(entity)
        return ExchangeUIEntity(
            walletEntity = walletEntity,
            exchangeValue = 0.0f,
            maxBalance = if (isSell) walletEntity.balance else Float.MAX_VALUE,
            _prefix = if (isSell) "" else "+",
            _color = if (isSell) Palette.BLACK_DARK else Palette.GREEN,
        )
    }
}
