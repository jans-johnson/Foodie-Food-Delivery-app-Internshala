package com.jns.foodie.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jns.foodie.R
import com.jns.foodie.model.CartItems

class CartActivity : AppCompatActivity() {

    lateinit var toolbarCart:Toolbar
    lateinit var tvRestaurantName: TextView
    lateinit var cartProgressLayout: RelativeLayout
    lateinit var recyclerViewCart: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var btnPlaceOrder: Button


    lateinit var selectedItemsId: ArrayList<String>
    lateinit var restaurantId:String
    lateinit var restaurantName:String

    var totalAmount = 0
    var cartListItems = arrayListOf<CartItems>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        toolbarCart=findViewById(R.id.toolbarCart)
        tvRestaurantName=findViewById(R.id.tvRestaurantName)
        cartProgressLayout=findViewById(R.id.cartProgressLayout)
        recyclerViewCart=findViewById(R.id.recyclerViewCart)
        btnPlaceOrder=findViewById(R.id.btnPlaceOrder)

        restaurantId = intent.getStringExtra("restaurantId")!!
        restaurantName = intent.getStringExtra("restaurantName")!!
        selectedItemsId = intent.getStringArrayListExtra("selectedItemsId")!!
        tvRestaurantName.text = restaurantName

        setToolBar()

        layoutManager = LinearLayoutManager(this)
        recyclerViewCart = findViewById(R.id.recyclerViewCart)
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