package com.jns.foodie.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.navigation.NavigationView
import com.jns.foodie.R
import com.jns.foodie.adapter.HistoryAdapter
import com.jns.foodie.model.OrderHistory
import com.jns.foodie.utils.ConnectionManager
import com.jns.foodie.utils.noInternetDialogBox
import com.jns.foodie.utils.responseErrorToast
import org.json.JSONException


class HistoryFragment(private val navigationView: NavigationView, private val supportFragmentManager: FragmentManager,private val supportActionBar: androidx.appcompat.app.ActionBar) : Fragment() {

    lateinit var recyclerViewOrderHistory: RecyclerView
    lateinit var historyProgressLayout: RelativeLayout
    lateinit var noOrdersLayout: RelativeLayout
    lateinit var btnBrowseRestaurant: Button
    lateinit var sharedPreferences:SharedPreferences
    lateinit var userId: String
    lateinit var historyAdapter: HistoryAdapter
    lateinit var layoutManager: RecyclerView.LayoutManager

    var orderList= arrayListOf<OrderHistory>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_history, container, false)

        recyclerViewOrderHistory=view.findViewById(R.id.RecyclerViewOrderHistory)
        historyProgressLayout=view.findViewById(R.id.historyProgressLayout)
        noOrdersLayout=view.findViewById(R.id.noOrdersLayout)
        layoutManager= LinearLayoutManager(activity)
        btnBrowseRestaurant=view.findViewById(R.id.btnBrowseRestaurant)

        btnBrowseRestaurant.setOnClickListener {
            supportFragmentManager.beginTransaction()
                    .replace(
                            R.id.frameLayout,
                            HomeFragment()
                    ).commit()
            navigationView.setCheckedItem(R.id.itemHome)
            supportActionBar.title="All Restaurants"
        }


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
                        if (orderList.isEmpty())
                            noOrdersLayout.visibility=View.VISIBLE

                        historyAdapter=HistoryAdapter(activity as Context,orderList)
                        recyclerViewOrderHistory.adapter=historyAdapter
                        recyclerViewOrderHistory.layoutManager=layoutManager
                    }
                    historyProgressLayout.visibility=View.INVISIBLE
                },
                Response.ErrorListener {
                    historyProgressLayout.visibility = View.INVISIBLE

                    responseErrorToast(activity as Context,it.toString())
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
                Toast.makeText(activity,"Some Error Occurred",Toast.LENGTH_SHORT).show()
            }
        }
        else {

            val alterDialog = noInternetDialogBox(activity as Context)
            alterDialog.show()
        }

        super.onResume()
    }
}