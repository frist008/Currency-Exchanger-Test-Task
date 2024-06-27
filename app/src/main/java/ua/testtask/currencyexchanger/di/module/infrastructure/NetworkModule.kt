package ua.testtask.currencyexchanger.di.module.infrastructure

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.testtask.currencyexchanger.data.network.api.AppApi
import ua.testtask.currencyexchanger.data.network.api.wrapper.AppApiWrapper
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NetworkModule {

    @Binds
    @Singleton
    fun bindApi(wrapper: AppApiWrapper): AppApi
}
