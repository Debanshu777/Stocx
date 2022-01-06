package com.debanshu777.stocx.ui

import androidx.lifecycle.ViewModel
import com.debanshu777.stocx.dataSource.repository.StockRepository

class MainActivityViewModel(
    private val stockRepository: StockRepository
):ViewModel() {
}