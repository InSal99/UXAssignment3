package com.example.uxassignment3

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.example.uxassignment3.databinding.RewardBannerViewBinding
import kotlin.math.roundToInt

class RewardCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: RewardBannerViewBinding = RewardBannerViewBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    private var _progressText: String = "240 stars to Gold Level"
    private var _levelButtonText: String = "Green Level"
    private var _progressBarColor: Int = context.getColor(R.color.green_accent)

    var progress: Float = 0f
        set(value) {
            updateProgressViews(value)
        }

    var progressText: String
        get() = _progressText
        set(value) {
            _progressText = value
            binding.tvProgress.text = value
        }

    var levelButtonText: String
        get() = _levelButtonText
        set(value) {
            _levelButtonText = value
            binding.btnLevel.text = value
        }

    var progressBarColor: Int
        get() = _progressBarColor
        set(value) {
            _progressBarColor = value
            binding.customProgressBar.setBarColor(value)
        }

    init {
        initAttributes(attrs)
        setupViews()
    }

    private fun initAttributes(attrs: AttributeSet?) {
        context.obtainStyledAttributes(attrs, R.styleable.RewardCardView).use { ta ->
            _progressText = ta.getString(R.styleable.RewardCardView_progressText) ?: _progressText
            _levelButtonText = ta.getString(R.styleable.RewardCardView_levelButtonText) ?: _levelButtonText
            progress = ta.getFloat(R.styleable.RewardCardView_initialProgress, 60f)
            _progressBarColor = ta.getColor(R.styleable.RewardCardView_progressBarColor, context.getColor(R.color.green_accent))
        }
    }

    private fun setupViews() {
        progressText = _progressText
        levelButtonText = _levelButtonText
        progressBarColor = _progressBarColor
    }

    private fun updateRewardCardView(progress: Float) {
        if (progress >= GOLD_THRESHOLD) {
            binding.vCardDecorator.background = context.getDrawable(R.drawable.rounded_bg_gold_full)
            binding.ivStar.imageTintList = ColorStateList.valueOf(context.getColor(R.color.gold_full))
        }
    }

    private fun updateProgressViews(progress: Float) {
        binding.customProgressBar.setupProgress(progress)
        binding.tvStarCount.text = progress.toInt().toString()
        updateRewardCardView(progress)
    }

    companion object {
        private const val MAX_PROGRESS = 500f
        private const val GOLD_THRESHOLD = 300f
    }
}