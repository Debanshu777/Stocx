package com.debanshu777.stocx.dataSource.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "stock")
data class Stock(

    @PrimaryKey
    @SerializedName("sid")
    val sid: String,

    @SerializedName("change")
    val change: Int,

    @SerializedName("close")
    val close: Double,

    @SerializedName("date")
    val date: String,

    @SerializedName("high")
    val high: Double,

    @SerializedName("low")
    val low: Double,

    @SerializedName("price")
    val price: Double,

    @SerializedName("volume")
    val volume: Int
)