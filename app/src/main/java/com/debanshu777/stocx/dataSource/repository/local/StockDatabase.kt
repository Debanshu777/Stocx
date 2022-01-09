package com.debanshu777.stocx.dataSource.repository.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.debanshu777.stocx.dataSource.model.Stock
import com.debanshu777.stocx.utils.Constants.Companion.DB_NAME

@Database(entities = [Stock::class], version = 1)
abstract class StockDatabase : RoomDatabase() {
    abstract fun getStockDao(): StockDao

    companion object {
        @Volatile
        private var instance: StockDatabase? = null
        private val LOCK = Any()
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                StockDatabase::class.java,
                DB_NAME
            ).build()
    }
}
