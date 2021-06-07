package com.jns.foodie.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.jns.foodie.R

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        val sharedPreferences=getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed({
            if (!sharedPreferences.getBoolean("isNotFirstTime",false))
            {
                intent = Intent(this, OnBoardingActivity::class.java)
                startActivity(intent)
            }
            else if (sharedPreferences.getBoolean("isLoggedIn", false)) {
                intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }else {
                intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
            finish()
        }, 1000)



        }
}