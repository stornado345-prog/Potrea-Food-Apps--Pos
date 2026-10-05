package com.example.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.database.PoetraFoodDatabase
import com.example.data.repository.PoetraFoodRepository
import com.example.domain.model.CartItem
import com.example.domain.model.Category
import com.example.domain.model.Order
import com.example.domain.model.OrderStatus
import com.example.domain.model.PaymentStatus
import com.example.domain.model.Product
import com.example.domain.model.RestaurantTable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class KioskUiState(
    val categories: List<Category> = emptyList(),
    val selectedCategoryId: String = "all",
    val searchQuery: String = "",
    val products: List<Product> = emptyList(),
    val cartItems: List<CartItem> = emptyList(),
    val subtotal: Double = 0.0,
    val tax: Double = 0.0,
    val total: Double = 0.0,
    val selectedTable: RestaurantTable = RestaurantTable("table_01", "Table 01", true),
    val allTables: List<RestaurantTable> = emptyList(),
    val selectedProductForDetail: Product? = null,
    val isTableDialogOpen: Boolean = false,
    val isCartDrawerOpen: Boolean = false,
    val userMessage: String? = null
)

class KioskViewModel(application: Application) : AndroidViewModel(application) {

    private val database = PoetraFoodDatabase.getInstance(application)
    private val repository = PoetraFoodRepository(
        productDao = database.productDao(),
        categoryDao = database.categoryDao(),
        cartDao = database.cartDao(),
        orderDao = database.orderDao(),
        tableDao = database.tableDao()
    )

    private val _selectedCategoryId = MutableStateFlow("all")
    val selectedCategoryId = _selectedCategoryId.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedProductForDetail = MutableStateFlow<Product?>(null)
    val selectedProductForDetail = _selectedProductForDetail.asStateFlow()

    private val _isTableDialogOpen = MutableStateFlow(false)
    val isTableDialogOpen = _isTableDialogOpen.asStateFlow()

    private val _isCartDrawerOpen = MutableStateFlow(false)
    val isCartDrawerOpen = _isCartDrawerOpen.asStateFlow()

    private val _userMessage = MutableStateFlow<String?>(null)
    val userMessage = _userMessage.asStateFlow()

    init {
        viewModelScope.launch {
            repository.initializeDatabase()
        }
    }

    val categories: StateFlow<List<Category>> = repository.categories
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allTables: StateFlow<List<RestaurantTable>> = repository.tables
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val selectedTable: StateFlow<RestaurantTable> = repository.selectedTable
        .combine(repository.tables) { selected, all ->
            selected ?: all.firstOrNull { it.isSelected } ?: RestaurantTable("table_01", "Table 01", true)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            RestaurantTable("table_01", "Table 01", true)
        )

    val cartItems: StateFlow<List<CartItem>> = repository.cartItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val orders: StateFlow<List<Order>> = repository.orders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredProducts: StateFlow<List<Product>> = combine(
        repository.allProducts,
        _selectedCategoryId,
        _searchQuery
    ) { products, categoryId, query ->
        products.filter { product ->
            val matchesCategory = (categoryId == "all" || product.categoryId.equals(categoryId, ignoreCase = true))
            val matchesQuery = query.isBlank() ||
                    product.name.contains(query, ignoreCase = true) ||
                    product.description.contains(query, ignoreCase = true)
            matchesCategory && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selectCategory(categoryId: String) {
        _selectedCategoryId.value = categoryId
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun openProductDetail(product: Product) {
        _selectedProductForDetail.value = product
    }

    fun closeProductDetail() {
        _selectedProductForDetail.value = null
    }

    fun setTableDialogOpen(open: Boolean) {
        _isTableDialogOpen.value = open
    }

    fun setCartDrawerOpen(open: Boolean) {
        _isCartDrawerOpen.value = open
    }

    fun selectTable(tableId: String) {
        viewModelScope.launch {
            repository.setSelectedTable(tableId)
            _isTableDialogOpen.value = false
            showNotification("Location updated")
        }
    }

    fun addToCart(
        product: Product,
        quantity: Int,
        customizations: String = "",
        customizationPrice: Double = 0.0,
        itemNotes: String = ""
    ) {
        viewModelScope.launch {
            repository.addToCart(
                product = product,
                quantity = quantity,
                customizations = customizations,
                customizationPrice = customizationPrice,
                itemNotes = itemNotes
            )
            showNotification("Added ${product.name} to order")
        }
    }

    fun updateCartQuantity(cartItemId: Long, newQuantity: Int) {
        viewModelScope.launch {
            repository.updateCartQuantity(cartItemId, newQuantity)
        }
    }

    fun removeCartItem(cartItemId: Long) {
        viewModelScope.launch {
            repository.removeCartItem(cartItemId)
            showNotification("Item removed from order")
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            repository.clearCart()
            showNotification("Order cleared")
        }
    }

    suspend fun placeOrder(): Order {
        val currentTable = selectedTable.value.label
        val order = repository.createOrder(currentTable)
        showNotification("Order ${order.orderNumber} placed!")
        return order
    }

    fun getOrderFlow(orderId: Long) = repository.getOrderById(orderId)

    fun simulateStatusAdvance(orderId: Long, currentStatus: OrderStatus) {
        viewModelScope.launch {
            val (nextStatus, nextPayment) = when (currentStatus) {
                OrderStatus.PENDING_PAYMENT -> Pair(OrderStatus.PAID, PaymentStatus.PAID)
                OrderStatus.PAID -> Pair(OrderStatus.PREPARING, PaymentStatus.PAID)
                OrderStatus.PREPARING -> Pair(OrderStatus.READY, PaymentStatus.PAID)
                OrderStatus.READY -> Pair(OrderStatus.COMPLETED, PaymentStatus.PAID)
                OrderStatus.COMPLETED -> Pair(OrderStatus.COMPLETED, PaymentStatus.PAID)
                OrderStatus.CANCELLED -> Pair(OrderStatus.CANCELLED, PaymentStatus.UNPAID)
            }
            repository.updateOrderStatus(orderId, nextStatus, nextPayment)
            showNotification("Status updated to ${nextStatus.label}")
        }
    }

    fun updateProductPrice(productId: Long, newPrice: Double) {
        viewModelScope.launch {
            repository.updateProductPrice(productId, newPrice)
            showNotification("Product price updated")
        }
    }

    fun clearUserMessage() {
        _userMessage.value = null
    }

    private fun showNotification(message: String) {
        _userMessage.value = message
    }
}
