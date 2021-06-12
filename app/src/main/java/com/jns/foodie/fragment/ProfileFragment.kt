package com.jns.foodie.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.amulyakhare.textdrawable.TextDrawable
import com.jns.foodie.R
import java.util.*


class ProfileFragment() : Fragment() {

    lateinit var ivProfile: ImageView
    lateinit var tvProfileName: TextView
    lateinit var tvProfileMobile: TextView
    lateinit var tvProfileEmail: TextView
    lateinit var tvProfileAddress: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_profile, container, false)

        ivProfile=view.findViewById(R.id.ivProfile)
        tvProfileName=view.findViewById(R.id.tvProfileName)
        tvProfileMobile=view.findViewById(R.id.tvProfileMobile)
        tvProfileEmail=view.findViewById(R.id.tvProfileEmail)
        tvProfileAddress=view.findViewById(R.id.tvProfileAddress)

        val sharedPreferences= activity?.getSharedPreferences("UserDetails", Context.MODE_PRIVATE)

        tvProfileName.text=sharedPreferences?.getString("name","UserName")
        tvProfileEmail.text=sharedPreferences?.getString("email","UserEmail")
        tvProfileMobile.text=sharedPreferences?.getString("mobile_number","mobile")
        tvProfileAddress.text=sharedPreferences?.getString("address","address")

        val textDrawable=TextDrawable.builder().buildRound(tvProfileName.text[0].toString()
            .toUpperCase(Locale.ROOT), Color.rgb(11, 94, 74))
        ivProfile.setImageDrawable(textDrawable)

        return view
    }

}