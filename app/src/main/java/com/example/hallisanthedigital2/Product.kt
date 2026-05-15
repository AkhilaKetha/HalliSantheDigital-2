package com.example.hallisanthedigital2

import androidx.room.*

// 1. Existing Product Table
@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val price: String,
    val imagePath: String,
    val category: String
)

// 2. NEW: Order Table (The Cart)
@Entity(tableName = "orders")
data class Order(
    @PrimaryKey(autoGenerate = true) val orderId: Int = 0,
    val productName: String,
    val productPrice: String,
    val productImage: String
)

@Dao
interface ProductDao {
    @Query("SELECT * FROM products")
    suspend fun getAll(): List<Product>

    @Query("SELECT * FROM products WHERE category = :cat")
    suspend fun getByCategory(cat: String): List<Product>

    @Query("SELECT * FROM products WHERE name LIKE :query")
    suspend fun search(query: String): List<Product>

    @Insert
    suspend fun insert(product: Product)

    // --- NEW ORDER COMMANDS ---
    @Insert
    suspend fun addOrder(order: Order)

    @Query("SELECT * FROM orders")
    suspend fun getAllOrders(): List<Order>

    @Query("DELETE FROM orders")
    suspend fun clearOrders()
}

// Update version to 3 because we added a new table
@Database(entities = [Product::class, Order::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
}