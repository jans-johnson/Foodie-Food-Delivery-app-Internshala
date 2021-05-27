package com.jns.foodie.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.jns.foodie.R

class RestaurantMenuActivity : AppCompatActivity() {

    lateinit var toolbarMenu: Toolbar
    lateinit var recyclerViewMenu: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var ivFavIcon:ImageView
    lateinit var tvRating:TextView

    lateinit var restaurantId: String
    lateinit var restaurantName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_menu)

        ivFavIcon=findViewById(R.id.ivFavIcon)
        tvRating=findViewById(R.id.tvRating)
        toolbarMenu=findViewById(R.id.toolbarMenu)

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> {

                super.onBackPressed()

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
}