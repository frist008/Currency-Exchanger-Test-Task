package ua.testtask.currencyexchanger.data.network.api

import retrofit2.http.GET
import ua.testtask.currencyexchanger.data.network.entity.CurrencyDTO

interface AppApi {

    @GET("currency-exchange-rates")
    suspend fun getCurrencies(): CurrencyDTO
}
