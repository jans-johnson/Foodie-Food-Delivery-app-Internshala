package com.jns.foodie.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.jns.foodie.R
import com.jns.foodie.utils.ConnectionManager
import org.json.JSONException
import org.json.JSONObject

class ForgotPasswordFragment(val fm: FragmentManager) : Fragment() {

    lateinit var btnNext: Button
    lateinit var etPhoneFP: EditText
    lateinit var etEmailAddressFP: EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_forgot_password, container, false)


        btnNext=view.findViewById(R.id.btnNext)
        etPhoneFP=view.findViewById(R.id.etPhone)
        etEmailAddressFP=view.findViewById(R.id.etEmailAddress)



        btnNext.setOnClickListener {

            if(etPhoneFP.text.length<10)
                etPhoneFP.error="Enter a valid Mobile Number"
            else
                sendRequest()

        }

        return view
    }

    fun sendRequest()
    {
        if(ConnectionManager().checkConnectivity(activity as Context))
        {
            val details = JSONObject()

            details.put("mobile_number",etPhoneFP.text)
            details.put("email",etEmailAddressFP.text)

            val queue=Volley.newRequestQueue(activity as Context)
            val url="http://13.235.250.119/v2/forgot_password/fetch_result"

            val jsonObjectRequest=object : JsonObjectRequest(
                Method.POST,
                url,
                details,
            Response.Listener {
                try {
                    val response=it.getJSONObject("data")
                    val success = response.getBoolean("success")
                    if (success)
                    {
                        val first=response.getBoolean("first_try")
                        if (first)
                        {
                            Toast.makeText(activity,"OTP sent to email", Toast.LENGTH_SHORT).show()
                            fm.beginTransaction()
                                .replace(
                                    R.id.frameForgotPassword,
                                    ResetPasswordFragment(etPhoneFP.text.toString())
                                ).commit()
                        }
                        else
                        {
                            Toast.makeText(activity,"OTP already sent Once", Toast.LENGTH_SHORT).show()
                            fm.beginTransaction()
                                .replace(
                                    R.id.frameForgotPassword,
                                    ResetPasswordFragment(etPhoneFP.text.toString())
                                ).commit()
                        }
                    }
                    else
                    {
                        val error=response.getString("errorMessage")
                        if (error.equals("No user found!"))
                        {
                            Toast.makeText(activity,"User not Found !", Toast.LENGTH_SHORT).show()
                        }
                        else if(error.equals("Invalid data"))
                        {
                            Toast.makeText(activity,"Invalid Data", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                catch (e: JSONException)
                {
                    Toast.makeText(activity, "Some unexpected error occurred!!", Toast.LENGTH_SHORT).show()
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
    }

}
}