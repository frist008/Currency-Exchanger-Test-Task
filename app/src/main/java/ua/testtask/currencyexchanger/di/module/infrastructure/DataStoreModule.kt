package ua.testtask.currencyexchanger.di.module.infrastructure

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.testtask.currencyexchanger.data.source.TaxStore
import ua.testtask.currencyexchanger.data.source.impl.TaxStoreImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataStoreModule {

    @Binds
    @Singleton
    fun bindTexStore(repository: TaxStoreImpl): TaxStore
}
