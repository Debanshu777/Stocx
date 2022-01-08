package com.debanshu777.stocx.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.debanshu777.stocx.dataSource.model.Stock
import com.debanshu777.stocx.dataSource.model.StockResponse
import com.debanshu777.stocx.dataSource.repository.StockRepository
import com.debanshu777.stocx.utils.Constants
import com.debanshu777.stocx.utils.Resource
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class MainActivityViewModel(
    private val stockRepository: StockRepository
) : ViewModel() {
    val stockData: MutableLiveData<Resource<StockResponse>> = MutableLiveData()
    val isNetworkAvailable: MutableLiveData<Boolean> = MutableLiveData(true)
    val getDataActiveState: MutableLiveData<String> = MutableLiveData("INACTIVE")
    private var stockDataResponse: StockResponse? = null
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
        setStockData(getStockDataFromNetwork(Constants.QUERY))
    }

    suspend fun setStockData(response: Response<StockResponse>) {
        stockData.postValue(Resource.Loading())
        try {
            if (isNetworkAvailable.value == true) {
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

    private suspend fun getStockDataFromNetwork(sids: String) =
        stockRepository.getStockDataFromNetwork(sids)

    private suspend fun updateLocalStockData(stock: Stock) =
        stockRepository.updateLocalStockData(stock)

//    fun getStockDataFromLocal(): List<Stock> =
//        stockRepository.getStockDataFromLocal()
}
