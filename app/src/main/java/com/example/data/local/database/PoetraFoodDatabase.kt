package com.example.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.CartDao
import com.example.data.local.dao.CategoryDao
import com.example.data.local.dao.OrderDao
import com.example.data.local.dao.ProductDao
import com.example.data.local.dao.TableDao
import com.example.data.local.entity.CartItemEntity
import com.example.data.local.entity.CategoryEntity
import com.example.data.local.entity.OrderEntity
import com.example.data.local.entity.OrderItemEntity
import com.example.data.local.entity.ProductEntity
import com.example.data.local.entity.RestaurantTableEntity

@Database(
    entities = [
        ProductEntity::class,
        CategoryEntity::class,
        CartItemEntity::class,
        OrderEntity::class,
        OrderItemEntity::class,
        RestaurantTableEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class PoetraFoodDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun categoryDao(): CategoryDao
    abstract fun cartDao(): CartDao
    abstract fun orderDao(): OrderDao
    abstract fun tableDao(): TableDao

    companion object {
        @Volatile
        private var INSTANCE: PoetraFoodDatabase? = null

        fun getInstance(context: Context): PoetraFoodDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PoetraFoodDatabase::class.java,
                    "poetra_food_database.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
