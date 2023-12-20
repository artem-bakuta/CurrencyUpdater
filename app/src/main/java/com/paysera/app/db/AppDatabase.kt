package com.paysera.app.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.paysera.app.db.dao.ExchangeRateDao
import com.paysera.app.db.dao.models.ExchangeRateDaoModel
import com.paysera.app.utils.db.MapConverter

@Database(entities = [ExchangeRateDaoModel::class], version = 1)
@TypeConverters(MapConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun exchangeRateDao(): ExchangeRateDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                if (instance == null) {
                    instance =
                        Room.databaseBuilder(context, AppDatabase::class.java, "currency_rates")
                            .fallbackToDestructiveMigration()
                            .build()
                }
                return instance!!
            }
        }
    }
}
