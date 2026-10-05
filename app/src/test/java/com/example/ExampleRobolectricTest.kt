package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.domain.model.CartItem
import com.example.domain.model.OrderStatus
import com.example.domain.model.PaymentStatus
import com.example.domain.model.formatRupiah
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read app_name string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Poetra Food", appName)
    }

    @Test
    fun `test rupiah currency formatting`() {
        val formatted = formatRupiah(25000.0)
        assertTrue(formatted.contains("25.000"))
    }

    @Test
    fun `test cart item calculation`() {
        val item = CartItem(
            id = 1,
            productId = 1,
            productName = "Poetra Burger",
            productPrice = 25000.0,
            productImage = "ic_category_burger",
            quantity = 2,
            customizationPrice = 4000.0
        )
        // Unit price = 25.000 + 4.000 = 29.000, Total for 2 = 58.000
        assertEquals(29000.0, item.unitPriceWithCustomizations, 0.001)
        assertEquals(58000.0, item.totalPrice, 0.001)
    }

    @Test
    fun `test order status labels`() {
        assertEquals("Waiting for Payment", OrderStatus.PENDING_PAYMENT.label)
        assertEquals("Paid", OrderStatus.PAID.label)
        assertEquals("Preparing", OrderStatus.PREPARING.label)
        assertEquals("Ready for Pickup", OrderStatus.READY.label)
        assertEquals("Unpaid", PaymentStatus.UNPAID.label)
    }
}
