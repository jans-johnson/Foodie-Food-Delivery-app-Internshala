package com.jns.foodie.activity

import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jns.foodie.R
import com.jns.foodie.adapter.CartAdapter
import com.jns.foodie.model.CartItems
import com.jns.foodie.model.PlaceOrder
import com.jns.foodie.utils.ConnectionManager
import com.jns.foodie.utils.noInternetDialogBox
import com.jns.foodie.utils.responseErrorToast
import org.json.JSONException
import org.json.JSONObject


class CartActivity : AppCompatActivity() {

    private lateinit var toolbarCart: Toolbar
    private lateinit var tvRestaurantName: TextView
    lateinit var tvTotal: TextView
    lateinit var cartProgressLayout: RelativeLayout
    lateinit var recyclerViewCart: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var btnPlaceOrder: Button


    lateinit var placeOrder: PlaceOrder
    lateinit var restaurantName: String

    var cartListItems = arrayListOf<CartItems>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        toolbarCart = findViewById(R.id.toolbarCart)
        tvRestaurantName = findViewById(R.id.tvRestaurantName)
        cartProgressLayout = findViewById(R.id.cartProgressLayout)
        recyclerViewCart = findViewById(R.id.recyclerViewCart)
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder)
        recyclerViewCart = findViewById(R.id.recyclerViewCart)
        tvTotal = findViewById(R.id.tvTotal)

        btnPlaceOrder.setOnClickListener {

            if (ConnectionManager().checkConnectivity(this)) {
                cartProgressLayout.visibility = View.VISIBLE

                try {
                    val queue = Volley.newRequestQueue(this)
                    val url = "http://13.235.250.119/v2/place_order/fetch_result/"
                    val orderDetails = JSONObject(intent.getStringExtra("details")!!)
                    Log.d("please", orderDetails.toString())

                    val jsonObjectRequest = object : JsonObjectRequest(
                        Method.POST, url, orderDetails,
                        Response.Listener {
                            cartProgressLayout.visibility = View.GONE
                            val response = it.getJSONObject("data")
                            if (response.getBoolean("success")) {
                                val dialogView = LayoutInflater.from(this).inflate(
                                    R.layout.order_placed_dialog,
                                    null
                                )
                                val builder = AlertDialog.Builder(this).setView(dialogView).show()
                                builder.setCanceledOnTouchOutside(false)
                                createNotification()

                                //to redirect to mainActivity
                                Handler().postDelayed({
                                    val intent = Intent(this@CartActivity, MainActivity::class.java)
                                    intent.flags =
                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    startActivity(intent)
                                    builder.dismiss()
                                }, 2000)
                            }

                        },
                        Response.ErrorListener {
                            cartProgressLayout.visibility = View.GONE
                            responseErrorToast(this, it.toString())
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
                    Toast.makeText(this, "Some Error Occurred", Toast.LENGTH_SHORT).show()
                }
            } else {
                cartProgressLayout.visibility = View.GONE

                val alterDialog = noInternetDialogBox(this)
                alterDialog.show()
            }

        }

        placeOrder = Gson().fromJson(intent.getStringExtra("details")!!, PlaceOrder::class.java)
        restaurantName = intent.getStringExtra("restaurantName")!!

        val myType = object : TypeToken<List<CartItems>>() {}.type
        cartListItems = Gson().fromJson(intent.getStringExtra("cartItems"), myType)

        tvRestaurantName.text = restaurantName
        "Total Rs. ${placeOrder.totalCost}".also { tvTotal.text = it }

        setToolBar()

        val cartAdapter = CartAdapter(cartListItems)
        layoutManager = LinearLayoutManager(this)

        recyclerViewCart.layoutManager = layoutManager
        recyclerViewCart.adapter = cartAdapter
    }

    private fun setToolBar() {
        setSupportActionBar(toolbarCart)
        supportActionBar?.title = "My Cart"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> {
                super.onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun createNotification() {
        val notificationId = 1
        val channelId = "personal_notification"
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
        notificationBuilder.setSmallIcon(R.mipmap.ic_logo)
        notificationBuilder.setContentTitle("Order Placed")
        notificationBuilder.setContentText("Your order has been successfully placed!")
        notificationBuilder.setStyle(
            NotificationCompat.BigTextStyle()
                .bigText("Ordered from ${restaurantName}. Please pay Rs.${placeOrder.totalCost}")
        )

        notificationBuilder.priority = NotificationCompat.PRIORITY_DEFAULT
        val notificationManagerCompat = NotificationManagerCompat.from(this)
        notificationManagerCompat.notify(notificationId, notificationBuilder.build())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Order Placed"
            val description = "Your order has been successfully placed!"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val notificationChannel = NotificationChannel(channelId, name, importance)
            notificationChannel.description = description

            val notificationManager =
                (getSystemService(Context.NOTIFICATION_SERVICE)) as NotificationManager

            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}