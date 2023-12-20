package com.paysera.app.domain.models

data class ExchangeRate(
    val base: String?, // Base currency
    val date: String?, // Date of the exchange rates
    val rates: Map<String, Double>? // Map of target currencies to their respective rates against the base currency
)
