package com.jns.foodie.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.widget.Toolbar
import com.jns.foodie.R
import com.jns.foodie.fragment.ForgotPasswordFragment

class ForgotPasswordActivity : AppCompatActivity() {
    lateinit var frameForgotPassword: FrameLayout
    lateinit var tbForgotPassword: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        frameForgotPassword = findViewById(R.id.frameForgotPassword)

        tbForgotPassword = findViewById(R.id.tbForgotPassword)

        setSupportActionBar(tbForgotPassword)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Forgot Password"

        supportFragmentManager.beginTransaction()
            .replace(
                R.id.frameForgotPassword,
                ForgotPasswordFragment(supportFragmentManager)
            ).commit()


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }
}