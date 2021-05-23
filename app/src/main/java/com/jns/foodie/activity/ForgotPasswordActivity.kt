package com.jns.foodie.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import com.jns.foodie.R

class ForgotPasswordActivity : AppCompatActivity() {
    lateinit var tbForgotPassword:Toolbar
    lateinit var btnNext:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        tbForgotPassword=findViewById(R.id.tbForgotPassword)
        btnNext=findViewById(R.id.btnNext)

        setSupportActionBar(tbForgotPassword)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle("Forgot Password")

        btnNext.setOnClickListener {
            val intent=Intent(this,ResetPasswordActivity::class.java)
            startActivity(intent)
        }
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