package com.example.hallisanthedigital2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.io.File

class OrderViewAdapter(private val list: List<Order>) : RecyclerView.Adapter<OrderViewAdapter.ViewHolder>() {
    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val img: ImageView = v.findViewById(R.id.imgProduct)
        val name: TextView = v.findViewById(R.id.txtProductName)
        val price: TextView = v.findViewById(R.id.txtProductPrice)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false))

    override fun onBindViewHolder(h: ViewHolder, p: Int) {
        val item = list[p]
        h.name.text = item.productName
        h.price.text = "₹ ${item.productPrice}"
        Glide.with(h.itemView).load(File(item.productImage)).into(h.img)
    }
    override fun getItemCount() = list.size
}