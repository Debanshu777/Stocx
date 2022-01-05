package com.debanshu777.stocx.dataSource.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.debanshu777.stocx.dataSource.model.Stock

@Dao
interface StockDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertStock(article: Stock): Long

    @Query("SELECT * FROM stock")
    fun getAllStocks(): LiveData<List<Stock>>
}