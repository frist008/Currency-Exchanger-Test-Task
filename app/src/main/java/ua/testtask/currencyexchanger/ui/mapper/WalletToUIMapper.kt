package ua.testtask.currencyexchanger.ui.mapper

import android.content.res.Resources
import ua.testtask.currencyexchanger.domain.entity.WalletDomainEntity
import ua.testtask.currencyexchanger.ui.entity.main.WalletUIEntity
import javax.inject.Inject

class WalletToUIMapper @Inject constructor(private val resources: Resources) :
    (WalletDomainEntity) -> WalletUIEntity {

    override operator fun invoke(entity: WalletDomainEntity): WalletUIEntity =
        entity.toUI(resources)
}
