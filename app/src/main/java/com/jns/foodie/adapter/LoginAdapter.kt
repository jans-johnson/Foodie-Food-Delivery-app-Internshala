package com.jns.foodie.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.jns.foodie.fragment.LoginFragment
import com.jns.foodie.fragment.SignupFragment

class LoginAdapter(fm: FragmentManager, private var totalTabs: Int):FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return totalTabs
    }

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> {
                return LoginFragment()
            }
            1 -> {
                return SignupFragment()
            }
        }
        throw IllegalStateException("position $position is invalid for this viewpager")
    }

}