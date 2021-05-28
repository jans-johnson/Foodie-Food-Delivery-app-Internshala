package com.jns.foodie.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.jns.foodie.R
import com.jns.foodie.adapter.MenuAdapter
import com.jns.foodie.model.RestaurantMenu
import com.jns.foodie.utils.ConnectionManager
import org.json.JSONException

class RestaurantMenuActivity : AppCompatActivity() {

    lateinit var toolbarMenu: Toolbar
    lateinit var recyclerViewMenu: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var ivFavIcon:ImageView
    lateinit var tvRating:TextView
    lateinit var btnProceedToCart: Button
    lateinit var menuProgressLayout:RelativeLayout
    lateinit var menuAdapter: MenuAdapter

    lateinit var restaurantId: String
    lateinit var restaurantName: String
    var restaurantMenuList = arrayListOf<RestaurantMenu>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_menu)

        ivFavIcon=findViewById(R.id.ivFavIcon)
        tvRating=findViewById(R.id.tvRating)
        toolbarMenu=findViewById(R.id.toolbarMenu)
        btnProceedToCart=findViewById(R.id.btnProceedToCart)
        menuProgressLayout=findViewById(R.id.menuProgressLayout)
        recyclerViewMenu=findViewById(R.id.RecyclerViewMenu)
        layoutManager = LinearLayoutManager(this)

        restaurantId = intent.getStringExtra("restaurantId")!!
        restaurantName = intent.getStringExtra("restaurantName")!!
        val restaurantRating=intent.getStringExtra("restaurantRating")
        val fav=intent.getBooleanExtra("isFav",false)
        if (fav)
        {
            ivFavIcon.setImageResource(R.drawable.ic_favourite_fill)
        }
        tvRating.text="Rated $restaurantRating"

        setToolBar()

    }

    //Once items added to cart, If user press back , items will be cleared
    override fun onBackPressed() {
        if (menuAdapter.getSelectedItemCount() > 0) {

            val alterDialog = androidx.appcompat.app.AlertDialog.Builder(this)
            alterDialog.setTitle("Alert!")
            alterDialog.setMessage("Going back will remove everything from cart")
            alterDialog.setPositiveButton("Okay") { _, _ ->
                super.onBackPressed()
            }
            alterDialog.setNegativeButton("Cancel") { _, _ ->

            }
            alterDialog.show()
        } else {
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> {
                if (menuAdapter.getSelectedItemCount() > 0) {
                    val alterDialog = androidx.appcompat.app.AlertDialog.Builder(this)
                    alterDialog.setTitle("Alert!")
                    alterDialog.setMessage("Going back will remove everything from cart")
                    alterDialog.setPositiveButton("Okay") { _, _ ->
                        super.onBackPressed()
                    }
                    alterDialog.setNegativeButton("Cancel") { _, _ ->

                    }
                    alterDialog.show()
                } else {
                    super.onBackPressed()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun setToolBar() {
        setSupportActionBar(toolbarMenu)
        supportActionBar?.title = restaurantName
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onResume() {

        if (ConnectionManager().checkConnectivity(this)) {
            if (restaurantMenuList.isEmpty()){
                menuProgressLayout.visibility= View.VISIBLE

                try {
                    val queue=Volley.newRequestQueue(this)
                    val url="http://13.235.250.119/v2/restaurants/fetch_result/$restaurantId"

                    val jsonObjectRequest = object : JsonObjectRequest(
                            Method.GET,
                            url,
                            null,
                            Response.Listener {
                                val response = it.getJSONObject("data")
                                val success = response.getBoolean("success")

                                if (success) {
                                    restaurantMenuList.clear()
                                    val data = response.getJSONArray("data")

                                    for (i in 0 until data.length()) {
                                        val restaurant = data.getJSONObject(i)
                                        val menuObject = RestaurantMenu(
                                                restaurant.getString("id"),
                                                restaurant.getString("name"),
                                                restaurant.getString("cost_for_one")
                                        )

                                        restaurantMenuList.add(menuObject)
                                        menuAdapter = MenuAdapter(
                                                this,
                                                restaurantId,
                                                restaurantName,
                                                btnProceedToCart,
                                                restaurantMenuList
                                        )

                                        recyclerViewMenu.adapter = menuAdapter
                                        recyclerViewMenu.layoutManager = layoutManager
                                    }
                                }
                                menuProgressLayout.visibility = View.INVISIBLE
                            },
                            Response.ErrorListener {
                                println("Error12menu is $it")

                                Toast.makeText(
                                        this,
                                        "Some Error occurred!!!",
                                        Toast.LENGTH_SHORT
                                ).show()
                                menuProgressLayout.visibility = View.INVISIBLE
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
                    Toast.makeText(
                            this,
                            "Some Unexpected error occurred!!!",
                            Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {

            val alterDialog = androidx.appcompat.app.AlertDialog.Builder(this)
            alterDialog.setTitle("No Internet")
            alterDialog.setMessage("Check Internet Connection!")
            alterDialog.setPositiveButton("Open Settings") { _, _ ->
                val settingsIntent = Intent(Settings.ACTION_SETTINGS)
                startActivity(settingsIntent)
            }
            alterDialog.setNegativeButton("Exit") { _, _ ->
                finishAffinity()
            }
            alterDialog.setCancelable(false)
            alterDialog.create()
            alterDialog.show()
        }
        super.onResume()
    }
}