package ua.testtask.currencyexchanger.data.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import ua.testtask.currencyexchanger.data.network.exception.NetworkException
import javax.inject.Inject

class ErrorMapperInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response =
        try {
            chain.proceed(chain.request())
        } catch (e: Throwable) {
            throw NetworkException(e)
        }
}
