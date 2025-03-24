package com.example.uxassignment3

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.example.uxassignment3.databinding.RewardBannerViewBinding

class RewardCardView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: RewardBannerViewBinding

    var progress: Float = 0f
        set(value) {
            field = value
            binding.customProgressBar.setupProgress(value)
            binding.tvStarCount.text = value.toInt().toString() // Convert progress to stars
        }

    init {
        binding = RewardBannerViewBinding.inflate(LayoutInflater.from(context), this, true)
        setupInitialViews()
    }

    private fun setupInitialViews() {
        binding.tvProgress.text = "Custom Progress Text"
        binding.btnLevel.text = "Custom Level"
        progress = 240f // This will automatically update both progress bar and star count
    }

    // Optional: If you need separate control
    fun setProgressWithStars(progress: Float) {
        this.progress = progress
    }

//    init {
//        binding = RewardBannerViewBinding.inflate(LayoutInflater.from(context), this, true)
//
//        binding.tvProgress.text = "Custom Progress Text"
//        binding.btnLevel.text = "Custom Level"
//        binding.tvStarCount.text = "100" // Example: Update star count
//        binding.customProgressBar.setupProgress(30f) // Example: Update progress
//    }
//
//    fun setProgressText(text: String) {
//        binding.tvProgress.text = text
//    }
//
//    fun setStarCount(count: String) {
//        binding.tvStarCount.text = count
//    }
//
//    fun setProgress(progress: Float) {
//        binding.customProgressBar.setupProgress(progress)
//    }
//
//    fun getProgress(value: Float): Int {
//        binding.tvStarCount.text = value.toInt().toString()
//        Log.d("when get progress", value.toInt().toString())
//        return value.toInt()
//    }

}