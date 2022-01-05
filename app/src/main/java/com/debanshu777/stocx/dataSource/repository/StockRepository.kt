package com.debanshu777.stocx.dataSource.repository

import com.debanshu777.stocx.dataSource.local.StockDatabase
import com.debanshu777.stocx.dataSource.model.Stock
import com.debanshu777.stocx.dataSource.network.RetrofitInstance

class StockRepository(
    private val db: StockDatabase,
    private val client: RetrofitInstance
) {
    suspend fun getStockDataFromNetwork(sids:String)=
        RetrofitInstance.api.getStockData(sids)

    suspend fun updateLocalStockData(stock: Stock)=
        db.getStockDao().upsertStock(stock)

    fun getStockDataFromLocal()=
        db.getStockDao().getAllStocks()
}