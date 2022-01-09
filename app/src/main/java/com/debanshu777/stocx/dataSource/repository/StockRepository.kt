package com.debanshu777.stocx.dataSource.repository

import com.debanshu777.stocx.dataSource.repository.local.StockDatabase
import com.debanshu777.stocx.dataSource.model.Stock
import com.debanshu777.stocx.dataSource.repository.network.RetrofitInstance
import kotlinx.coroutines.flow.Flow

class StockRepository(
    private val db: StockDatabase,
) {
    suspend fun getStockDataFromNetwork(sids: String) =
        RetrofitInstance.api.getStockData(sids)

    suspend fun updateLocalStockData(stockList: List<Stock>) =
        db.getStockDao().upsertStock(stockList)

    fun getStockDataFromLocal(): Flow<List<Stock>> =
        db.getStockDao().getAllStocks()
}
