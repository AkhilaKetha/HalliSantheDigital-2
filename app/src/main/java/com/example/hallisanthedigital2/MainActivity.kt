package com.example.hallisanthedigital2

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var productAdapter: ProductAdapter
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Initialize Database (Ensure Product.kt is updated to Version 3)
        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "market_db")
            .fallbackToDestructiveMigration()
            .build()

        // 2. Set up Categories (Horizontal List)
        val rvCategories = findViewById<RecyclerView>(R.id.rvCategories)
        val categories = listOf(
            Category("All", 0),
            Category("Vegetables", 0),
            Category("Grocery", 0),
            Category("Fruits", 0),
            Category("Handicrafts", 0)
        )
        rvCategories.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvCategories.adapter = CategoryAdapter(categories) { selectedCategory ->
            filterByCategory(selectedCategory)
        }

        // 3. Set up Product Grid (Vertical List)
        val rvProducts = findViewById<RecyclerView>(R.id.recyclerView)

        // UPDATED: Now passing the click logic for the "+" button here
        productAdapter = ProductAdapter(emptyList()) { clickedProduct ->
            addToCart(clickedProduct)
        }

        rvProducts.layoutManager = GridLayoutManager(this, 2)
        rvProducts.adapter = productAdapter

        // 4. Bottom Navigation Logic
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.nav_home -> {
                    loadData()
                    true
                }
                R.id.nav_cat -> {
                    Toast.makeText(this, "Use the horizontal categories above!", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_orders -> {
                    // UPDATED: Now opens the real OrdersActivity
                    startActivity(Intent(this, OrdersActivity::class.java))
                    true
                }
                R.id.nav_profile -> {
                    Toast.makeText(this, "Profile: Artisan Account Active", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }

        // 5. Search Bar Logic
        findViewById<androidx.appcompat.widget.SearchView>(R.id.searchView)
            .setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?) = false
                override fun onQueryTextChange(newText: String?): Boolean {
                    searchData(newText ?: "")
                    return true
                }
            })

        // 6. Floating Action Button (Add Product)
        findViewById<FloatingActionButton>(R.id.fabAdd).setOnClickListener {
            startActivity(Intent(this, AddProductActivity::class.java))
        }

        loadData()
    }

    // NEW FUNCTION: Logic for the "+" button
    private fun addToCart(product: Product) {
        lifecycleScope.launch {
            val newOrder = Order(
                productName = product.name,
                productPrice = product.price,
                productImage = product.imagePath
            )
            db.productDao().addOrder(newOrder)
            Toast.makeText(this@MainActivity, "${product.name} added to Orders!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun loadData() {
        lifecycleScope.launch {
            val data = db.productDao().getAll()
            productAdapter.update(data)
        }
    }

    private fun filterByCategory(category: String) {
        lifecycleScope.launch {
            val data = if (category == "All") {
                db.productDao().getAll()
            } else {
                db.productDao().getByCategory(category)
            }
            productAdapter.update(data)
            if(data.isEmpty()) {
                Toast.makeText(this@MainActivity, "No products in $category", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun searchData(query: String) {
        lifecycleScope.launch {
            val data = db.productDao().search("%$query%")
            productAdapter.update(data)
        }
    }
}