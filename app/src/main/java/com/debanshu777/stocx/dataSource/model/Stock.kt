package com.debanshu777.stocx.dataSource.model


import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(tableName = "stock")
data class Stock(
    @SerializedName("change")
    val change: Int, // 67
    @SerializedName("close")
    val close: Double, // 3817.75
    @SerializedName("date")
    val date: String, // 2022-01-04T10:28:33.000Z
    @SerializedName("high")
    val high: Double, // 3889.15
    @SerializedName("low")
    val low: Double, // 3811.7
    @SerializedName("price")
    val price: Double, // 3884.75
    @SerializedName("sid")
    val sid: String, // TCS
    @SerializedName("volume")
    val volume: Int // 2488606
)