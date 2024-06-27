package ua.testtask.currencyexchanger.data.network.entity

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ua.testtask.currencyexchanger.data.database.entity.WalletDBO
import ua.testtask.currencyexchanger.domain.entity.CurrencyDomainEntity
import ua.testtask.currencyexchanger.util.json.DateSerializer
import java.util.Locale

@Serializable data class CurrencyDTO(
    @SerialName("base") val base: String? = null,
    @Serializable(DateSerializer::class) @SerialName("date") val date: LocalDate? = null,
    @SerialName("rates") val rateMap: Map<String, Float> = emptyMap(),
) {

    fun toDBOList(): List<WalletDBO> =
        if (base.isNullOrEmpty()) {
            emptyList()
        } else {
            val locale = Locale.getDefault()
            val newBaseName = base.uppercase(locale)
            rateMap
                .keys
                .asSequence()
                .map {
                    val newName = it.uppercase(locale)
                    if (newName == newBaseName) {
                        WalletDBO(
                            name = it.uppercase(locale),
                            balance = 1000f,
                        )
                    } else {
                        WalletDBO(name = it.uppercase(locale))
                    }
                }
                .plus(WalletDBO(name = newBaseName, balance = 1000f))
                .distinctBy(WalletDBO::name)
                .toList()
        }

    fun toDomainMap(): Map<String, CurrencyDomainEntity> =
        if (base == null) {
            emptyMap()
        } else {
            rateMap.mapValues { (key, value) ->
                CurrencyDomainEntity(
                    baseCurrency = base,
                    targetCurrency = key,
                    sellPrice = value,
                    buyPrice = value,
                )
            }
        }
}
