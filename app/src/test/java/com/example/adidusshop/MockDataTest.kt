package com.example.adidusshop

import com.example.adidusshop.services.MockData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MockDataTest {

    @Test
    fun sampleProducts_isNotEmpty() {
        assertFalse(MockData.sampleProducts.isEmpty())
        assertEquals(6, MockData.sampleProducts.size)
    }

    @Test
    fun sampleProducts_haveVariants() {
        MockData.sampleProducts.forEach { product ->
            assertTrue("Product ${product.name} must have variants", product.variants?.isNotEmpty() == true)
        }
    }

    @Test
    fun mockCartItems_canCalculateTotal() {
        val total = MockData.mockCartItems.sumOf { it.price * it.quantity }
        assertTrue("Total must be positive", total > 0)
    }
}
