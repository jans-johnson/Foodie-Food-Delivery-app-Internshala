package com.jns.foodie.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.jns.foodie.R
import com.jns.foodie.activity.LoginActivity
import com.jns.foodie.utils.ConnectionManager
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Method


class ResetPasswordFragment(val mobileNumber: String) : Fragment() {

    lateinit var etOtp: TextView
    lateinit var etPasswordFP: TextView
    lateinit var etCPasswordFP: TextView
    lateinit var btnSubmit: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_reset_password, container, false)

        etOtp=view.findViewById(R.id.etOtp)
        etPasswordFP=view.findViewById(R.id.etPasswordFP)
        etCPasswordFP=view.findViewById(R.id.etCPasswordFP)
        btnSubmit=view.findViewById(R.id.btnSubmit)

        btnSubmit.setOnClickListener {
            if (etOtp.text.isEmpty()) {
                etOtp.error = "Enter OTP to continue"
            } else if (etPasswordFP.text.length < 5)
                etPasswordFP.error = "Password Length Should be more than 5"
            else if (!etPasswordFP.text.toString().equals(etCPasswordFP.text.toString())) {
                etCPasswordFP.error = "Passwords Do not match"
            } else {
                if (ConnectionManager().checkConnectivity(activity as Context)) {
                    try {
                        val details = JSONObject()
                        details.put("mobile_number", mobileNumber)
                        details.put("password", etPasswordFP.text.toString())
                        details.put("otp", etOtp.text.toString())

                        val queue = Volley.newRequestQueue(activity as Context)

                        val url = "http://13.235.250.119/v2/reset_password/fetch_result"

                        val jsonObjectRequest = object : JsonObjectRequest(Method.POST, url, details,
                                Response.Listener {
                                    val response = it.getJSONObject("data")
                                    if (response.getBoolean("success")) {
                                        Toast.makeText(activity, "Password Changed Successfully", Toast.LENGTH_SHORT).show()
                                        val intent = Intent(activity, LoginActivity::class.java)
                                        startActivity(intent)
                                        activity?.finish()
                                    }
                                    else
                                    {
                                        val error=response.getString("errorMessage")
                                        Toast.makeText(activity,error,Toast.LENGTH_SHORT).show()
                                    }
                                },
                                Response.ErrorListener {
                                    Toast.makeText(activity, "Unexpected Error Occured", Toast.LENGTH_SHORT).show()
                                }) {
                            override fun getHeaders(): MutableMap<String, String> {
                                val headers = HashMap<String, String>()
                                headers["Content-type"] = "application/json"
                                headers["token"] = "c1b1256d960512"
                                return headers
                            }
                        }
                        queue.add(jsonObjectRequest)
                    }catch (e: JSONException) {
                        Toast.makeText(activity, "Some unexpected error occurred!!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        return view
    }

}