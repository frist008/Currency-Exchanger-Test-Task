package ua.testtask.currencyexchanger.data.network

import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import ua.testtask.currencyexchanger.BuildConfig
import ua.testtask.currencyexchanger.data.network.interceptor.ErrorMapperInterceptor

class NetworkClient(domain: String) {

    val client: OkHttpClient

    init {
        val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            setLevel(
                if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                },
            )
        }
        val certificatePinner = CertificatePinner.Builder()
            .also { builder -> BuildConfig.CERTIFICATE_ARR.forEach { builder.add(domain, it) } }
            .build()
        val errorMapperInterceptor = ErrorMapperInterceptor()

        client = OkHttpClient()
            .newBuilder()
            .addInterceptor(errorMapperInterceptor)
            .addNetworkInterceptor(httpLoggingInterceptor)
            .apply {
                if (BuildConfig.CERTIFICATE_ARR.isNotEmpty()) {
                    certificatePinner(certificatePinner)
                }
            }
            .build()
    }
}
