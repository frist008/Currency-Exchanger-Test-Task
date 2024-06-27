package ua.testtask.currencyexchanger.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import ua.testtask.currencyexchanger.domain.entity.WalletDomainEntity

@Entity(
    tableName = WalletDBO.NAME_TABLE,
    indices = [Index(value = [WalletDBO.NAME], unique = true)],
)
data class WalletDBO(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    val id: Long = 0,
    @ColumnInfo(name = NAME)
    val name: String,
    @ColumnInfo(name = BALANCE)
    val balance: Float = 0f,
) {

    fun toDomainEntity(): WalletDomainEntity =
        WalletDomainEntity(
            id = id,
            name = name,
            balance = balance,
        )

    companion object {
        const val NAME_TABLE = "Currency"
        const val ID = "id_$NAME_TABLE"
        const val NAME = "name_$NAME_TABLE"
        const val BALANCE = "balance_$NAME_TABLE"
    }
}
