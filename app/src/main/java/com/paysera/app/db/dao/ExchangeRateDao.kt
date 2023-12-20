package com.paysera.app.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.paysera.app.db.dao.models.ExchangeRateDaoModel

@Dao
interface ExchangeRateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(rates: List<ExchangeRateDaoModel>)

    @Query("SELECT * FROM ExchangeRateDaoModel ORDER BY date DESC LIMIT 1")
    suspend fun getLatestRate(): ExchangeRateDaoModel?
}
