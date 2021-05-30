package com.jns.foodie.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.amulyakhare.textdrawable.TextDrawable
import com.google.android.material.navigation.NavigationView
import com.jns.foodie.R
import com.jns.foodie.adapter.HomeAdapter
import com.jns.foodie.fragment.*

class MainActivity : AppCompatActivity() {

    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var frameLayout: FrameLayout
    lateinit var navigationView: NavigationView
    lateinit var drawerLayout: DrawerLayout
    lateinit var tvDashboardUser: TextView
    lateinit var tvDashboardMobile: TextView
    lateinit var ivDashboard: ImageView
    lateinit var sharedPreferences: SharedPreferences

    var previousMenuItemSelected: MenuItem? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        coordinatorLayout = findViewById(R.id.coordinatorLayout)
        toolbar = findViewById(R.id.toolBar)
        frameLayout = findViewById(R.id.frameLayout)
        navigationView = findViewById(R.id.navigationView)
        drawerLayout = findViewById(R.id.drawerLayout)

        val headerView=navigationView.getHeaderView(0)
        tvDashboardUser=headerView.findViewById(R.id.tvDashboardUser)
        tvDashboardMobile=headerView.findViewById(R.id.tvDashboardMobile)
        ivDashboard=headerView.findViewById(R.id.ivDashboard)


        sharedPreferences=getSharedPreferences("UserDetails",Context.MODE_PRIVATE)

        setToolBar()

        tvDashboardUser.text=sharedPreferences.getString("name","UserName")
        tvDashboardMobile.text=sharedPreferences.getString("mobile_number","9999999999")

        val textDrawable=TextDrawable.builder().buildRound(tvDashboardUser.text[0].toString().toUpperCase(),R.color.PrimaryDark1)
        ivDashboard.setImageDrawable(textDrawable)


        //for the hamburger icon
        val actionBarDrawerToggle = ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.string.open_drawer,
                R.string.close_drawer
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        navigationView.setNavigationItemSelectedListener {
            if (previousMenuItemSelected!=null){
                previousMenuItemSelected?.isChecked=false
            }

            previousMenuItemSelected = it
            it.isCheckable = true
            it.isChecked = true


            when (it.itemId) {
                R.id.itemHome -> {
                    openDashboard()
                    drawerLayout.closeDrawers()
                }
                R.id.itemProfile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frameLayout,
                            ProfileFragment()
                        ).commit()

                    supportActionBar?.title = "My Profile"
                    drawerLayout.closeDrawers()
                }
                R.id.itemFavourite -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frameLayout,
                            FavouriteFragment()
                        ).commit()

                    supportActionBar?.title = "Favourites"
                    drawerLayout.closeDrawers()
                }
                R.id.itemHistory -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frameLayout,
                            HistoryFragment()
                        ).commit()

                    supportActionBar?.title = "Order History"
                    drawerLayout.closeDrawers()
                }
                R.id.itemFAQ -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frameLayout,
                            FaqFragment()
                        ).commit()

                    supportActionBar?.title = "FAQs"
                    drawerLayout.closeDrawers()
                }
                R.id.itemLogout -> {
                    drawerLayout.closeDrawers()

                    val alterDialog = androidx.appcompat.app.AlertDialog.Builder(this)
                    alterDialog.setMessage("Are you sure you want to to log out?")
                    alterDialog.setPositiveButton("Yes") { _, _ ->
                        sharedPreferences.edit().clear().apply()
                        this.deleteDatabase("restaurant-db")
                        val intent=Intent(this,LoginActivity::class.java)
                        startActivity(intent)
                        this.finish()
                    }
                    alterDialog.setNegativeButton("No") { _, _ ->

                    }
                    alterDialog.create()
                    alterDialog.show()
                }
            }

            return@setNavigationItemSelectedListener true
        }
        openDashboard()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        when (supportFragmentManager.findFragmentById(R.id.frameLayout)) {
            !is HomeFragment -> {
                navigationView.menu.getItem(0).isChecked = true
                openDashboard()
            }
            else -> super.onBackPressed()
        }
    }

    fun setToolBar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "All Restaurants"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun openDashboard() {
        supportFragmentManager.beginTransaction().replace(
            R.id.frameLayout,
            HomeFragment()
        ).commit()

        supportActionBar?.title = "All Restaurants"
        navigationView.setCheckedItem(R.id.itemHome)
    }
}