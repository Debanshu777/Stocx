package com.debanshu777.stocx.dataSource.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.debanshu777.stocx.dataSource.model.Stock
import kotlinx.coroutines.flow.Flow

@Dao
interface StockDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertStock(stock: Stock): Long

    @Query("SELECT * FROM stock")
    fun getAllStocks(): Flow<List<Stock>>
}