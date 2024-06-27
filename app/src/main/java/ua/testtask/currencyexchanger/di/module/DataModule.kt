package ua.testtask.currencyexchanger.di.module

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import retrofit2.Converter
import ua.testtask.currencyexchanger.util.media.MediaType
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideJson(): Json =
        Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
            allowTrailingComma = true
            isLenient = true
        }

    @Provides
    @Singleton
    fun provideJsonFactory(json: Json): Converter.Factory =
        json.asConverterFactory(MediaType.JSON)
}
