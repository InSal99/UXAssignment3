package com.example.uxassignment3

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.example.uxassignment3.databinding.RewardBannerViewBinding
import kotlin.math.roundToInt

class RewardCardView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: RewardBannerViewBinding

    init {
        binding = RewardBannerViewBinding.inflate(LayoutInflater.from(context), this, true)

        binding.tvProgress.text = "Custom Progress Text"
        binding.btnLevel.text = "Custom Level"
        binding.tvStarCount.text = getProgress() // Example: Update star count
        binding.customProgressBar.setProgress(60f) // Example: Update progress
    }

    fun setProgressText(text: String) {
        binding.tvProgress.text = text
    }

    fun setStarCount(count: String) {
        binding.tvStarCount.text = count
    }

    fun setProgress(progress: Float) {
        binding.customProgressBar.setProgress(progress)
    }

    fun getProgress() : String {
        Log.d("Tes Active Checkpoint", binding.customProgressBar.checkpointLabels[binding.customProgressBar.activeCheckpoint])
        Log.d("Active Checkpoint", binding.customProgressBar.activeCheckpoint.toString())
        return binding.customProgressBar.checkpointLabels[binding.customProgressBar.activeCheckpoint]
    }

}