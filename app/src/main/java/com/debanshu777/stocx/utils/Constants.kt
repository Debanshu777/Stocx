package com.debanshu777.stocx.utils

class Constants {
    companion object {
        const val BASE_URL: String = "https://api.tickertape.in"
        const val QUERY: String = "TCS,RELI,HDBK,INFY,HLL,ITC"
        const val DB_NAME = "stock_db.db"
        const val DELAY_TIME_BETWEEN_CALL: Long = 5_000
        const val ACTIVE = "ACTIVE"
        const val INACTIVE = "INACTIVE"
    }
}
