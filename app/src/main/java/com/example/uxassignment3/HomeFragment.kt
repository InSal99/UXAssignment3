package com.example.uxassignment3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uxassignment3.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val autoScrollDelay = 5000L

        private val loggedInUser : String = "Anastania"

        private val products = listOf(
            Product("Blonde Roast - Sunsera", R.drawable.menu1, "Rp 54.000"),
            Product("Mocha Cookie Crumble", R.drawable.menu2, "Rp 76.000"),
            Product("Lavender Crème Frappe", R.drawable.menu3, "Rp69.000")
        )

        private val news = listOf(
            News("Starbucks Reports Q1 Fiscal 2025 Results", R.drawable.news1, "Development", "15 hours ago"),
            News("We Embrace Our Community Growth", R.drawable.news2, "Promotion", "22 hours ago")
        )
        private val images = listOf(
            Image(R.drawable.promo1, "https://where2lifestylemagazine.com/wp-content/uploads/2021/11/TikTok-Drink-Web-Banner_1920x800-750x398.png"),
            Image(R.drawable.promo2, "https://www.starbucks.com.hk/media/wysiwyg/SUM3_BLACKPINK_DESKTOP_1248X692_REUSABLE_EN.jpg"),
            Image(R.drawable.promo3, "https://foodgressing.com/wp-content/uploads/2023/05/image006.jpg.webp")
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

        binding.btnNotif.onClickListener = {
            binding.btnNotif.updateBadgeCount(1)
        }
    }

    private fun setupUser() {
        binding.btnProfile.text = "Hi, ${getUsername()}"
    }

    private fun getUsername(): String {
        return loggedInUser
    }

    private fun setupNotif() {
        binding.btnNotif.updateBadgeCount(1)
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
