package com.example.hallisanthedigital2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.io.File

class ProductAdapter(
    private var list: List<Product>,
    private val onAddClick: (Product) -> Unit // New callback for the + button
) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val img: ImageView = v.findViewById(R.id.imgProduct)
        val name: TextView = v.findViewById(R.id.txtProductName)
        val price: TextView = v.findViewById(R.id.txtProductPrice)
        val btnAdd: ImageView = v.findViewById(R.id.btnAddIcon) // Ensure you add this ID in XML
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.name.text = item.name
        holder.price.text = "₹ ${item.price}"
        Glide.with(holder.itemView.context).load(File(item.imagePath)).into(holder.img)

        // Handle the Green "+" Button Click
        holder.btnAdd.setOnClickListener {
            onAddClick(item)
        }
    }

    override fun getItemCount() = list.size

    fun update(newList: List<Product>) {
        list = newList
        notifyDataSetChanged()
    }
}