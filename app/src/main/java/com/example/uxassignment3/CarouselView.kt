package com.example.uxassignment3

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.uxassignment3.databinding.CarouselViewBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class CarouselView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: CarouselViewBinding =
        CarouselViewBinding .inflate(LayoutInflater.from(context), this, true)

    private var autoScrollDelay = 7000L
    private val handler = Handler(Looper.getMainLooper())
    private val autoScrollRunnable = object : Runnable {
        override fun run() {
            val viewPager = binding.vpPnP2
            val nextItem = (viewPager.currentItem + 1) % (viewPager.adapter?.itemCount ?: 1)
            viewPager.post { viewPager.setCurrentItem(nextItem, true) }
            handler.removeCallbacks(this)
            handler.postDelayed(this, autoScrollDelay)
        }
    }

    var images: List<Image> = emptyList()
        set(value) {
            field = value
            setupViewPager()
        }

    init {
        setupViewPager()
    }

    private fun setupViewPager() {
        binding.vpPnP2.apply {
            adapter = ImageAdapter(images)
            offscreenPageLimit = 1
        }
        setupTabLayout()
        startAutoScroll()
    }

    private fun setupTabLayout() {
        TabLayoutMediator(binding.dotIndicator2, binding.vpPnP2) { tab, _ ->
            LayoutInflater.from(context).inflate(R.layout.tab_item, null).also { customView ->
                tab.customView = customView
            }
        }.attach()

        binding.dotIndicator2.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                updateDotAppearance(tab.customView, true)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                updateDotAppearance(tab.customView, false)
            }

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        binding.dotIndicator2.post {
            if (binding.dotIndicator2.tabCount > 0) {
                binding.dotIndicator2.getTabAt(0)?.let { tab ->
                    updateDotAppearance(tab.customView, true, resources.getDimensionPixelSize(R.dimen.stretched_width))
                    tab.select()
                }
            }
        }

        binding.vpPnP2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                animateTabDots(position, positionOffset)
            }

            //Reset and restart auto-scroll
            override fun onPageSelected(position: Int) {
                handler.removeCallbacks(autoScrollRunnable)
                handler.postDelayed(autoScrollRunnable, autoScrollDelay)
            }
        })
    }

    private fun animateTabDots(position: Int, positionOffset: Float) {
        val currentTab = binding.dotIndicator2.getTabAt(position)?.customView
        val nextTab = binding.dotIndicator2.getTabAt(position + 1)?.customView

        currentTab?.let {
            updateDotWidth(it.findViewById(R.id.tabDot), calculateDotWidth(1 - positionOffset))
        }

        nextTab?.let {
            updateDotWidth(it.findViewById(R.id.tabDot), calculateDotWidth(positionOffset))
        }
    }

    private fun calculateDotWidth(percentage: Float): Int {
        val defaultWidth = resources.getDimensionPixelSize(R.dimen.default_width)
        val stretchedWidth = resources.getDimensionPixelSize(R.dimen.stretched_width)
        return defaultWidth + ((stretchedWidth - defaultWidth) * percentage).toInt()
    }


    private fun updateDotAppearance(tabView: View?, isSelected: Boolean, width: Int? = null) {
        tabView?.let { view ->
            val dotView = view.findViewById<ImageView>(R.id.tabDot)
            dotView.background = createDotDrawable(isSelected)
            width?.let { updateDotWidth(dotView, it) }
        }
    }

    private fun updateDotWidth(dotView: ImageView, width: Int) {
        dotView.layoutParams = dotView.layoutParams.apply {
            this.width = width
        }
        dotView.requestLayout()
    }

    private fun createDotDrawable(isSelected: Boolean): GradientDrawable {
        return GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = resources.getDimension(R.dimen.corner_radius)
            setColor(ContextCompat.getColor(context, if (isSelected) R.color.green_starbucks else R.color.black_light))
        }
    }

    fun startAutoScroll() {
        handler.postDelayed(autoScrollRunnable, autoScrollDelay)
    }

    fun stopAutoScroll() {
        handler.removeCallbacks(autoScrollRunnable)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startAutoScroll()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopAutoScroll()
    }

    fun updateImages(images: List<Image>) {
        binding.vpPnP2.adapter = ImageAdapter(images)
    }

}

