package com.debanshu777.stocx.dataSource.polling

import com.debanshu777.stocx.dataSource.model.StockResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface Poller {
    @ExperimentalCoroutinesApi
    fun poll(delay: Long):Flow<Response<StockResponse>>
}