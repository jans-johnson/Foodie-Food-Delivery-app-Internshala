package com.jns.foodie.fragment

import android.app.Activity
import android.content.AbstractThreadedSyncAdapter
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.jns.foodie.R
import com.jns.foodie.adapter.HistoryAdapter
import com.jns.foodie.model.OrderHistory
import com.jns.foodie.model.Restaurant
import com.jns.foodie.utils.ConnectionManager
import org.json.JSONException


class HistoryFragment : Fragment() {

    lateinit var recyclerViewOrderHistory: RecyclerView
    lateinit var historyProgressLayout: RelativeLayout
    lateinit var sharedPreferences:SharedPreferences
    lateinit var userId: String
    lateinit var historyAdapter: HistoryAdapter
    lateinit var layoutManager: RecyclerView.LayoutManager

    var orderList= arrayListOf<OrderHistory>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_history, container, false)

        recyclerViewOrderHistory=view.findViewById(R.id.RecyclerViewOrderHistory)
        historyProgressLayout=view.findViewById(R.id.historyProgressLayout)
        layoutManager= LinearLayoutManager(activity)


        return view
    }

    override fun onResume() {
        sharedPreferences= activity?.getSharedPreferences("UserDetails",Context.MODE_PRIVATE)!!
        userId =sharedPreferences.getString("user_id","user_id")!!

        if (ConnectionManager().checkConnectivity(activity as Context))
        {
            historyProgressLayout.visibility=View.VISIBLE

            try {
                val queue=Volley.newRequestQueue(activity as Context)
                val url="http://13.235.250.119/v2/orders/fetch_result/$userId"

                val jsonObjectRequest=object : JsonObjectRequest(Method.GET,url,null,
                Response.Listener {
                    val response=it.getJSONObject("data")
                    if (response.getBoolean("success")) {
                        val data = response.getJSONArray("data")
                        for (i in 0 until data.length()) {
                            val orderJsonObject=data.getJSONObject(i)
                            val orderObject=OrderHistory(
                                    orderJsonObject.getString("order_id"),
                                    orderJsonObject.getString("restaurant_name"),
                                    orderJsonObject.getString("total_cost"),
                                    orderJsonObject.getString("order_placed_at"),
                                    orderJsonObject.getJSONArray("food_items")
                            )
                            orderList.add(orderObject)
                        }
                        historyAdapter=HistoryAdapter(activity as Context,orderList)
                        recyclerViewOrderHistory.adapter=historyAdapter
                        recyclerViewOrderHistory.layoutManager=layoutManager
                    }
                    historyProgressLayout.visibility=View.INVISIBLE
                },
                Response.ErrorListener {
                    historyProgressLayout.visibility = View.INVISIBLE

                    Toast.makeText(activity as Context, "Some Error occurred!!!", Toast.LENGTH_SHORT).show()
                })
                {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "c1b1256d960512"
                        return headers
                    }
                }
                queue.add(jsonObjectRequest)
                historyProgressLayout.visibility=View.INVISIBLE
            }
            catch (e:JSONException)
            {
                Toast.makeText(activity,"Some Error Occured",Toast.LENGTH_SHORT).show()
            }
        }
        else {

            val alterDialog = androidx.appcompat.app.AlertDialog.Builder(activity as Context)
            alterDialog.setTitle("No Internet")
            alterDialog.setMessage("Check Internet Connection!")
            alterDialog.setPositiveButton("Open Settings") { _, _ ->
                val settingsIntent = Intent(Settings.ACTION_SETTINGS)
                startActivity(settingsIntent)
            }
            alterDialog.setNegativeButton("Exit") { _, _ ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            alterDialog.setCancelable(false)
            alterDialog.create()
            alterDialog.show()
        }

        super.onResume()
    }
}