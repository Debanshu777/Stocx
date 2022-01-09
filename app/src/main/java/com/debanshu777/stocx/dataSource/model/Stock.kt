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
    val change: Double,

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
) {
    override fun equals(other: Any?): Boolean {
        return when(other){
            is Stock->{
                this.price==other.price && this.change==other.change
            }
            else-> false
        }
    }

    override fun hashCode(): Int {
        return sid.hashCode()
    }
}
