package com.example.uxassignment3

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView

class IconWithBadgeView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val icon: ImageView
    private val badge: TextView

    init {
        // Inflate the layout
        val view = LayoutInflater.from(context).inflate(R.layout.icon_with_badge, this, true)
        icon = view.findViewById(R.id.ivIcon)
        badge = view.findViewById(R.id.badge)

        // Read the attrs
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.IconWithBadgeView, defStyleAttr, 0)
        val iconResId = typedArray.getResourceId(R.styleable.IconWithBadgeView_iconSrc, 0)
        if (iconResId != 0) {
            setIcon(iconResId)
        }
        typedArray.recycle()
    }

    // Set the icon
    fun setIcon(resId: Int) {
        icon.setImageResource(resId)
    }

    // Set the badge count
    fun setBadgeCount(count: Int) {
        if (count > 0) {
            badge.text = if (count > 99) "99+" else count.toString()
            badge.visibility = VISIBLE
        } else {
            badge.visibility = GONE
        }
    }

    // Set the badge visibility
    fun setBadgeVisible(isVisible: Boolean) {
        badge.visibility = if (isVisible) VISIBLE else GONE
    }
}