package com.debanshu777.stocx.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.debanshu777.stocx.StocxApplication
import com.debanshu777.stocx.dataSource.model.Stock
import com.debanshu777.stocx.dataSource.model.StockResponse
import com.debanshu777.stocx.dataSource.network.ConnectionLiveData
import com.debanshu777.stocx.dataSource.repository.StockRepository
import com.debanshu777.stocx.utils.Constants
import com.debanshu777.stocx.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class MainActivityViewModel(
    application: Application,
    private val connectionLiveData: ConnectionLiveData,
    private val stockRepository: StockRepository
) : AndroidViewModel(application) {
    val stockData: MutableLiveData<Resource<StockResponse>> = MutableLiveData()
    val isNetworkAvailable: MutableLiveData<Boolean> = MutableLiveData(true)
    var stockDataResponse: StockResponse? = null
    val responseFlow = MutableLiveData<List<Stock>>(null)

    init {
        getDataToUI()
        viewModelScope.launch {
            stockRepository.getStockDataFromLocal().collect {
                responseFlow.postValue(it)
            }
        }
    }

    private fun getDataToUI() = viewModelScope.launch {
        getStockData(Constants.QUERY)
    }

    private suspend fun getStockData(sids: String) {
        stockData.postValue(Resource.Loading())
        try {
            if (isNetworkAvailable.value == true) {
                val response = stockRepository.getStockDataFromNetwork(sids)
                val outComeValue = handleStockResponse(response)
                stockData.postValue(outComeValue)
                val value = outComeValue.data
                if (value != null) {
                    for (i in value.data) {
                        updateLocalStockData(i)
                    }
                }
            } else {
              stockData.postValue(Resource.Error("No Internet Connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> stockData.postValue(Resource.Error("Network Error"))
                else -> stockData.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handleStockResponse(response: Response<StockResponse>): Resource<StockResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                stockDataResponse = resultResponse
                return Resource.Success(stockDataResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    suspend fun getStockDataFromNetwork(sids: String) =
        stockRepository.getStockDataFromNetwork(sids)

    private suspend fun updateLocalStockData(stock: Stock) =
        stockRepository.updateLocalStockData(stock)

//    fun getStockDataFromLocal(): List<Stock> =
//        stockRepository.getStockDataFromLocal()

}