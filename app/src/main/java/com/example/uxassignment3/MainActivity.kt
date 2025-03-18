package com.example.uxassignment3

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uxassignment3.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

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

        binding.mBottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
//                R.id.btnHome -> showToast("Home clicked")
                R.id.btnCard -> showToast("Card clicked")
                R.id.btnOrder -> showToast("Order clicked")
                R.id.btnReward -> showToast("Reward clicked")
                R.id.btnStore -> showToast("Store clicked")
            }
            true
        }

        val mFragmentManager = super.getSupportFragmentManager()
        val fragment1  = HomeFragment()
        val fragment = mFragmentManager.findFragmentByTag(HomeFragment::class.java.simpleName)

        if (fragment == null) {
            mFragmentManager
                .beginTransaction()
                .replace(binding.root.id, fragment1, HomeFragment::class.java.simpleName)
                .commit()
        }

    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}