package com.example.uxassignment3

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.uxassignment3.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupInbox()
        setupRecyclerViews()
        setupCarousel()
    }

    private fun setupInbox() {
        binding.btnInbox.setBadgeCount(1)
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

    companion object {
        private const val autoScrollDelay = 5000L

        private val products = listOf(
            Product("Blonde Roast - Sunsera", R.drawable.ic_launcher_background, "Rp 54.000"),
            Product("Mocha Cookie Crumble", R.drawable.ic_launcher_background, "Rp 76.000"),
            Product("Lavender Crème Frappe", R.drawable.ic_launcher_background, "Rp69.000")
        )

        private val news = listOf(
            News("Starbucks Reports Q1 Fiscal 2025 Results", R.drawable.ic_launcher_background, "Development", "15 hours ago"),
            News("We Embrace Our Community Growth", R.drawable.ic_launcher_background, "Promotion", "22 hours ago")
        )
        private val images = listOf(
            Image(R.drawable.promo1, "https://fastly.picsum.photos/id/866/500/500.jpg"),
            Image(R.drawable.promo1, "https://fastly.picsum.photos/id/270/500/500.jpg"),
            Image(R.drawable.promo1, "https://fastly.picsum.photos/id/320/500/500.jpg"),
            Image(R.drawable.promo1, "https://fastly.picsum.photos/id/798/500/500.jpg")
        )
    }
}






