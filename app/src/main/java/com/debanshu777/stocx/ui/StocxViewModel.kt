package com.debanshu777.stocx.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.debanshu777.stocx.dataSource.model.Stock
import com.debanshu777.stocx.dataSource.model.StockResponse
import com.debanshu777.stocx.dataSource.repository.StockRepository
import com.debanshu777.stocx.utils.Constants.Companion.INACTIVE
import com.debanshu777.stocx.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class StocxViewModel(
    private val stockRepository: StockRepository
) : ViewModel() {

    private val stockDataNetworkResponse: MutableLiveData<Resource<StockResponse>> =
        MutableLiveData()
    val isNetworkAvailable: MutableLiveData<Boolean> = MutableLiveData(true)
    val pollingState: MutableLiveData<String> = MutableLiveData(INACTIVE)
    private var stockDataIntermediateNetworkResponse: StockResponse? = null
    val stockDataLocalSingleSource = MutableLiveData<List<Stock>>(null)

    init {
        viewModelScope.launch {
            stockRepository.getStockDataFromLocal().collect {
                stockDataLocalSingleSource.postValue(it)
            }
        }
    }

    fun setDataSingleSource(sids:String) = viewModelScope.launch {
        if (isNetworkAvailable.value == true) {
            setStockData(getStockDataFromNetwork(sids))
        }
    }

    suspend fun setStockData(response: Response<StockResponse>) {
        stockDataNetworkResponse.postValue(Resource.Loading())
        try {
            if (isNetworkAvailable.value == true) {
                val outComeValue = handleStockResponse(response)
                stockDataNetworkResponse.postValue(outComeValue)
                val value = outComeValue.data
                if (value != null) {
                    updateLocalStockData(value.data)
                }
            } else {
                stockDataNetworkResponse.postValue(Resource.Error("No Internet Connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> stockDataNetworkResponse.postValue(Resource.Error("Network Error"))
                else -> stockDataNetworkResponse.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handleStockResponse(response: Response<StockResponse>): Resource<StockResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                stockDataIntermediateNetworkResponse = resultResponse
                return Resource.Success(stockDataIntermediateNetworkResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private suspend fun getStockDataFromNetwork(sids: String) =
        stockRepository.getStockDataFromNetwork(sids)

    private suspend fun updateLocalStockData(stockList: List<Stock>) =
        stockRepository.updateLocalStockData(stockList)

}
