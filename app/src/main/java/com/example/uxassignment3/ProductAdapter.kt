package com.example.uxassignment3

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.uxassignment3.databinding.ProductCardBinding

class ProductAdapter(private val products: List<Product>): RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
    class ProductViewHolder(binding: ProductCardBinding): RecyclerView.ViewHolder(binding.root) {
        val tvProductName: TextView = binding.tvProductName
        val ivProductImage: ImageView = binding.ivProductImage
        val tvProductPrice: TextView = binding.tvProductPrice
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ProductCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val currentItem = products[position]

        holder.tvProductName.text = currentItem.name
        holder.ivProductImage.setImageResource(currentItem.image)
        holder.tvProductPrice.text = currentItem.price
    }

    override fun getItemCount(): Int {
        return products.size
    }
}