package com.jns.foodie.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.jns.foodie.R
import com.jns.foodie.adapter.LoginAdapter

class LoginActivity : AppCompatActivity() {
    lateinit var tlLogin: TabLayout
    lateinit var vpLogin: ViewPager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        tlLogin=findViewById(R.id.tlLogin)
        vpLogin=findViewById(R.id.vpLogin)

        tlLogin.addTab(tlLogin.newTab().setText("LOGIN"))
        tlLogin.addTab(tlLogin.newTab().setText("SIGNUP"))
        tlLogin.tabGravity=TabLayout.GRAVITY_FILL

        val adapter=LoginAdapter(this,supportFragmentManager,tlLogin.tabCount)
        vpLogin.adapter=adapter

        vpLogin.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tlLogin))


        tlLogin.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    vpLogin.currentItem=tab.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }
    fun selectSignup()
    {
        val tab = tlLogin.getTabAt(1)
        tab?.select()
    }
}