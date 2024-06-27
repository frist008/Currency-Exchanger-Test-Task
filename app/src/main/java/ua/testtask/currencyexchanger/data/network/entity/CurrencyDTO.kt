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
            rateMap
                .keys
                .asSequence()
                .plus(base)
                .map { it.uppercase(locale) }
                .distinct()
                .map { WalletDBO(name = it) }
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

    companion object {
        const val DEFAULT_BASE_CURRENCY = "EUR"
    }
}
