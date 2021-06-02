package com.jns.foodie.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jns.foodie.R
import com.jns.foodie.adapter.CartAdapter
import com.jns.foodie.model.CartItems
import com.jns.foodie.model.PlaceOrder

class CartActivity : AppCompatActivity() {

    lateinit var toolbarCart:Toolbar
    lateinit var tvRestaurantName: TextView
    lateinit var cartProgressLayout: RelativeLayout
    lateinit var recyclerViewCart: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var btnPlaceOrder: Button


    lateinit var placeOrder: PlaceOrder
    lateinit var restaurantName:String

    var cartListItems = arrayListOf<CartItems>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        toolbarCart=findViewById(R.id.toolbarCart)
        tvRestaurantName=findViewById(R.id.tvRestaurantName)
        cartProgressLayout=findViewById(R.id.cartProgressLayout)
        recyclerViewCart=findViewById(R.id.recyclerViewCart)
        btnPlaceOrder=findViewById(R.id.btnPlaceOrder)
        recyclerViewCart = findViewById(R.id.recyclerViewCart)

        btnPlaceOrder.setOnClickListener {

            Toast.makeText(this,"yes Working",Toast.LENGTH_SHORT).show()

        }

        placeOrder = Gson().fromJson(intent.getStringExtra("details")!!,PlaceOrder::class.java)
        restaurantName= intent.getStringExtra("restaurantName")!!

        val myType = object : TypeToken<List<CartItems>>() {}.type
        cartListItems=Gson().fromJson(intent.getStringExtra("cartItems"),myType)

        tvRestaurantName.text=restaurantName

        setToolBar()

        val cartAdapter=CartAdapter(cartListItems)
        layoutManager = LinearLayoutManager(this)

        recyclerViewCart.layoutManager=layoutManager
        recyclerViewCart.adapter=cartAdapter
    }

    fun setToolBar() {
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
}