package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.domain.model.CartItem
import com.example.domain.model.Category
import com.example.domain.model.Order
import com.example.domain.model.OrderItem
import com.example.domain.model.OrderStatus
import com.example.domain.model.PaymentStatus
import com.example.domain.model.Product
import com.example.domain.model.RestaurantTable

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val categoryId: String,
    val description: String,
    val price: Double,
    val imageRes: String,
    val isAvailable: Boolean = true,
    val badge: String? = null,
    val calories: Int = 0
) {
    fun toDomain(): Product = Product(
        id = id,
        name = name,
        categoryId = categoryId,
        description = description,
        price = price,
        imageRes = imageRes,
        isAvailable = isAvailable,
        badge = badge,
        calories = calories
    )

    companion object {
        fun fromDomain(p: Product): ProductEntity = ProductEntity(
            id = p.id,
            name = p.name,
            categoryId = p.categoryId,
            description = p.description,
            price = p.price,
            imageRes = p.imageRes,
            isAvailable = p.isAvailable,
            badge = p.badge,
            calories = p.calories
        )
    }
}

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey val id: String,
    val name: String,
    val iconName: String,
    val sortOrder: Int
) {
    fun toDomain(): Category = Category(
        id = id,
        name = name,
        iconName = iconName,
        sortOrder = sortOrder
    )

    companion object {
        fun fromDomain(c: Category): CategoryEntity = CategoryEntity(
            id = c.id,
            name = c.name,
            iconName = c.iconName,
            sortOrder = c.sortOrder
        )
    }
}

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val productId: Long,
    val productName: String,
    val productPrice: Double,
    val productImage: String,
    val quantity: Int = 1,
    val customizations: String = "",
    val customizationPrice: Double = 0.0,
    val itemNotes: String = ""
) {
    fun toDomain(): CartItem = CartItem(
        id = id,
        productId = productId,
        productName = productName,
        productPrice = productPrice,
        productImage = productImage,
        quantity = quantity,
        customizations = customizations,
        customizationPrice = customizationPrice,
        itemNotes = itemNotes
    )

    companion object {
        fun fromDomain(item: CartItem): CartItemEntity = CartItemEntity(
            id = item.id,
            productId = item.productId,
            productName = item.productName,
            productPrice = item.productPrice,
            productImage = item.productImage,
            quantity = item.quantity,
            customizations = item.customizations,
            customizationPrice = item.customizationPrice,
            itemNotes = item.itemNotes
        )
    }
}

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val orderNumber: String,
    val tableNumber: String,
    val status: String,
    val paymentStatus: String,
    val subtotal: Double,
    val tax: Double,
    val total: Double,
    val createdAt: Long = System.currentTimeMillis()
) {
    fun toDomain(items: List<OrderItem> = emptyList()): Order = Order(
        id = id,
        orderNumber = orderNumber,
        tableNumber = tableNumber,
        status = runCatching { OrderStatus.valueOf(status) }.getOrDefault(OrderStatus.PENDING_PAYMENT),
        paymentStatus = runCatching { PaymentStatus.valueOf(paymentStatus) }.getOrDefault(PaymentStatus.UNPAID),
        subtotal = subtotal,
        tax = tax,
        total = total,
        createdAt = createdAt,
        items = items
    )

    companion object {
        fun fromDomain(order: Order): OrderEntity = OrderEntity(
            id = order.id,
            orderNumber = order.orderNumber,
            tableNumber = order.tableNumber,
            status = order.status.name,
            paymentStatus = order.paymentStatus.name,
            subtotal = order.subtotal,
            tax = order.tax,
            total = order.total,
            createdAt = order.createdAt
        )
    }
}

@Entity(tableName = "order_items")
data class OrderItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val orderId: Long,
    val productId: Long,
    val productName: String,
    val unitPrice: Double,
    val quantity: Int,
    val totalPrice: Double,
    val customizations: String = ""
) {
    fun toDomain(): OrderItem = OrderItem(
        id = id,
        orderId = orderId,
        productId = productId,
        productName = productName,
        unitPrice = unitPrice,
        quantity = quantity,
        totalPrice = totalPrice,
        customizations = customizations
    )

    companion object {
        fun fromDomain(item: OrderItem, orderId: Long): OrderItemEntity = OrderItemEntity(
            id = item.id,
            orderId = orderId,
            productId = item.productId,
            productName = item.productName,
            unitPrice = item.unitPrice,
            quantity = item.quantity,
            totalPrice = item.totalPrice,
            customizations = item.customizations
        )
    }
}

@Entity(tableName = "restaurant_tables")
data class RestaurantTableEntity(
    @PrimaryKey val id: String,
    val label: String,
    val isSelected: Boolean = false
) {
    fun toDomain(): RestaurantTable = RestaurantTable(
        id = id,
        label = label,
        isSelected = isSelected
    )

    companion object {
        fun fromDomain(t: RestaurantTable): RestaurantTableEntity = RestaurantTableEntity(
            id = t.id,
            label = t.label,
            isSelected = t.isSelected
        )
    }
}
