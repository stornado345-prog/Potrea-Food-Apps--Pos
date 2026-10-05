package com.example.data.local

import com.example.data.local.dao.CategoryDao
import com.example.data.local.dao.ProductDao
import com.example.data.local.dao.TableDao
import com.example.data.local.entity.CategoryEntity
import com.example.data.local.entity.ProductEntity
import com.example.data.local.entity.RestaurantTableEntity

object DemoDataSeeder {

    suspend fun seedDatabaseIfEmpty(
        productDao: ProductDao,
        categoryDao: CategoryDao,
        tableDao: TableDao
    ) {
        if (categoryDao.getCategoryCount() == 0) {
            val categories = listOf(
                CategoryEntity("all", "All Menu", "ic_category_all", 0),
                CategoryEntity("burgers", "Burgers", "ic_category_burger", 1),
                CategoryEntity("chicken", "Crispy Chicken", "ic_category_chicken", 2),
                CategoryEntity("rice", "Rice Bowls", "ic_category_rice", 3),
                CategoryEntity("snacks", "Sides & Snacks", "ic_category_snack", 4),
                CategoryEntity("drinks", "Beverages", "ic_category_drink", 5),
                CategoryEntity("combos", "Value Combos", "ic_category_combo", 6)
            )
            categoryDao.insertCategories(categories)
        }

        if (productDao.getProductCount() == 0) {
            val products = listOf(
                ProductEntity(
                    id = 1,
                    name = "Poetra Burger",
                    categoryId = "burgers",
                    description = "Signature juicy beef patty, melted cheddar, crisp lettuce, tomato, and secret Poetra sauce in a toasted sesame bun.",
                    price = 25000.0,
                    imageRes = "ic_poetra_burger",
                    isAvailable = true,
                    badge = "CHEF'S PICK",
                    calories = 540
                ),
                ProductEntity(
                    id = 2,
                    name = "Cheese Burger",
                    categoryId = "burgers",
                    description = "Juicy grilled beef patty layered with double American cheddar, crunchy pickles, ketchup, and mustard.",
                    price = 28000.0,
                    imageRes = "ic_cheese_burger",
                    isAvailable = true,
                    badge = "POPULAR",
                    calories = 590
                ),
                ProductEntity(
                    id = 3,
                    name = "Crispy Chicken Burger",
                    categoryId = "burgers",
                    description = "Crispy golden breaded chicken fillet with creamy mayonnaise and fresh shredded cabbage on a soft bun.",
                    price = 26000.0,
                    imageRes = "ic_crispy_chicken_burger",
                    isAvailable = true,
                    badge = "CRISPY",
                    calories = 510
                ),
                ProductEntity(
                    id = 4,
                    name = "Double Beef Burger",
                    categoryId = "burgers",
                    description = "Two 100% Australian beef patties with double melted cheese, sweet caramelized onions, and smoky barbecue sauce.",
                    price = 38000.0,
                    imageRes = "ic_double_beef_burger",
                    isAvailable = true,
                    badge = "BEST SELLER",
                    calories = 780
                ),
                ProductEntity(
                    id = 5,
                    name = "Crispy Chicken",
                    categoryId = "chicken",
                    description = "Original recipe crispy chicken piece fried to golden perfection with an irresistible savory crunch.",
                    price = 22000.0,
                    imageRes = "ic_crispy_chiken",
                    isAvailable = true,
                    badge = "CRUNCHY",
                    calories = 380
                ),
                ProductEntity(
                    id = 6,
                    name = "Spicy Chicken",
                    categoryId = "chicken",
                    description = "Crispy chicken marinated in spicy chili peppers and tossed in hot red seasoning for heat lovers.",
                    price = 24000.0,
                    imageRes = "ic_spicy_chiken",
                    isAvailable = true,
                    badge = "SPICY",
                    calories = 410
                ),
                ProductEntity(
                    id = 7,
                    name = "Chicken Wings",
                    categoryId = "chicken",
                    description = "4 pieces of crispy fried chicken wings glazed with savory honey garlic soy sauce and sesame seeds.",
                    price = 27000.0,
                    imageRes = "ic_chiken_winggs",
                    isAvailable = true,
                    badge = "NEW",
                    calories = 450
                ),
                ProductEntity(
                    id = 8,
                    name = "Nasi Goreng Spesial",
                    categoryId = "rice",
                    description = "Nasi goreng lezat dengan telur mata sapi dan ayam suwir khas Poetra Food.",
                    price = 28000.0,
                    imageRes = "ic_nasi_goreng_spesial",
                    isAvailable = true,
                    badge = "FAVORITE",
                    calories = 520
                ),
                ProductEntity(
                    id = 9,
                    name = "Crispy Chicken Rice",
                    categoryId = "rice",
                    description = "Fragrant butter garlic rice paired with chopped boneless crispy chicken and sweet chili dip.",
                    price = 28000.0,
                    imageRes = "ic_crispy_chicken_rice",
                    isAvailable = true,
                    badge = null,
                    calories = 560
                ),
                ProductEntity(
                    id = 10,
                    name = "Spicy Chicken Rice",
                    categoryId = "rice",
                    description = "Hot spicy fried chicken served over warm jasmine rice with authentic spicy sambal and sliced cucumbers.",
                    price = 29000.0,
                    imageRes = "ic_spicy_chiken_rice",
                    isAvailable = true,
                    badge = "HOT",
                    calories = 580
                ),
                ProductEntity(
                    id = 11,
                    name = "French Fries",
                    categoryId = "snacks",
                    description = "Golden shoestring potatoes fried to crispy perfection and lightly dusted with sea salt.",
                    price = 15000.0,
                    imageRes = "ic_freenfreice",
                    isAvailable = true,
                    badge = "CLASSIC",
                    calories = 320
                ),
                ProductEntity(
                    id = 12,
                    name = "Chicken Nuggets",
                    categoryId = "snacks",
                    description = "6 pieces of tender, juicy all-white meat nuggets served with tangy barbecue dip.",
                    price = 18000.0,
                    imageRes = "ic_chiken_nuget",
                    isAvailable = true,
                    badge = null,
                    calories = 280
                ),
                ProductEntity(
                    id = 13,
                    name = "Onion Rings",
                    categoryId = "snacks",
                    description = "Sweet yellow onion rings dipped in seasoned batter and deep fried until crunchy.",
                    price = 16000.0,
                    imageRes = "ic_onion_rings",
                    isAvailable = true,
                    badge = null,
                    calories = 290
                ),
                ProductEntity(
                    id = 14,
                    name = "Cola",
                    categoryId = "drinks",
                    description = "Classic chilled sparkling cola served over crystal ice cubes for ultimate refreshment.",
                    price = 10000.0,
                    imageRes = "ic_cola",
                    isAvailable = true,
                    badge = null,
                    calories = 140
                ),
                ProductEntity(
                    id = 15,
                    name = "Orange Drink",
                    categoryId = "drinks",
                    description = "Refreshing chilled citrus orange drink bursting with sweet, fruity zest.",
                    price = 12000.0,
                    imageRes = "ic_orange_drink",
                    isAvailable = true,
                    badge = null,
                    calories = 120
                ),
                ProductEntity(
                    id = 16,
                    name = "Lemon Tea",
                    categoryId = "drinks",
                    description = "Freshly brewed iced black tea with a bright splash of natural lemon juice.",
                    price = 10000.0,
                    imageRes = "ic_lemon_drink",
                    isAvailable = true,
                    badge = "POPULAR",
                    calories = 90
                ),
                ProductEntity(
                    id = 17,
                    name = "Mineral Water",
                    categoryId = "drinks",
                    description = "Pure natural bottled spring mineral water (600 ml).",
                    price = 6000.0,
                    imageRes = "ic_mineral_water",
                    isAvailable = true,
                    badge = null,
                    calories = 0
                ),
                ProductEntity(
                    id = 18,
                    name = "Poetra Combo Meal",
                    categoryId = "combos",
                    description = "Solo power combo: 1 Poetra Burger + 1 Regular Salted French Fries + 1 Ice Cold Cola.",
                    price = 45000.0,
                    imageRes = "ic_category_combo",
                    isAvailable = true,
                    badge = "BEST VALUE",
                    calories = 1000
                ),
                ProductEntity(
                    id = 19,
                    name = "Family Feast Combo",
                    categoryId = "combos",
                    description = "Perfect group sharing: 2 Crispy Chicken + 1 Poetra Burger + 2 Rice + 2 French Fries + 3 Lemon Teas.",
                    price = 85000.0,
                    imageRes = "ic_category_combo",
                    isAvailable = true,
                    badge = "SUPER SAVER",
                    calories = 1950
                )
            )
            productDao.insertProducts(products)
        }

        if (tableDao.getTableCount() == 0) {
            val tables = listOf(
                RestaurantTableEntity("table_01", "Table 01", isSelected = true),
                RestaurantTableEntity("table_02", "Table 02", isSelected = false),
                RestaurantTableEntity("table_03", "Table 03", isSelected = false),
                RestaurantTableEntity("table_04", "Table 04", isSelected = false),
                RestaurantTableEntity("standing_01", "Standing 01", isSelected = false),
                RestaurantTableEntity("standing_02", "Standing 02", isSelected = false),
                RestaurantTableEntity("takeaway_01", "Takeaway Kiosk", isSelected = false)
            )
            tableDao.insertTables(tables)
        }
    }
}
