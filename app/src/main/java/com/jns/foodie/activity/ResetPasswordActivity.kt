package com.jns.foodie.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.jns.foodie.R

class ResetPasswordActivity : AppCompatActivity() {

    lateinit var tbResetPassword: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        tbResetPassword=findViewById(R.id.tbResetPassword)
        setSupportActionBar(tbResetPassword)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle("Forgot Password")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId)
        {
            android.R.id.home->{onBackPressed()
                return true}

        }
        return super.onOptionsItemSelected(item)
    }
}