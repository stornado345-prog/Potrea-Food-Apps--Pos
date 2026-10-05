package com.example.data.repository

import com.example.data.local.DemoDataSeeder
import com.example.data.local.dao.CartDao
import com.example.data.local.dao.CategoryDao
import com.example.data.local.dao.OrderDao
import com.example.data.local.dao.ProductDao
import com.example.data.local.dao.TableDao
import com.example.data.local.entity.CartItemEntity
import com.example.data.local.entity.OrderEntity
import com.example.data.local.entity.OrderItemEntity
import com.example.domain.model.CartItem
import com.example.domain.model.Category
import com.example.domain.model.Order
import com.example.domain.model.OrderStatus
import com.example.domain.model.PaymentStatus
import com.example.domain.model.Product
import com.example.domain.model.RestaurantTable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class PoetraFoodRepository(
    private val productDao: ProductDao,
    private val categoryDao: CategoryDao,
    private val cartDao: CartDao,
    private val orderDao: OrderDao,
    private val tableDao: TableDao
) {

    suspend fun initializeDatabase() {
        DemoDataSeeder.seedDatabaseIfEmpty(productDao, categoryDao, tableDao)
    }

    // Categories
    val categories: Flow<List<Category>> = categoryDao.getAllCategories().map { list ->
        list.map { it.toDomain() }
    }

    // Products
    val allProducts: Flow<List<Product>> = productDao.getAllProducts().map { list ->
        list.map { it.toDomain() }
    }

    fun getProductsByCategory(categoryId: String): Flow<List<Product>> {
        return if (categoryId == "all") {
            allProducts
        } else {
            productDao.getProductsByCategory(categoryId).map { list ->
                list.map { it.toDomain() }
            }
        }
    }

    fun getProductById(id: Long): Flow<Product?> {
        return productDao.getProductById(id).map { it?.toDomain() }
    }

    suspend fun updateProductPrice(productId: Long, newPrice: Double) {
        productDao.updateProductPrice(productId, newPrice)
    }

    // Cart
    val cartItems: Flow<List<CartItem>> = cartDao.getAllCartItems().map { list ->
        list.map { it.toDomain() }
    }

    val cartCount: Flow<Int> = cartDao.getCartCount()

    suspend fun addToCart(
        product: Product,
        quantity: Int,
        customizations: String = "",
        customizationPrice: Double = 0.0,
        itemNotes: String = ""
    ) {
        val currentItems = cartDao.getAllCartItems().first()
        val existing = currentItems.find {
            it.productId == product.id &&
                    it.customizations == customizations &&
                    it.itemNotes == itemNotes
        }

        if (existing != null) {
            cartDao.updateCartItem(existing.copy(quantity = existing.quantity + quantity))
        } else {
            cartDao.insertCartItem(
                CartItemEntity(
                    productId = product.id,
                    productName = product.name,
                    productPrice = product.price,
                    productImage = product.imageRes,
                    quantity = quantity,
                    customizations = customizations,
                    customizationPrice = customizationPrice,
                    itemNotes = itemNotes
                )
            )
        }
    }

    suspend fun updateCartQuantity(cartItemId: Long, newQuantity: Int) {
        if (newQuantity <= 0) {
            cartDao.deleteCartItem(cartItemId)
        } else {
            val currentItems = cartDao.getAllCartItems().first()
            val item = currentItems.find { it.id == cartItemId }
            if (item != null) {
                cartDao.updateCartItem(item.copy(quantity = newQuantity))
            }
        }
    }

    suspend fun removeCartItem(cartItemId: Long) {
        cartDao.deleteCartItem(cartItemId)
    }

    suspend fun clearCart() {
        cartDao.clearCart()
    }

    // Tables
    val tables: Flow<List<RestaurantTable>> = tableDao.getAllTables().map { list ->
        list.map { it.toDomain() }
    }

    val selectedTable: Flow<RestaurantTable?> = tableDao.getSelectedTable().map { it?.toDomain() }

    suspend fun setSelectedTable(tableId: String) {
        tableDao.updateSelectedTable(tableId)
    }

    // Orders
    val orders: Flow<List<Order>> = orderDao.getAllOrders().map { orderEntities ->
        orderEntities.map { it.toDomain() }
    }

    fun getOrderById(orderId: Long): Flow<Order?> {
        return combine(
            orderDao.getOrderById(orderId),
            orderDao.getOrderItems(orderId)
        ) { orderEntity, itemEntities ->
            orderEntity?.toDomain(items = itemEntities.map { it.toDomain() })
        }
    }

    suspend fun createOrder(tableNumber: String): Order {
        val itemsInCart = cartDao.getAllCartItems().first()
        if (itemsInCart.isEmpty()) {
            throw IllegalStateException("Cart is empty!")
        }

        val count = orderDao.getOrderCount() + 1
        val orderNumber = String.format("PF-%03d", count)

        var subtotal = 0.0
        for (item in itemsInCart) {
            val unitPrice = item.productPrice + item.customizationPrice
            subtotal += unitPrice * item.quantity
        }
        val tax = subtotal * 0.10 // 10% PB1 Restaurant Tax
        val total = subtotal + tax

        val orderEntity = OrderEntity(
            orderNumber = orderNumber,
            tableNumber = tableNumber,
            status = OrderStatus.PENDING_PAYMENT.name,
            paymentStatus = PaymentStatus.UNPAID.name,
            subtotal = subtotal,
            tax = tax,
            total = total,
            createdAt = System.currentTimeMillis()
        )

        val newOrderId = orderDao.insertOrder(orderEntity)

        val orderItemEntities = itemsInCart.map { c ->
            val unitPrice = c.productPrice + c.customizationPrice
            val itemTotal = unitPrice * c.quantity
            OrderItemEntity(
                orderId = newOrderId,
                productId = c.productId,
                productName = c.productName,
                unitPrice = unitPrice,
                quantity = c.quantity,
                totalPrice = itemTotal,
                customizations = c.customizations
            )
        }
        orderDao.insertOrderItems(orderItemEntities)

        // Clear cart after successfully creating order
        cartDao.clearCart()

        return orderEntity.copy(id = newOrderId).toDomain(
            items = orderItemEntities.map { it.toDomain() }
        )
    }

    suspend fun updateOrderStatus(orderId: Long, status: OrderStatus, paymentStatus: PaymentStatus) {
        orderDao.updateOrderStatus(orderId, status.name, paymentStatus.name)
    }
}
