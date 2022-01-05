package com.debanshu777.stocx.dataSource.api

import com.debanshu777.stocx.dataSource.model.StockResponse
import com.debanshu777.stocx.utils.Resource
import retrofit2.http.GET
import retrofit2.http.Query

interface StockAPI {
    @GET("stocks/quotes")
    suspend fun getStockData(
        @Query("sids") sids: String,
    ):Resource<StockResponse>
}