package com.example.hallisanthedigital2

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.coroutines.launch

class OrdersActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders)

        val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "market_db")
            .fallbackToDestructiveMigration().build()

        val rv = findViewById<RecyclerView>(R.id.rvOrders)
        rv.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            val orderList = db.productDao().getAllOrders()
            // We use the same ProductAdapter here for simplicity, or create a simple one
            rv.adapter = OrderViewAdapter(orderList)
        }

        findViewById<Button>(R.id.btnCheckout).setOnClickListener {
            Toast.makeText(this, "Order Sent to Local Artisan!", Toast.LENGTH_LONG).show()
            lifecycleScope.launch { db.productDao().clearOrders() }
            finish()
        }
    }
}