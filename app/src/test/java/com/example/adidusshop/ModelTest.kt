package com.example.adidusshop

import com.example.adidusshop.models.CartItem
import com.example.adidusshop.models.Product
import com.example.adidusshop.models.ProductVariant
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class ModelTest {

    @Test
    fun productModel_initialization_isCorrect() {
        val variant = ProductVariant(
            variant_id = 10,
            product_id = 1,
            stock_quantity = 5,
            price = 3800,
            size = "8.5 UK",
            color = "White"
        )

        val product = Product(
            product_id = 1,
            name = "Adidas Samba",
            description = "Sneaker",
            price = 3800,
            image_filename = "lifestyle1",
            brand = "Adidas",
            variants = listOf(variant)
        )

        assertEquals(1, product.product_id)
        assertEquals("Adidas Samba", product.name)
        assertEquals(3800, product.price)
        assertNotNull(product.variants)
        assertEquals(1, product.variants?.size)
        assertEquals("8.5 UK", product.variants?.first()?.size)
    }

    @Test
    fun cartItem_subtotal_calculation_isCorrect() {
        val item = CartItem(
            cart_id = 1,
            quantity = 3,
            price = 2200,
            product_id = 5,
            name = "Adilette 22",
            description = "FlipFlops",
            image_filename = "slipper1",
            size = "9.0 UK",
            stock_quantity = 10
        )

        val subtotal = item.price * item.quantity
        assertEquals(6600, subtotal)
    }
}
