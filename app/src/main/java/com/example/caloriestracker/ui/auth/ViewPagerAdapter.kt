package com.example.caloriestracker.ui.auth

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.caloriestracker.ui.auth.login.LoginFragment
import com.example.caloriestracker.ui.auth.register.RegisterFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> LoginFragment()
            1 -> RegisterFragment()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }

    override fun getItemCount(): Int {
        return 2
    }
}
