package com.example.caloriestracker.ui.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.caloriestracker.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        val tabLayout: TabLayout = findViewById(R.id.tab_layout)

        val viewPagerAdapter = ViewPagerAdapter(this)
        viewPager.adapter = viewPagerAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Login"
                1 -> "Register"
                else -> throw IllegalArgumentException("Invalid position: $position")
            }
        }.attach()
    }
}

