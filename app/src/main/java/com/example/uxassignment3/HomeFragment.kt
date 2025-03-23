package com.example.uxassignment3

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.OptIn
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.uxassignment3.BadgeUtils.attachBadge
import com.example.uxassignment3.BadgeUtils.updateBadgeCount
import com.example.uxassignment3.databinding.FragmentHomeBinding
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.badge.ExperimentalBadgeUtils
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val autoScrollDelay = 5000L

        private val loggedInUser : String = "Anastania"

        private val products = listOf(
            Product("Blonde Roast - Sunsera", R.drawable.menu1, "Rp 54.000"),
            Product("Mocha Cookie Crumble", R.drawable.menu2, "Rp 76.000"),
            Product("Lavender Crème Frappe", R.drawable.menu2, "Rp69.000")
        )

        private val news = listOf(
            News("Starbucks Reports Q1 Fiscal 2025 Results", R.drawable.news1, "Development", "15 hours ago"),
            News("We Embrace Our Community Growth", R.drawable.news2, "Promotion", "22 hours ago")
        )
        private val images = listOf(
            Image(R.drawable.promo1, "https://fastly.picsum.photos/id/866/500/500.jpg"),
            Image(R.drawable.promo1, "https://fastly.picsum.photos/id/270/500/500.jpg"),
            Image(R.drawable.promo1, "https://fastly.picsum.photos/id/320/500/500.jpg"),
            Image(R.drawable.promo1, "https://fastly.picsum.photos/id/798/500/500.jpg")
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUser()
        setupNotif()
        setupCarousel()
        setupRecyclerViews()
    }

    private fun setupUser() {
        binding.btnProfile.text = "Hi, ${getUsername()}"
    }

    private fun getUsername(): String {
        return loggedInUser
    }

    private fun setupNotif() {
        binding.btnNotif.updateBadgeCount(10)
    }

    private fun setupRecyclerViews() {
        binding.rvDOTD.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = ProductAdapter(products)
        }

        binding.rvSN.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = NewsAdapter(news)
        }
    }

    private fun setupCarousel() {
        binding.carouselPnP.images = images
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
