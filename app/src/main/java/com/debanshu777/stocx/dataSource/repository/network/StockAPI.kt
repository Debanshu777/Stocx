package com.debanshu777.stocx.dataSource.repository.network

import com.debanshu777.stocx.dataSource.model.StockResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface StockAPI {
    @GET("stocks/quotes")
    suspend fun getStockData(
        @Query("sids") sids: String,
    ): Response<StockResponse>
}
