package com.example.expensetracker

import android.view.View
import androidx.viewpager2.widget.ViewPager2

class DepthPageTransformer : ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        page.apply {
            when {
                position <= -1f || position >= 1f -> {
                    alpha = 0f
                }
                position == 0f -> {
                    alpha = 1f
                    translationX = 0f
                    scaleX = 1f
                    scaleY = 1f
                }
                else -> {
                    // Fade the page out.
                    alpha = 1 - kotlin.math.abs(position)

                    // Counteract the default slide transition
                    translationX = width * -position

                    // Scale the page down (between MIN_SCALE and 1)
                    val MIN_SCALE = 0.85f
                    val scaleFactor = (MIN_SCALE + (1 - MIN_SCALE) * (1 - kotlin.math.abs(position)))
                    scaleX = scaleFactor
                    scaleY = scaleFactor
                }
            }
        }
    }
}

