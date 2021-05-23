package com.jns.foodie.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.jns.foodie.R
import com.jns.foodie.activity.ForgotPasswordActivity
import com.jns.foodie.activity.LoginActivity


class LoginFragment : Fragment() {

    lateinit var tvForgotPassword: TextView
    lateinit var tvSignup:TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_login, container, false)

        tvForgotPassword=view.findViewById(R.id.tvForgotPassword)
        tvSignup=view.findViewById(R.id.tvSignup)

        tvForgotPassword.setOnClickListener {
            val intent=Intent(activity as Context,ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        tvSignup.setOnClickListener {
            (activity as LoginActivity?)?.selectSignup()        //calling method inside LoginActivity
        }

        return view
    }

}