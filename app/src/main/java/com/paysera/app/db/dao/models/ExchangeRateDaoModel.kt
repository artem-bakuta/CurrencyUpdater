package com.paysera.app.db.dao.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ExchangeRateDaoModel(
    @PrimaryKey val id: String,
    val base: String, // Base currency
    val date: String, // Date of the exchange rates
    val rates: Map<String, Double> // Map of target currencies to their respective rates against the base currency
)