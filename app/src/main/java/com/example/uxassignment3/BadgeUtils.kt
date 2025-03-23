package com.example.uxassignment3

import android.content.Context
import android.util.Log
import android.view.View
import androidx.annotation.OptIn
import androidx.core.content.ContextCompat
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.badge.ExperimentalBadgeUtils
import com.google.android.material.button.MaterialButton

@OptIn(ExperimentalBadgeUtils::class)
object BadgeUtils {

    fun attachBadge(
        context: Context,
        button: MaterialButton,
        count: Int,
        backgroundColor: Int,
        textColor: Int
    ): BadgeDrawable {
        return BadgeDrawable.create(context).apply {
            number = count
            this.backgroundColor = ContextCompat.getColor(context, backgroundColor)
            badgeTextColor = ContextCompat.getColor(context, textColor)
        }
    }

    fun attachBadge(badgeDrawable: BadgeDrawable, view: View) {
        view.post {
            BadgeUtils.attachBadgeDrawable(badgeDrawable, view)
        }
    }

    fun updateBadgeCount(badge: BadgeDrawable?, count: Int) {
        badge?.number = count
    }
}