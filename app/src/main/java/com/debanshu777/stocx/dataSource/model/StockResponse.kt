package com.debanshu777.stocx.dataSource.model

import com.google.gson.annotations.SerializedName

data class StockResponse(
    @SerializedName("data")
    val data: List<Stock>,
    @SerializedName("success")
    val success: Boolean
)
