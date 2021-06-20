package com.jns.foodie.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.jns.foodie.R
import com.jns.foodie.activity.ForgotPasswordActivity
import com.jns.foodie.activity.LoginActivity
import com.jns.foodie.activity.MainActivity
import com.jns.foodie.utils.ConnectionManager
import com.jns.foodie.utils.noInternetDialogBox
import org.json.JSONException
import org.json.JSONObject


class LoginFragment : Fragment() {

    lateinit var tvForgotPassword: TextView
    lateinit var tvSignup:TextView
    lateinit var etMobile: EditText
    lateinit var etPassword:EditText
    lateinit var btnLogin: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_login, container, false)

        etMobile=view.findViewById(R.id.etMobile)
        etPassword=view.findViewById(R.id.etPassword)
        tvForgotPassword=view.findViewById(R.id.tvForgotPassword)
        tvSignup=view.findViewById(R.id.tvSignup)
        btnLogin=view.findViewById(R.id.btnLogin)

        btnLogin.setOnClickListener {

            if(etMobile.text.length<10)
                etMobile.error="Enter a valid Mobile Number"
            else if (ConnectionManager().checkConnectivity(activity as Context))
                loginUser()
            else
                Toast.makeText(activity,"Check Your Connection and try again !",Toast.LENGTH_SHORT).show()
        }

        tvForgotPassword.setOnClickListener {
            val intent=Intent(activity as Context,ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        tvSignup.setOnClickListener {
            (activity as LoginActivity?)?.selectSignup()        //calling method inside LoginActivity
        }

        return view
    }
    private fun loginUser()
    {
        val sharedPreferences= activity?.getSharedPreferences(
            "UserDetails",
            Context.MODE_PRIVATE
        )
        if(ConnectionManager().checkConnectivity(activity as Context)) {
                val loginUser = JSONObject()
                val dialogView = LayoutInflater.from(activity).inflate(R.layout.loading_dialog, null)
                val builder = AlertDialog.Builder(activity).setView(dialogView).show()
                builder.setCanceledOnTouchOutside(false)

                loginUser.put("mobile_number", etMobile.text)
                loginUser.put("password", etPassword.text)

                val queue = Volley.newRequestQueue(activity as Context)
                val url = "http://13.235.250.119/v2/login/fetch_result/"

                val jsonObjectRequest = object : JsonObjectRequest(
                    Method.POST,
                    url,
                    loginUser,
                    Response.Listener {
                        try {
                            builder.dismiss()
                            val response = it.getJSONObject("data")
                            val success = response.getBoolean("success")
                            if (success) {
                                val data = response.getJSONObject("data")
                                sharedPreferences?.edit()?.putBoolean("isLoggedIn", true)?.apply()
                                sharedPreferences?.edit()
                                    ?.putString("user_id", data.getString("user_id"))?.apply()
                                sharedPreferences?.edit()?.putString("name", data.getString("name"))
                                    ?.apply()
                                sharedPreferences?.edit()
                                    ?.putString("email", data.getString("email"))
                                    ?.apply()
                                sharedPreferences?.edit()
                                    ?.putString("mobile_number", data.getString("mobile_number"))
                                    ?.apply()
                                sharedPreferences?.edit()
                                    ?.putString("address", data.getString("address"))?.apply()

                                val intent = Intent(activity, MainActivity::class.java)
                                startActivity(intent)
                                Toast.makeText(activity, "Login Successful", Toast.LENGTH_SHORT)
                                    .show()
                                activity?.finish()
                            } else {
                                val errorMessage = response.getString("errorMessage")
                                if (errorMessage.equals("Mobile Number not registered"))
                                {
                                    etMobile.error = "Please enter your Registered mobile Number"
                                    Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show()
                                }
                                else if (errorMessage.equals("Incorrect password"))
                                {
                                    etPassword.error = "Wrong Password"
                                    Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show()
                                }

                            }
                        } catch (e: JSONException) {
                            Toast.makeText(activity, "Some unexpected error occurred!!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    Response.ErrorListener {
                        builder.dismiss()
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
            }else {

            val alterDialog = noInternetDialogBox(activity as Context)
            alterDialog.show()
        }
        }
    }
