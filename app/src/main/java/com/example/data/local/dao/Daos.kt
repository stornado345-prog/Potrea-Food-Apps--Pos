package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.data.local.entity.CartItemEntity
import com.example.data.local.entity.CategoryEntity
import com.example.data.local.entity.OrderEntity
import com.example.data.local.entity.OrderItemEntity
import com.example.data.local.entity.ProductEntity
import com.example.data.local.entity.RestaurantTableEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products ORDER BY id ASC")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE categoryId = :categoryId ORDER BY id ASC")
    fun getProductsByCategory(categoryId: String): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE id = :id LIMIT 1")
    fun getProductById(id: Long): Flow<ProductEntity?>

    @Query("SELECT COUNT(*) FROM products")
    suspend fun getProductCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<ProductEntity>)

    @Query("UPDATE products SET price = :price WHERE id = :id")
    suspend fun updateProductPrice(id: Long, price: Double)

    @Query("DELETE FROM products")
    suspend fun deleteAllProducts()
}

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories ORDER BY sortOrder ASC")
    fun getAllCategories(): Flow<List<CategoryEntity>>

    @Query("SELECT COUNT(*) FROM categories")
    suspend fun getCategoryCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<CategoryEntity>)

    @Query("DELETE FROM categories")
    suspend fun deleteAllCategories()
}

@Dao
interface CartDao {
    @Query("SELECT * FROM cart_items ORDER BY id DESC")
    fun getAllCartItems(): Flow<List<CartItemEntity>>

    @Query("SELECT COUNT(*) FROM cart_items")
    fun getCartCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(item: CartItemEntity): Long

    @Update
    suspend fun updateCartItem(item: CartItemEntity)

    @Query("DELETE FROM cart_items WHERE id = :id")
    suspend fun deleteCartItem(id: Long)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()
}

@Dao
interface OrderDao {
    @Query("SELECT * FROM orders ORDER BY createdAt DESC")
    fun getAllOrders(): Flow<List<OrderEntity>>

    @Query("SELECT * FROM orders WHERE id = :orderId LIMIT 1")
    fun getOrderById(orderId: Long): Flow<OrderEntity?>

    @Query("SELECT * FROM orders ORDER BY id DESC LIMIT 1")
    fun getLatestOrder(): Flow<OrderEntity?>

    @Query("SELECT COUNT(*) FROM orders")
    suspend fun getOrderCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity): Long

    @Query("UPDATE orders SET status = :status, paymentStatus = :paymentStatus WHERE id = :orderId")
    suspend fun updateOrderStatus(orderId: Long, status: String, paymentStatus: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderItems(items: List<OrderItemEntity>)

    @Query("SELECT * FROM order_items WHERE orderId = :orderId")
    fun getOrderItems(orderId: Long): Flow<List<OrderItemEntity>>
}

@Dao
interface TableDao {
    @Query("SELECT * FROM restaurant_tables ORDER BY id ASC")
    fun getAllTables(): Flow<List<RestaurantTableEntity>>

    @Query("SELECT * FROM restaurant_tables WHERE isSelected = 1 LIMIT 1")
    fun getSelectedTable(): Flow<RestaurantTableEntity?>

    @Query("SELECT COUNT(*) FROM restaurant_tables")
    suspend fun getTableCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTables(tables: List<RestaurantTableEntity>)

    @Query("UPDATE restaurant_tables SET isSelected = 0")
    suspend fun deselectAllTables()

    @Query("UPDATE restaurant_tables SET isSelected = 1 WHERE id = :tableId")
    suspend fun selectTable(tableId: String)

    @Transaction
    suspend fun updateSelectedTable(tableId: String) {
        deselectAllTables()
        selectTable(tableId)
    }
}
