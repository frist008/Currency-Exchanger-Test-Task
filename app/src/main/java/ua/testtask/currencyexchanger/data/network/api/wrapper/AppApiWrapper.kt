package ua.testtask.currencyexchanger.data.network.api.wrapper

import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.tasks.asDeferred
import retrofit2.Converter
import retrofit2.Retrofit
import timber.log.Timber
import ua.testtask.currencyexchanger.data.network.NetworkClient
import ua.testtask.currencyexchanger.data.network.api.AppApi
import ua.testtask.currencyexchanger.data.network.entity.CurrencyDTO
import ua.testtask.currencyexchanger.data.network.exception.InternetNotAvailableException
import ua.testtask.currencyexchanger.data.network.exception.NetworkException
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes

/**
 * Logic required to fulfill the requirement:
 * Note: Apart from this, please refrain from using Company name in your homework task.
 */
class AppApiWrapper @Inject constructor(
    private val converterFactory: Converter.Factory,
) : AppApi {

    private var api: AppApi? = null

    private val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig

    private suspend fun getDomainName(): String {
        val configSettingsAsyncTask = remoteConfig.setConfigSettingsAsync(
            remoteConfigSettings {
                fetchTimeoutInSeconds = 10
                minimumFetchIntervalInSeconds = 60.minutes.inWholeSeconds
            },
        )
        val fetchAndActivateTask = remoteConfig.fetchAndActivate()

        awaitAll(configSettingsAsyncTask.asDeferred(), fetchAndActivateTask.asDeferred())

        return remoteConfig.getString(DOMAIN_NAME_KEY)
    }

    private suspend fun getApi(): AppApi? =
        api ?: runCatching {
            val domainName = getDomainName()
            val domain = "https://$domainName/tasks/api/"
            val api = Retrofit.Builder()
                .client(NetworkClient(domainName).client)
                .addConverterFactory(converterFactory)
                .baseUrl(domain)
                .build()
                .create(AppApi::class.java)
                .also(this::api::set)
            api
        }
            .onFailure { Timber.d(it) }
            .getOrNull()

    override suspend fun getCurrencies(): CurrencyDTO =
        getApi()?.getCurrencies() ?: throw NetworkException(InternetNotAvailableException())

    companion object {
        private const val DOMAIN_NAME_KEY = "server_domain"
    }
}
