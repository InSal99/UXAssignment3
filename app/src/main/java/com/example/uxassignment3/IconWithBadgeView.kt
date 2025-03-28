package com.example.uxassignment3

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.BounceInterpolator
import androidx.core.content.ContextCompat
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.button.MaterialButton

class IconWithBadgeView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MaterialButton(context, attrs, defStyleAttr) {

    private lateinit var badgeDrawable: BadgeDrawable
    var onClickListener: (() -> Unit)? = null

    init {
        setupBadge()
        setupTouchListener()
    }

    private fun setupBadge() {
        badgeDrawable = BadgeDrawable.create(context).apply {
            backgroundColor = ContextCompat.getColor(context, R.color.red_full)
            badgeTextColor = ContextCompat.getColor(context, R.color.white_full)
            maxCharacterCount = 3
            badgeGravity = BadgeDrawable.TOP_END
            isVisible = true
            number = 0
            horizontalOffset = 60
            verticalOffset = 60
            setTextAppearance(R.style.Caption_Regular)
        }
        BadgeUtils.attachBadge(badgeDrawable, this)
    }

    fun updateBadgeCount(count: Int) {
        badgeDrawable.number +=  count

        if (badgeDrawable.number > 99) {
            badgeDrawable.text = "99+"
        }
        Log.d("BadgeDebug", "Badge count updated: ${badgeDrawable.number}")
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupTouchListener() {
        setOnTouchListener {view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    view.animate().scaleX(0.8f).scaleY(0.8f).setDuration(200).start()
                }
                MotionEvent.ACTION_UP -> {
                    view.animate().scaleX(1f).scaleY(1f).setDuration(200).setInterpolator(AccelerateDecelerateInterpolator()).start()
//                    view.performClick()
                    setupClickListener()
                    onClickListener?.invoke()
                }
                MotionEvent.ACTION_CANCEL -> {
                    view.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
                }
            }
            return@setOnTouchListener true
        }
    }

    private fun setupClickListener() {
        super.setOnClickListener {
            onClickListener?.invoke()
        }
    }

    private fun animateScale() {
        animate()
            .scaleX(1.1f)
            .scaleY(1.1f)
            .setDuration(200)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .withEndAction {
                animate()
                    .scaleX(1.0f)
                    .scaleY(1.0f)
                    .setDuration(200)
                    .start()
            }
            .start()
    }

}


//===================== Icon With Badge View Group =============================
//
//class IconWithBadgeView @JvmOverloads constructor(
//    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
//) : FrameLayout(context, attrs, defStyleAttr) {
//
//    private val icon: ImageView
//    private val badge: TextView
//
//    init {
//        // Inflate the layout
//        val view = LayoutInflater.from(context).inflate(R.layout.icon_with_badge, this, true)
//        icon = view.findViewById(R.id.ivIcon)
//        badge = view.findViewById(R.id.badge)
//
//        // Read the attrs
//        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.IconWithBadgeView, defStyleAttr, 0)
//        val iconResId = typedArray.getResourceId(R.styleable.IconWithBadgeView_iconSrc, 0)
//        if (iconResId != 0) {
//            setIcon(iconResId)
//        }
//        typedArray.recycle()
//    }
//
//    // Set the icon
//    fun setIcon(resId: Int) {
//        icon.setImageResource(resId)
//    }
//
//    // Set the badge count
//    fun setBadgeCount(count: Int) {
//        if (count > 0) {
//            badge.text = if (count > 99) "99+" else count.toString()
//            badge.visibility = VISIBLE
//        } else {
//            badge.visibility = GONE
//        }
//    }
//
//    // Set the badge visibility
//    fun setBadgeVisible(isVisible: Boolean) {
//        badge.visibility = if (isVisible) VISIBLE else GONE
//    }
//}