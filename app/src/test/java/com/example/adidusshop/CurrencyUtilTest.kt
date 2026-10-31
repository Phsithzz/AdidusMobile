package com.example.adidusshop

import com.example.adidusshop.viewModels.CurrencyUtil
import org.junit.Assert.assertTrue
import org.junit.Test

class CurrencyUtilTest {

    @Test
    fun baht_formatsCorrectly() {
        val formatted = CurrencyUtil.baht(3800)
        // Checks that formatted contains 3,800
        assertTrue("Formatted currency should contain 3,800: $formatted", formatted.contains("3,800"))
    }

    @Test
    fun baht_zeroAmount_formatsCorrectly() {
        val formatted = CurrencyUtil.baht(0)
        assertTrue("Formatted currency should contain 0: $formatted", formatted.contains("0"))
    }

    @Test
    fun baht_largeAmount_formatsCorrectly() {
        val formatted = CurrencyUtil.baht(125000)
        assertTrue("Formatted currency should contain 125,000: $formatted", formatted.contains("125,000"))
    }
}
