package com.debanshu777.stocx.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.debanshu777.stocx.dataSource.repository.StockRepository

class MainActivityViewModelProviderFactory(private val stockRepository: StockRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainActivityViewModel(stockRepository) as T
    }
}