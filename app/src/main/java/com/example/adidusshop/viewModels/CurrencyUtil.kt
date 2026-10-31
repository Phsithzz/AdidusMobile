package com.example.adidusshop.viewModels

import java.text.NumberFormat
import java.util.Locale

object CurrencyUtil {
    fun baht(amount: Int): String {
        val formatter = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("th-TH"))
        formatter.maximumFractionDigits = 0
        return formatter.format(amount)
    }
}
