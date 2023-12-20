package com.paysera.app.domain.repositories

import com.paysera.app.db.AppDatabase
import com.paysera.app.db.dao.models.ExchangeRateDaoModel
import com.paysera.app.domain.network.CurrencyExchangeRateApi
import com.paysera.app.utils.numbers.roundTwoDec

class ExchangeRateRepository(
    private val api: CurrencyExchangeRateApi,
    private val db: AppDatabase
) {
    suspend fun getLatestRates(): ExchangeRateDaoModel {
        val rates = api.getRates()

        val exchangeRateDaoModel = ExchangeRateDaoModel(
            id = "",
            base = rates.base.orEmpty(),
            date = rates.date.orEmpty(),
            rates = rates.rates?.mapValues { it.value.roundTwoDec() } ?: emptyMap()
        )

        db.exchangeRateDao().insertAll(listOf(rates).map {
            exchangeRateDaoModel
        })
        return exchangeRateDaoModel

    }

    suspend fun getLatestRatesCached(): ExchangeRateDaoModel? {
        return db.exchangeRateDao().getLatestRate()
    }
}


