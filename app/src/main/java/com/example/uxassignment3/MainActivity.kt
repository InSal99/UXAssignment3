package com.example.uxassignment3

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.app.ActivityOptions
import android.content.Intent
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uxassignment3.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

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

        setupBottomNavigationListener()
        setupFragment()

    }

    private fun setupBottomNavigationListener() {
        binding.mBottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.btnHome -> {
                    animateAVD(binding.mBottomNavigation, R.id.btnHome)
                }
                R.id.btnCard -> {
                    showToast("Card clicked")
                    animateMenuItem(binding.mBottomNavigation, R.id.btnCard)
                }
                R.id.btnOrder -> {
                    showToast("Order clicked")
                    animateMenuItem(binding.mBottomNavigation, R.id.btnOrder)
                }
                R.id.btnReward -> {
                    showToast("Reward clicked")
                    animateMenuItem(binding.mBottomNavigation, R.id.btnReward)
                }
                R.id.btnStore -> {
                    showToast("Store clicked")
                    animateMenuItem(binding.mBottomNavigation, R.id.btnStore)
                }
            }
            true
        }
    }

    private fun setupFragment() {
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

    private fun animateMenuItem(bottomNavigationView: BottomNavigationView, menuItemId: Int) {
        val menuView = bottomNavigationView.findViewById<View>(menuItemId)

        val scaleX = PropertyValuesHolder.ofFloat("scaleX", 1.0f, 0.8f)
        val scaleY = PropertyValuesHolder.ofFloat("scaleY", 1.0f, 0.8f)
        val animator = ObjectAnimator.ofPropertyValuesHolder(menuView, scaleX, scaleY).apply {
            duration = 200
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }

        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                val reverseScaleX = PropertyValuesHolder.ofFloat("scaleX", 0.8f, 1.0f)
                val reverseScaleY = PropertyValuesHolder.ofFloat("scaleY", 0.8f, 1.0f)
                ObjectAnimator.ofPropertyValuesHolder(menuView, reverseScaleX, reverseScaleY).apply {
                    duration = 200
                    interpolator = AccelerateDecelerateInterpolator()
                    start()
                }
            }
        })
    }

    private fun animateAVD (bottomNavigationView: BottomNavigationView, menuItemId: Int) {
        val menuView = bottomNavigationView.findViewById<View>(menuItemId)
        if (menuView == null) {
            Log.e("AnimationDebug", "Menu view not found.")
            return
        }

        val iconContainer = menuView.findViewById<View>(com.google.android.material.R.id.navigation_bar_item_icon_container)
        val icon = iconContainer?.findViewById<ImageView>(com.google.android.material.R.id.navigation_bar_item_icon_view)
        if (icon == null) {
            Log.e("AnimationDebug", "Icon not found.")
            return
        }

        val avd = icon.drawable as? AnimatedVectorDrawable
        avd?.start()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}