//class HomeFragment : Fragment() {
//    private var _binding: FragmentHomeBinding? = null
//    private val binding get() = _binding!!
//
//    private val defaultWidth by lazy { resources.getDimensionPixelSize(R.dimen.default_width) }
//    private val stretchedWidth by lazy { resources.getDimensionPixelSize(R.dimen.stretched_width) }
//    private val cornerRadius by lazy { resources.getDimension(R.dimen.corner_radius) }
//
//    // Auto-scroll handler
//    private val handler = Handler(Looper.getMainLooper())
//    private val autoScrollRunnable: Runnable = object : Runnable {
//        override fun run() {
//            binding.vpPnP.let { viewPager ->
//                val currentItem = viewPager.currentItem
//                val itemCount = viewPager.adapter?.itemCount ?: 0
//
//                if (currentItem == itemCount - 1) {
//                    resetDotAppearance(binding.dotIndicator.getTabAt(itemCount - 1)?.customView)
//                }
//
//                val nextItem = if (currentItem < itemCount - 1) currentItem + 1 else 0
//                viewPager.setCurrentItem(nextItem, true)
//
//                handler.postDelayed(this, autoScrollDelay)
//            }
//        }
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentHomeBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        binding.carouselPnP.updateImages(images)
//
//        setupInbox()
//        setupRecyclerViews()
//        setupCarousel()
//    }
//
//    private fun setupInbox() {
//        binding.btnInbox.setBadgeCount(1)
//    }
//
//    private fun setupRecyclerViews() {
//        // Products recycler view
//        binding.rvDOTD.apply {
//            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//            adapter = ProductAdapter(products)
//        }
//
//        // News recycler view
//        binding.rvSN.apply {
//            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//            adapter = NewsAdapter(news)
//        }
//    }
//
//    private fun setupCarousel() {
//        binding.vpPnP.apply {
//            adapter = ImageAdapter(images)
//            offscreenPageLimit = 1
//            post { requestLayout() }
//        }
//
//        setupTabLayout()
//    }
//
//    private fun setupTabLayout() {
//        // Connect TabLayout with ViewPager2 and set up custom dots
//        TabLayoutMediator(binding.dotIndicator, binding.vpPnP) { tab, _ ->
//            // Create custom tab view
//            LayoutInflater.from(context).inflate(R.layout.tab_item, null).also { customView ->
//                tab.customView = customView
//            }
//        }.attach()
//
//        // Set up dot appearance changes
//        binding.dotIndicator.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//            override fun onTabSelected(tab: TabLayout.Tab) {
//                updateDotAppearance(tab.customView, true)
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab) {
//                updateDotAppearance(tab.customView, false)
//            }
//
//            override fun onTabReselected(tab: TabLayout.Tab) {
//                // Not needed
//            }
//        })
//
//        // Initialize first tab
//        if (binding.dotIndicator.tabCount > 0) {
//            binding.dotIndicator.getTabAt(0)?.let { tab ->
//                updateDotAppearance(tab.customView, true, stretchedWidth)
//                tab.select()
//            }
//        }
//
//        // Register page change callback
//        binding.vpPnP.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
//                animateTabDots(position, positionOffset)
//            }
//        })
//
//        // Disable tab clicks
//        for (i in 0 until binding.dotIndicator.tabCount) {
//            binding.dotIndicator.getTabAt(i)?.view?.isClickable = false
//        }
//    }
//
//    private fun animateTabDots(position: Int, positionOffset: Float) {
//        // Get current and next tabs
//        val currentTab = binding.dotIndicator.getTabAt(position)?.customView
//        val nextPosition = position + 1
//        val nextTab = if (nextPosition < binding.dotIndicator.tabCount)
//            binding.dotIndicator.getTabAt(nextPosition)?.customView else null
//
//        // Animate current tab dot
//        currentTab?.let {
//            val width = calculateDotWidth(1 - positionOffset)
//            updateDotWidth(getDotView(it), width)
//        }
//
//        // Animate next tab dot if available
//        nextTab?.let {
//            val width = calculateDotWidth(positionOffset)
//            updateDotWidth(getDotView(it), width)
//        }
//    }
//
//    private fun calculateDotWidth(percentage: Float): Int {
//        return defaultWidth + ((stretchedWidth - defaultWidth) * percentage).toInt()
//    }
//
//    private fun getDotView(tabView: View): ImageView {
//        return tabView.findViewById(R.id.tabDot)
//    }
//
//    private fun updateDotAppearance(tabView: View?, isSelected: Boolean, width: Int? = null) {
//        tabView?.let { view ->
//            val dotView = getDotView(view)
//
//            // Set background drawable
//            dotView.background = createDotDrawable(isSelected)
//
//            // Set width if specified
//            width?.let { updateDotWidth(dotView, it) }
//        }
//    }
//
//    private fun resetDotAppearance(tabView: View?) {
//        updateDotAppearance(tabView, false, defaultWidth)
//    }
//
//    private fun createDotDrawable(isSelected: Boolean): GradientDrawable {
//        return GradientDrawable().apply {
//            shape = GradientDrawable.RECTANGLE
//            cornerRadius = this.cornerRadius
//            setColor(ContextCompat.getColor(
//                requireActivity(),
//                if (isSelected) R.color.green_starbucks else R.color.black_light
//            ))
//        }
//    }
//
//    private fun updateDotWidth(dotView: ImageView, width: Int) {
//        dotView.layoutParams = dotView.layoutParams.apply {
//            this.width = width
//        }
//        dotView.requestLayout()
//    }
//
//    private fun startAutoScroll() {
//        handler.postDelayed(autoScrollRunnable, autoScrollDelay)
//    }
//
//    private fun stopAutoScroll() {
//        handler.removeCallbacks(autoScrollRunnable)
//    }
//
//    override fun onResume() {
//        super.onResume()
//        startAutoScroll()
//    }
//
//    override fun onPause() {
//        super.onPause()
//        stopAutoScroll()
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//
//    companion object {
//        private const val autoScrollDelay = 5000L
//
//        private val products = listOf(
//            Product("Blonde Roast - Sunsera", R.drawable.ic_launcher_background, "Rp 54.000"),
//            Product("Mocha Cookie Crumble", R.drawable.ic_launcher_background, "Rp 76.000"),
//            Product("Lavender Crème Frappe", R.drawable.ic_launcher_background, "Rp69.000")
//        )
//
//        private val news = listOf(
//            News("Starbucks Reports Q1 Fiscal 2025 Results", R.drawable.ic_launcher_background, "Development", "15 hours ago"),
//            News("We Embrace Our Community Growth", R.drawable.ic_launcher_background, "Promotion", "22 hours ago")
//        )
//
//        // Note: All images use the same drawable - consider optimizing this if possible
//        private val images = listOf(
//            Image(R.drawable.promo1, "https://fastly.picsum.photos/id/866/500/500.jpg?hmac=FOptChXpmOmfR5SpiL2pp74Yadf1T_bRhBF1wJZa9hg"),
//            Image(R.drawable.promo1, "https://fastly.picsum.photos/id/270/500/500.jpg?hmac=MK7XNrBrZ73QsthvGaAkiNoTl65ZDlUhEO-6fnd-ZnY"),
//            Image(R.drawable.promo1, "https://fastly.picsum.photos/id/320/500/500.jpg?hmac=2iE7TIF9kIqQOHrIUPOJx2wP1CJewQIZBeMLIRrm74s"),
//            Image(R.drawable.promo1, "https://fastly.picsum.photos/id/798/500/500.jpg?hmac=Bmzk6g3m8sUiEVHfJWBscr2DUg8Vd2QhN7igHBXLLfo"),
//            Image(R.drawable.promo1, "https://fastly.picsum.photos/id/95/500/500.jpg?hmac=0aldBQ7cQN5D_qyamlSP5j51o-Og4gRxSq4AYvnKk2U"),
//            Image(R.drawable.promo1, "https://fastly.picsum.photos/id/778/500/500.jpg?hmac=jZLZ6WV_OGRxAIIYPk7vGRabcAGAILzxVxhqSH9uLas")
//        )
//    }
//}












//class HomeFragment : Fragment() {
//    private lateinit var _binding: FragmentHomeBinding
//    private val binding get() = _binding
//
//    private lateinit var productAdapter: ProductAdapter
//    private lateinit var products: List<Product>
//    private lateinit var newsAdapter: NewsAdapter
//    private lateinit var news: List<News>
//
//    private lateinit var viewPager : ViewPager2
//    private lateinit var tabLayout: TabLayout
//
//    // Use lazy initialization
//    private val defaultWidth by lazy { resources.getDimensionPixelSize(R.dimen.default_width) }
//    private val stretchedWidth by lazy { resources.getDimensionPixelSize(R.dimen.stretched_width) }
//
//    // Auto-scroll handler (add these here)
//    private val handler = Handler(Looper.getMainLooper())
//
//    private val autoScrollRunnable = object : Runnable {
//        override fun run() {
//            val currentItem = viewPager.currentItem
//            val itemCount = viewPager.adapter?.itemCount ?: 0
//
//            // Check if we're on the last item
//            if (currentItem == itemCount - 1) {
//                // We're about to wrap around to the first item
//                // Reset the width of the last dot manually before transition
//                val lastTab = tabLayout.getTabAt(itemCount - 1)?.customView
//                lastTab?.let { view ->
//                    val dotView = view.findViewById<ImageView>(R.id.tabDot)
//                    // Use the shape reset from your existing code but apply it immediately
//                    val circleDrawable = GradientDrawable()
//                    circleDrawable.shape = GradientDrawable.RECTANGLE
//                    circleDrawable.cornerRadius = resources.getDimension(R.dimen.corner_radius)
//                    circleDrawable.setColor(
//                        ContextCompat.getColor(requireActivity(),
//                            R.color.black_light))
//                    dotView.background = circleDrawable
//
//                    // Reset width immediately
//                    val params = dotView.layoutParams
//                    params.width = defaultWidth
//                    dotView.layoutParams = params
//                }
//            }
//
//            // Continue with normal transition
//            val nextItem = if (currentItem < itemCount - 1) currentItem + 1 else 0
//            viewPager.setCurrentItem(nextItem, true) // true for smooth scroll
//            handler.postDelayed(this, 5000) // 5 seconds interval
//        }
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        _binding = FragmentHomeBinding.inflate(layoutInflater)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        products = listOf(
//            Product("Blonde Roast - Sunsera", R.drawable.ic_launcher_background, "Rp 54.000"),
//            Product("Mocha Cookie Crumble", R.drawable.ic_launcher_background, "Rp 76.000"),
//            Product("Lavender Crème Frappe", R.drawable.ic_launcher_background, "Rp69.000")
//        )
//
//        news = listOf(
//            News("Starbucks Reports Q1 Fiscal 2025 Results", R.drawable.ic_launcher_background, "Development", "15 hours ago"),
//            News("We Embrace Our Community Growth", R.drawable.ic_launcher_background, "Promotion", "22 hours ago")
//        )
//
//        binding.btnInbox.setBadgeCount(1)
//
//        binding.rvDOTD.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//        productAdapter = ProductAdapter(products)
//        binding.rvDOTD.adapter = productAdapter
//
//        binding.rvSN.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//        newsAdapter = NewsAdapter(news)
//        binding.rvSN.adapter = newsAdapter
//
//
//        /*==================================== CAROUSEL ==============================================*/
//        viewPager = binding.vpPnP
//        binding.vpPnP.offscreenPageLimit = 1
//        binding.vpPnP.post {
//            binding.vpPnP.requestLayout()
//        }
//        tabLayout = binding.dotIndicator
//
//        val imageList = arrayListOf(
//            Image(
//                R.drawable.promo1,
//                "https://fastly.picsum.photos/id/866/500/500.jpg?hmac=FOptChXpmOmfR5SpiL2pp74Yadf1T_bRhBF1wJZa9hg"
//            ),
//            Image(
//                R.drawable.promo1,
//                "https://fastly.picsum.photos/id/270/500/500.jpg?hmac=MK7XNrBrZ73QsthvGaAkiNoTl65ZDlUhEO-6fnd-ZnY"
//            ),
//            Image(
//                R.drawable.promo1,
//                "https://fastly.picsum.photos/id/320/500/500.jpg?hmac=2iE7TIF9kIqQOHrIUPOJx2wP1CJewQIZBeMLIRrm74s"
//            ),
//            Image(
//                R.drawable.promo1,
//                "https://fastly.picsum.photos/id/798/500/500.jpg?hmac=Bmzk6g3m8sUiEVHfJWBscr2DUg8Vd2QhN7igHBXLLfo"
//            ),
//            Image(
//                R.drawable.promo1,
//                "https://fastly.picsum.photos/id/95/500/500.jpg?hmac=0aldBQ7cQN5D_qyamlSP5j51o-Og4gRxSq4AYvnKk2U"
//            ),
//            Image(
//                R.drawable.promo1,
//                "https://fastly.picsum.photos/id/778/500/500.jpg?hmac=jZLZ6WV_OGRxAIIYPk7vGRabcAGAILzxVxhqSH9uLas"
//            )
//        )
//
//
//        // Set up ViewPager2 with the updated adapter
//        val adapter = ImageAdapter(imageList)
//        viewPager.adapter = adapter
//
//        // Connect TabLayout with ViewPager2
//        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
//            // Inflate the custom tab layout
//            val customTabView = LayoutInflater.from(context).inflate(R.layout.tab_item, null)
//            tab.customView = customTabView
//        }.attach()
//
//        // Modify your TabLayout listener
//        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//            override fun onTabSelected(tab: TabLayout.Tab) {
//                val tabDot = tab.customView
//                tabDot?.let { view ->
//                    // Only change the color/shape here, not the width
//                    val dotView = view.findViewById<ImageView>(R.id.tabDot)
//                    val pillDrawable = GradientDrawable()
//                    pillDrawable.shape = GradientDrawable.RECTANGLE
//                    pillDrawable.cornerRadius = resources.getDimension(R.dimen.corner_radius)
//                    pillDrawable.setColor(ContextCompat.getColor(requireActivity(),
//                        R.color.green_starbucks))
//                    dotView.background = pillDrawable
//                    // Don't animate width here, let onPageScrolled handle it
//                }
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab) {
//                val tabDot = tab.customView
//                tabDot?.let { view ->
//                    // Only change the color/shape here, not the width
//                    val dotView = view.findViewById<ImageView>(R.id.tabDot)
//                    val circleDrawable = GradientDrawable()
//                    circleDrawable.shape = GradientDrawable.RECTANGLE
//                    circleDrawable.cornerRadius = resources.getDimension(R.dimen.corner_radius)
//                    circleDrawable.setColor(ContextCompat.getColor(requireActivity(),
//                        R.color.black_light))
//                    dotView.background = circleDrawable
//                    // Don't animate width here, let onPageScrolled handle it
//                }
//            }
//
//            override fun onTabReselected(tab: TabLayout.Tab) {
//                // Handle reselection if needed
//            }
//        })
//
//        // Manually select the first tab to trigger the animation
//        if (tabLayout.tabCount > 0) {
//            val firstTab = tabLayout.getTabAt(0)
//            firstTab?.let { tab ->
//                // Force animation of the first dot
//                val tabDot = tab.customView
//                tabDot?.let { view ->
//                    // Directly apply the selected state without animation
//                    val dotView = view.findViewById<ImageView>(R.id.tabDot)
//
//                    // Create pill shape for selected state
//                    val pillDrawable = GradientDrawable()
//                    pillDrawable.shape = GradientDrawable.RECTANGLE
//                    pillDrawable.cornerRadius = resources.getDimension(R.dimen.corner_radius)
//                    pillDrawable.setColor(ContextCompat.getColor(requireActivity(),
//                        R.color.green_starbucks))
//
//                    // Apply the drawable and width
//                    dotView.background = pillDrawable
//                    val params = dotView.layoutParams
//                    params.width = stretchedWidth
//                    dotView.layoutParams = params
//
//                    // Select the tab (this is different from just styling it)
//                    tab.select()
//                }
//            }
//        }
//
//        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
//
//                val currentTab = tabLayout.getTabAt(position)?.customView
//                val nextTab = tabLayout.getTabAt(position + 1)?.customView
//
//                currentTab?.let { tab ->
//                    val width = defaultWidth + ((stretchedWidth - defaultWidth) * (1 - positionOffset)).toInt()
//                    animateDotWidth(tab.findViewById(R.id.tabDot), width)
//                }
//
//                nextTab?.let { tab ->
//                    val width = defaultWidth + ((stretchedWidth - defaultWidth) * positionOffset).toInt()
//                    animateDotWidth(tab.findViewById(R.id.tabDot), width)
//                }
//            }
//        })
//
//
//        // Make dots not clickable
//        for (i in 0 until tabLayout.tabCount) {
//            val tab = tabLayout.getTabAt(i)
//            tab?.view?.isClickable = false
//        }
//    }
//
//    private fun animateDotWidth(dotView: ImageView, width: Int) {
//        val params = dotView.layoutParams
//        params.width = width
//        dotView.layoutParams = params
//        dotView.requestLayout()
//    }
//
//    // Add these somewhere in your class, with your other methods
//    private fun startAutoScroll() {
//        handler.postDelayed(autoScrollRunnable, 5000)
//    }
//
//    private fun stopAutoScroll() {
//        handler.removeCallbacks(autoScrollRunnable)
//    }
//
//    // Add these at the end of your class
//    override fun onResume() {
//        super.onResume()
//        startAutoScroll()
//    }
//
//    override fun onPause() {
//        super.onPause()
//        stopAutoScroll()
//    }
//
//    companion object {
//
//    }
//}