package ua.testtask.currencyexchanger.domain.usecase

import ua.testtask.currencyexchanger.data.repository.CurrencyRepository
import ua.testtask.currencyexchanger.domain.entity.WalletDomainEntity
import ua.testtask.currencyexchanger.domain.entity.exception.IncorrectBalanceException
import javax.inject.Inject

class UpdateWalletUseCase @Inject constructor(
    private val repository: CurrencyRepository,
    private val calculateBalanceUseCase: CalculateBalanceUseCase,
) {

    suspend fun invoke(base: WalletDomainEntity, target: WalletDomainEntity, sum: Float): Float {
        val newBase = base.copy(balance = base.balance - sum)

        if (newBase.balance < 0) {
            throw IncorrectBalanceException()
        }

        val newTarget = target.copy(
            balance = target.balance + calculateBalanceUseCase.calc(base, target, sum),
        )

        return repository.updateWallet(newBase, newTarget) * sum
    }
}
