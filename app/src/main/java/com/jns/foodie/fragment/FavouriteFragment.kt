package com.jns.foodie.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.jns.foodie.R
import com.jns.foodie.adapter.HomeAdapter
import com.jns.foodie.database.RestaurantDatabase
import com.jns.foodie.database.RestaurantEntity
import com.jns.foodie.model.Restaurant
import com.jns.foodie.utils.ConnectionManager
import org.json.JSONException

class FavouriteFragment : Fragment() {

    lateinit var recyclerViewFavourite: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var favoriteAdapter: HomeAdapter
    lateinit var favoriteProgressLayout: RelativeLayout
    var restaurantInfoList = arrayListOf<Restaurant>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favourite, container, false)

        layoutManager=LinearLayoutManager(activity)
        recyclerViewFavourite=view.findViewById(R.id.recyclerViewFavourite)
        favoriteProgressLayout=view.findViewById(R.id.favoriteProgressLayout)

        return view
    }

    override fun onResume() {

        if (ConnectionManager().checkConnectivity(activity as Context)) {
            favoriteProgressLayout.visibility = View.VISIBLE
            try {
                val queue = Volley.newRequestQueue(activity as Context)
                val url = "http://13.235.250.119/v2/restaurants/fetch_result/"
                val jsonObjectRequest = object : JsonObjectRequest(
                        Method.GET,
                        url,
                        null,
                        Response.Listener {

                            val response = it.getJSONObject("data")
                            val success = response.getBoolean("success")

                            if (success) {

                                restaurantInfoList.clear()
                                val data = response.getJSONArray("data")
                                for (i in 0 until data.length()) {
                                    val restaurantJsonObject = data.getJSONObject(i)
                                    val restaurantEntity = RestaurantEntity(
                                            restaurantJsonObject.getString("id"),
                                            restaurantJsonObject.getString("name")
                                    )

                                    if (DBAsyncTask(activity as Context, restaurantEntity).execute().get())
                                    {
                                        val restaurantObject = Restaurant(
                                                restaurantJsonObject.getString("id"),
                                                restaurantJsonObject.getString("name"),
                                                restaurantJsonObject.getString("rating"),
                                                restaurantJsonObject.getString("cost_for_one"),
                                                restaurantJsonObject.getString("image_url")
                                        )

                                        restaurantInfoList.add(restaurantObject)
                                        favoriteAdapter = HomeAdapter(activity as Context, restaurantInfoList)
                                        recyclerViewFavourite.adapter = favoriteAdapter
                                        recyclerViewFavourite.layoutManager = layoutManager
                                    }
                                }
                                if (restaurantInfoList.size == 0) {
                                    Toast.makeText(
                                            activity as Context,
                                            "Nothing is added to Favorites",
                                            Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                            favoriteProgressLayout.visibility = View.INVISIBLE
                        },
                        Response.ErrorListener {
                            println("Error12 is $it")

                            Toast.makeText(
                                    activity as Context,
                                    "Some Error occurred!!!",
                                    Toast.LENGTH_SHORT
                            ).show()

                            favoriteProgressLayout.visibility = View.INVISIBLE
                        }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "26c5144c5b9c13"
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


    class DBAsyncTask(val context: Context,val restaurantEntity: RestaurantEntity) :
            AsyncTask<Void, Void, Boolean>() {

        val db = Room.databaseBuilder(context, RestaurantDatabase::class.java, "restaurant-db").build()

        override fun doInBackground(vararg p0: Void?): Boolean {
                    val restaurant: RestaurantEntity? = db.restaurantDao()
                            .getRestaurantById(restaurantEntity.restaurantId)
                    db.close()
                    return restaurant != null
            }
        }

    }
