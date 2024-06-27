package ua.testtask.currencyexchanger.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ua.testtask.currencyexchanger.data.database.dao.WalletDAO
import ua.testtask.currencyexchanger.data.database.entity.WalletDBO

@Database(
    entities = [WalletDBO::class],
    version = 1,
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun walletDao(): WalletDAO

    companion object {

        const val DATABASE_NAME = "app_database"
    }
}
