package com.example.uxassignment3

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.uxassignment3.databinding.NewsCardBinding

class NewsAdapter (private val news: List<News>) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {
    class NewsViewHolder(binding: NewsCardBinding): RecyclerView.ViewHolder(binding.root) {
        val tvNewsTitle: TextView = binding.tvNewsTitle
        val ivNewsImage: ImageView = binding.ivNewsImage
        val tvNewsCategory: TextView = binding.tvNewsCategory
        val tvNewsUploadTime: TextView = binding.tvNewsUploadTime
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = NewsCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentItem = news[position]

        holder.tvNewsTitle.text = currentItem.title
        holder.ivNewsImage.setImageResource(currentItem.image)
        holder.tvNewsCategory.text = currentItem.category
        holder.tvNewsUploadTime.text = currentItem.uploadTime
    }

    override fun getItemCount(): Int {
        return news.size
    }
}