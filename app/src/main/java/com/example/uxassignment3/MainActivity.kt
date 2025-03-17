package com.example.uxassignment3

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uxassignment3.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var productAdapter: ProductAdapter
    private lateinit var products: List<Product>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        products = listOf(
            Product("Blonde Roast - Sunsera", R.drawable.ic_launcher_background, "Rp 54.000"),
            Product("Mocha Cookie Crumble", R.drawable.ic_launcher_background, "Rp 76.000"),
            Product("Lavender Crème Frappe", R.drawable.ic_launcher_background, "Rp69.000")
        )

        binding.btnInbox.setBadgeCount(1)

        binding.rvDOTD.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        productAdapter = ProductAdapter(products)
        binding.rvDOTD.adapter = productAdapter

    }
}