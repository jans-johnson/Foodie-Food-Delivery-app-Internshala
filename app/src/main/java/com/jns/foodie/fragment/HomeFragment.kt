package com.jns.foodie.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.jns.foodie.R
import com.jns.foodie.adapter.HomeAdapter
import com.jns.foodie.database.RestaurantEntity
import com.jns.foodie.utils.ConnectionManager
import org.json.JSONException
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap


class HomeFragment() : Fragment() {

    lateinit var recyclerViewHome: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var homeAdapter: HomeAdapter
    lateinit var homeProgressBarLayout: RelativeLayout
    lateinit var cantFind: RelativeLayout
    lateinit var etSearch: EditText
    lateinit var radioButtonView: View

    var restaurantList= arrayListOf<RestaurantEntity>()
    val filteredList = arrayListOf<RestaurantEntity>()
    var filtered=0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)

        val view= inflater.inflate(R.layout.fragment_home, container, false)

        layoutManager=LinearLayoutManager(activity)
        recyclerViewHome=view.findViewById(R.id.recyclerViewHome)
        homeProgressBarLayout=view.findViewById(R.id.homeProgresBarLayout)
        cantFind=view.findViewById(R.id.cantFind)
        etSearch=view.findViewById(R.id.etSearch)

        etSearch.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(strTyped: Editable?) {
                searchFilter(strTyped.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })



        return view
    }



    override fun onResume()
    {

        if (ConnectionManager().checkConnectivity(activity as Context)) {
            if (restaurantList.isEmpty())
            {
                homeProgressBarLayout.visibility = View.VISIBLE
                try {
                    val queue = Volley.newRequestQueue(activity as Context)
                    val url = "http://13.235.250.119/v2/restaurants/fetch_result"

                    val jsonObjectRequest = object : JsonObjectRequest(
                            Method.GET,
                            url,
                            null,
                            Response.Listener {

                                val response = it.getJSONObject("data")
                                val success = response.getBoolean("success")
                                if (success) {

                                    val data = response.getJSONArray("data")
                                    for (i in 0 until data.length()) {
                                        val restaurantJsonObject = data.getJSONObject(i)
                                        val restaurantObject = RestaurantEntity(
                                                restaurantJsonObject.getString("id"),
                                                restaurantJsonObject.getString("name"),
                                                restaurantJsonObject.getString("rating"),
                                                restaurantJsonObject.getString("cost_for_one"),
                                                restaurantJsonObject.getString("image_url")
                                        )
                                        restaurantList.add(restaurantObject)
                                    }
                                    homeAdapter = HomeAdapter(activity as Context, restaurantList)
                                    recyclerViewHome.adapter = homeAdapter
                                    recyclerViewHome.layoutManager = layoutManager
                                }
                                homeProgressBarLayout.visibility = View.INVISIBLE
                            },
                            Response.ErrorListener {

                                homeProgressBarLayout.visibility = View.INVISIBLE

                                Toast.makeText(
                                        activity as Context,
                                        "Some Error occurred!!!",
                                        Toast.LENGTH_SHORT
                                ).show()
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
                    Toast.makeText(
                            activity as Context,
                            "Some Unexpected error occurred!!!",
                            Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.action_sort -> {
                etSearch.clearFocus()
                val restaurantList1: ArrayList<RestaurantEntity>
                if (filtered==1)
                    restaurantList1=filteredList
                else
                    restaurantList1=restaurantList

                radioButtonView = View.inflate(activity, R.layout.sort_menu, null)
                val radioGroup = radioButtonView.findViewById<RadioGroup>(R.id.groupradio)
                androidx.appcompat.app.AlertDialog.Builder(activity as Context)
                        .setTitle("Sort By?")
                        .setView(radioButtonView)
                        .setPositiveButton("OK") { _, _ ->
                            if (radioGroup.checkedRadioButtonId == R.id.radio_high_to_low) {
                                Collections.sort(restaurantList1, costComparator)
                                restaurantList1.reverse()
                                homeAdapter.notifyDataSetChanged()
                            }
                            if (radioGroup.checkedRadioButtonId == R.id.radio_low_to_high) {
                                Collections.sort(restaurantList1, costComparator)
                                homeAdapter.notifyDataSetChanged()
                            }
                            if (radioGroup.checkedRadioButtonId == R.id.radio_rating) {
                                Collections.sort(restaurantList1, ratingComparator)
                                restaurantList1.reverse()
                                homeAdapter.notifyDataSetChanged()
                            }
                        }
                        .setNegativeButton("Cancel") { _, _ ->

                        }
                        .create()
                        .show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun searchFilter(query: String)
    {
        filtered=1
        filteredList.clear()
        for (item in restaurantList) {
            if (item.restaurantName.toLowerCase(Locale.ROOT)
                            .contains(query.toLowerCase(Locale.ROOT))
            ) {
                filteredList.add(item)
            }
        }

        if (filteredList.size == 0) {
            cantFind.visibility = View.VISIBLE
        } else {
            cantFind.visibility = View.INVISIBLE
        }

        homeAdapter.filterList(filteredList)
    }


    //sort according to ratings
    var ratingComparator = Comparator<RestaurantEntity> { restaurant1, restaurant2 ->

        if (restaurant1.restaurantRating.compareTo(restaurant2.restaurantRating, true) == 0) {
            restaurant1.restaurantName.compareTo(restaurant2.restaurantName, true)
        } else {
            restaurant1.restaurantRating.compareTo(restaurant2.restaurantRating, true)
        }
    }

    //sort according to cost(decreasing)
    var costComparator = Comparator<RestaurantEntity> { restaurant1, restaurant2 ->

        restaurant1.restaurantCost.compareTo(restaurant2.restaurantCost, true)
    }
}