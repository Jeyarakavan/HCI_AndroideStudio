package com.example.expensetracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        val tabs = findViewById<TabLayout>(R.id.tabLayout)

        val adapter = AuthPagerAdapter(this)
        viewPager.adapter = adapter

        // Improve swipe animation with a depth transformer and keep one page offscreen
        viewPager.setPageTransformer(DepthPageTransformer())
        viewPager.offscreenPageLimit = 1

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Login"
                else -> "Sign Up"
            }
        }.attach()

        // Visual tweaks for tabs (colors set via theme)
        tabs.setSelectedTabIndicatorColor(getColor(R.color.brand_accent))
    }
}
