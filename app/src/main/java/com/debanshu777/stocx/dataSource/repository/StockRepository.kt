package com.debanshu777.stocx.dataSource.repository

import com.debanshu777.stocx.dataSource.local.StockDatabase
import com.debanshu777.stocx.dataSource.network.RetrofitInstance

class StockRepository(
    private val db: StockDatabase,
    private val client: RetrofitInstance
) {

}