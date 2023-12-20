package com.paysera.app.domain.network

import com.paysera.app.domain.models.ExchangeRate
import retrofit2.http.GET

interface CurrencyExchangeRateApi {
    @GET("tasks/api/currency-exchange-rates")
    suspend fun getRates(): ExchangeRate
}
