package com.debanshu777.stocx.dataSource.polling

import android.util.Log
import com.debanshu777.stocx.dataSource.model.StockResponse
import com.debanshu777.stocx.dataSource.repository.StockRepository
import com.debanshu777.stocx.ui.MainActivityViewModel
import com.debanshu777.stocx.utils.Constants
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response

class StockPoller(
    private val viewModel: MainActivityViewModel,
    private val repository: StockRepository,
    private val dispatcher:CoroutineDispatcher
):Poller {
    @ExperimentalCoroutinesApi
    override fun poll(delay: Long):Flow<Response<StockResponse>> {
        return channelFlow {
            while (!isClosedForSend) {
                if(viewModel.getDataActiveState.value=="INACTIVE") {
                    close()
                    return@channelFlow
                }
                val data= repository.getStockDataFromNetwork(Constants.QUERY)
                send(data)
                delay(delay)
                Log.e("Poller","Poller Running")

            }
        }.flowOn(dispatcher)
    }
}