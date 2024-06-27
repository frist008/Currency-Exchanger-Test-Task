package ua.testtask.currencyexchanger.di.module.infrastructure

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ua.testtask.currencyexchanger.data.database.AppDatabase
import ua.testtask.currencyexchanger.data.database.dao.WalletDAO
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context = context,
            klass = AppDatabase::class.java,
            name = AppDatabase.DATABASE_NAME,
        ).build()

    @Provides
    @Singleton
    fun provideWallet(database: AppDatabase): WalletDAO = database.walletDao()
}
