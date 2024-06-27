package ua.testtask.currencyexchanger.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ua.testtask.currencyexchanger.data.database.entity.WalletDBO

@Dao interface WalletDAO {

    @Query("SELECT * FROM ${WalletDBO.NAME_TABLE} ORDER BY ${WalletDBO.NAME} ASC")
    fun getAll(): Flow<List<WalletDBO>>

    @Update
    suspend fun update(vararg dbo: WalletDBO)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(currencies: List<WalletDBO>)
}
