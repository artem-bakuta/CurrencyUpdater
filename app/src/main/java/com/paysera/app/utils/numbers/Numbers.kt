package com.paysera.app.utils.numbers

import java.math.BigDecimal
import java.math.RoundingMode

fun Double.roundTwoDec(): Double {
    return BigDecimal(this).setScale(2, RoundingMode.HALF_UP).toDouble()
}