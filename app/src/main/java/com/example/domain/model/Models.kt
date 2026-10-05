package com.example.domain.model

import java.text.NumberFormat
import java.util.Locale

data class Product(
    val id: Long = 0,
    val name: String,
    val categoryId: String,
    val description: String,
    val price: Double,
    val imageRes: String,
    val isAvailable: Boolean = true,
    val badge: String? = null,
    val calories: Int = 0
) {
    val formattedPrice: String
        get() = formatRupiah(price)
}

data class Category(
    val id: String,
    val name: String,
    val iconName: String,
    val sortOrder: Int
)

data class CartItem(
    val id: Long = 0,
    val productId: Long,
    val productName: String,
    val productPrice: Double,
    val productImage: String,
    val quantity: Int = 1,
    val customizations: String = "",
    val customizationPrice: Double = 0.0,
    val itemNotes: String = ""
) {
    val unitPriceWithCustomizations: Double
        get() = productPrice + customizationPrice

    val totalPrice: Double
        get() = unitPriceWithCustomizations * quantity

    val formattedTotalPrice: String
        get() = formatRupiah(totalPrice)
}

enum class OrderStatus(val label: String) {
    PENDING_PAYMENT("Waiting for Payment"),
    PAID("Paid"),
    PREPARING("Preparing"),
    READY("Ready for Pickup"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled")
}

enum class PaymentStatus(val label: String) {
    UNPAID("Unpaid"),
    PAID("Paid")
}

data class Order(
    val id: Long = 0,
    val orderNumber: String,
    val tableNumber: String,
    val status: OrderStatus = OrderStatus.PENDING_PAYMENT,
    val paymentStatus: PaymentStatus = PaymentStatus.UNPAID,
    val subtotal: Double,
    val tax: Double,
    val total: Double,
    val createdAt: Long = System.currentTimeMillis(),
    val items: List<OrderItem> = emptyList()
) {
    val formattedSubtotal: String
        get() = formatRupiah(subtotal)
    val formattedTax: String
        get() = formatRupiah(tax)
    val formattedTotal: String
        get() = formatRupiah(total)
}

data class OrderItem(
    val id: Long = 0,
    val orderId: Long = 0,
    val productId: Long,
    val productName: String,
    val unitPrice: Double,
    val quantity: Int,
    val totalPrice: Double,
    val customizations: String = ""
) {
    val formattedTotalPrice: String
        get() = formatRupiah(totalPrice)
}

data class RestaurantTable(
    val id: String,
    val label: String,
    val isSelected: Boolean = false
)

data class CustomizationOption(
    val id: String,
    val name: String,
    val extraPrice: Double = 0.0,
    val isSelected: Boolean = false
)

fun formatRupiah(amount: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    format.maximumFractionDigits = 0
    return format.format(amount).replace("Rp", "Rp ")
}
