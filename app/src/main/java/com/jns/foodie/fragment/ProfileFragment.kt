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
import android.widget.Toast
import com.amulyakhare.textdrawable.TextDrawable
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.jns.foodie.R
import com.jns.foodie.utils.ConnectionManager
import com.jns.foodie.utils.noInternetDialogBox
import com.jns.foodie.utils.responseErrorToast
import org.json.JSONException
import java.util.*


class ProfileFragment : Fragment() {

    lateinit var ivProfile: ImageView
    lateinit var tvProfileName: TextView
    lateinit var tvProfileMobile: TextView
    lateinit var tvProfileEmail: TextView
    lateinit var tvProfileAddress: TextView
    lateinit var tvTotalOrders: TextView
    var total = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        ivProfile = view.findViewById(R.id.ivProfile)
        tvProfileName = view.findViewById(R.id.tvProfileName)
        tvProfileMobile = view.findViewById(R.id.tvProfileMobile)
        tvProfileEmail = view.findViewById(R.id.tvProfileEmail)
        tvProfileAddress = view.findViewById(R.id.tvProfileAddress)
        tvTotalOrders = view.findViewById(R.id.tvTotalOrders)

        val sharedPreferences = activity?.getSharedPreferences("UserDetails", Context.MODE_PRIVATE)

        tvProfileName.text = sharedPreferences?.getString("name", "UserName")
        tvProfileEmail.text = sharedPreferences?.getString("email", "UserEmail")
        tvProfileMobile.text = sharedPreferences?.getString("mobile_number", "mobile")
        tvProfileAddress.text = sharedPreferences?.getString("address", "address")
        "Total Orders :\n...".also { tvTotalOrders.text = it }

        val textDrawable = TextDrawable.builder().buildRound(
            tvProfileName.text[0].toString()
                .toUpperCase(Locale.ROOT), Color.rgb(11, 94, 74)
        )
        ivProfile.setImageDrawable(textDrawable)

        return view
    }

    override fun onResume() {
        val sharedPreferences =
            activity?.getSharedPreferences("UserDetails", Context.MODE_PRIVATE)!!
        val userId = sharedPreferences.getString("user_id", "user_id")!!

        if (ConnectionManager().checkConnectivity(activity as Context)) {

            try {
                val queue = Volley.newRequestQueue(activity as Context)
                val url = "http://13.235.250.119/v2/orders/fetch_result/$userId"

                val jsonObjectRequest = object : JsonObjectRequest(
                    Method.GET, url, null,
                    Response.Listener { its ->
                        val response = its.getJSONObject("data")
                        if (response.getBoolean("success")) {
                            val data = response.getJSONArray("data")
                            total = data.length()
                            "Total Orders :\n$total".also { tvTotalOrders.text = it }
                        }
                    },
                    Response.ErrorListener {

                        responseErrorToast(activity as Context, it.toString())
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
                Toast.makeText(activity, "Some Error Occurred", Toast.LENGTH_SHORT).show()
            }
        } else {

            val alterDialog = noInternetDialogBox(activity as Context)
            alterDialog.show()
        }

        super.onResume()
    }

}