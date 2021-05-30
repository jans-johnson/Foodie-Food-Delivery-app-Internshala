package com.jns.foodie.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.jns.foodie.R
import com.jns.foodie.activity.MainActivity
import com.jns.foodie.utils.ConnectionManager
import org.json.JSONException
import org.json.JSONObject


class SignupFragment : Fragment() {
    lateinit var etRegName: EditText
    lateinit var etRegEmail: EditText
    lateinit var etRegPhone: EditText
    lateinit var etRegAddress: EditText
    lateinit var etRegPassword: EditText
    lateinit var etRegCPassword: EditText
    lateinit var btnRegister: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_signup, container, false)
        etRegName=view.findViewById(R.id.etRegName)
        etRegEmail=view.findViewById(R.id.etRegEmail)
        etRegPhone=view.findViewById(R.id.etRegPhone)
        etRegAddress=view.findViewById(R.id.etRegAddress)
        etRegPassword=view.findViewById(R.id.etRegPassword)
        etRegCPassword=view.findViewById(R.id.etRegCPassword )
        btnRegister=view.findViewById(R.id.btnRegister)

        btnRegister.setOnClickListener {
            if (etRegName.text.isBlank())
                etRegName.error = "Enter Your Name"
            else if (etRegEmail.text.isBlank())
                etRegEmail.error = "Enter Your Email"
            else if (etRegPhone.text.length < 10)
                etRegPhone.error = "Enter a valid Phone Number"
            else if (etRegAddress.text.isBlank())
                etRegAddress.error = "Enter Your Address"
            else if (etRegPassword.length() < 5)
                etRegPassword.error = "Password Length Should be more than 4"
            else if (!etRegCPassword.text.toString().equals(etRegPassword.text.toString()))
                etRegCPassword.error = "Passwords Do not Match"
            else {
                val sharedPreferences = activity?.getSharedPreferences(
                        "UserDetails",
                        Context.MODE_PRIVATE
                )

                if (ConnectionManager().checkConnectivity(activity as Context)) {
                    try {
                        val registerUser = JSONObject()
                        registerUser.put("name", etRegName.text.toString())
                        registerUser.put("mobile_number", etRegPhone.text.toString())
                        registerUser.put("password", etRegPassword.text.toString())
                        registerUser.put("address", etRegAddress.text.toString())
                        registerUser.put("email", etRegEmail.text.toString())

                        val queue = Volley.newRequestQueue(activity as Context)

                        val url = "http://13.235.250.119/v2/register/fetch_result"

                        val jsonObjectRequest = object : JsonObjectRequest(Method.POST, url, registerUser, Response.Listener {
                            val response = it.getJSONObject("data")
                            if (response.getBoolean("success")) {
                                val data = response.getJSONObject("data")
                                sharedPreferences?.edit()?.putBoolean("isLoggedIn", true)?.apply()

                                sharedPreferences?.edit()?.putString("user_id", data.getString("user_id"))?.apply()
                                sharedPreferences?.edit()?.putString("name", data.getString("name"))?.apply()
                                sharedPreferences?.edit()?.putString("email", data.getString("email"))?.apply()
                                sharedPreferences?.edit()?.putString("mobile_number", data.getString("mobile_number"))?.apply()
                                sharedPreferences?.edit()?.putString("address", data.getString("address"))?.apply()

                                val intent = Intent(activity, MainActivity::class.java)
                                startActivity(intent)

                                Toast.makeText(activity, "Registered Successfully", Toast.LENGTH_SHORT).show()
                                activity?.finish()
                            } else {
                                val responseMessageServer =
                                        response.getString("errorMessage")
                                Toast.makeText(activity, responseMessageServer, Toast.LENGTH_SHORT).show()
                            }
                        },
                                Response.ErrorListener {
                                    Toast.makeText(activity, "Some Error occurred!!", Toast.LENGTH_SHORT).show()
                                }) {
                            override fun getHeaders(): MutableMap<String, String> {
                                val headers = HashMap<String, String>()
                                headers["Content-type"] = "application/json"
                                headers["token"] = "c1b1256d960512"
                                return headers
                            }
                        }

                        queue.add(jsonObjectRequest)
                    } catch (e: JSONException) {
                        Toast.makeText(activity, "Some unexpected error occurred!!!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        return view
    }

}