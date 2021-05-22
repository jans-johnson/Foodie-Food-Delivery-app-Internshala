package com.jns.foodie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabItem
import com.google.android.material.tabs.TabLayout

class LoginActivity : AppCompatActivity() {
    lateinit var tlLogin: TabLayout
    lateinit var vpLogin: ViewPager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        tlLogin=findViewById(R.id.tlLogin)
        vpLogin=findViewById(R.id.vpLogin)
    }
}