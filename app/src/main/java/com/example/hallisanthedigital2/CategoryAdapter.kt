package com.example.hallisanthedigital2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CategoryAdapter(
    private val list: List<Category>,
    private val onCategoryClick: (String) -> Unit // This handles the click
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val name: TextView = v.findViewById(R.id.categoryName)
        val iconCircle: View = v.findViewById(R.id.categoryIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.name.text = item.name

        // When a user clicks a circle (e.g., Vegetables), this tells MainActivity
        holder.itemView.setOnClickListener {
            onCategoryClick(item.name)
        }
    }

    override fun getItemCount() = list.size
}