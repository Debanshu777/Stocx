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
import com.debanshu777.stocx.dataSource.repository.StockRepository
import com.debanshu777.stocx.utils.Constants
import com.debanshu777.stocx.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class MainActivityViewModel(
    application:Application,
    private val stockRepository: StockRepository
):AndroidViewModel(application) {
    val stockData:MutableLiveData<Resource<StockResponse>> = MutableLiveData()
    var stockDataResponse: StockResponse? = null
    init {
        getDataToUI()
    }
    private fun getDataToUI() = viewModelScope.launch {
        getStockData(Constants.QUERY)
    }

    private suspend fun getStockData(sids: String) {
        stockData.postValue(Resource.Loading())
        try {
            //if (hasInternetConnection()) {
                val response = stockRepository.getStockDataFromNetwork(sids)
                stockData.postValue(handleStockResponse(response))
            //} else {
              //  stockData.postValue(Resource.Error("No Internet Connection"))
            //}
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

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<StocxApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }

    suspend fun getStockDataFromNetwork(sids:String) =
        stockRepository.getStockDataFromNetwork(sids)
    suspend fun updateLocalStockData(stock: Stock) =
        stockRepository.updateLocalStockData(stock)
    fun getStockDataFromLocal(): Flow<List<Stock>> =
        stockRepository.getStockDataFromLocal()

}