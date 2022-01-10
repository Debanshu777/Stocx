package com.debanshu777.stocx.dataSource.polling

import android.util.Log
import com.debanshu777.stocx.dataSource.model.StockResponse
import com.debanshu777.stocx.dataSource.repository.StockRepository
import com.debanshu777.stocx.ui.StocxViewModel
import com.debanshu777.stocx.utils.Constants
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response

class StockPoller(
    private val viewModel: StocxViewModel,
    private val repository: StockRepository,
    private val dispatcher: CoroutineDispatcher
) : Poller {
    @ExperimentalCoroutinesApi
    override fun poll(delay: Long,sids:String): Flow<Response<StockResponse>> {
        return channelFlow {
            while (!isClosedForSend) {
                if (viewModel.pollingState.value == "INACTIVE" || !viewModel.isNetworkAvailable.value!!) {
                    Log.i("Poller", "Poller Stopping")
                    close()
                    return@channelFlow
                }
                Log.i("Poller", "Poller Running")
                delay(delay)
                val data = repository.getStockDataFromNetwork(sids)
                send(data)
            }
        }.flowOn(dispatcher)
    }
}
