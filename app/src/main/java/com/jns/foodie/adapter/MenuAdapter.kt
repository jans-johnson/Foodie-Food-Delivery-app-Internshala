package com.jns.foodie.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.jns.foodie.R
import com.jns.foodie.activity.CartActivity
import com.jns.foodie.model.CartItems
import com.jns.foodie.model.Food
import com.jns.foodie.model.PlaceOrder
import com.jns.foodie.model.RestaurantMenu

class MenuAdapter (val context: Context,
                   val restaurantId: String,
                   val restaurantName: String,
                   val buttonProceedToCart: Button,
                   val restaurantMenu: ArrayList<RestaurantMenu>) : RecyclerView.Adapter<MenuAdapter.ViewHolderMenu>(){

    var itemSelectedCount: Int = 0
    var totalCost:Int=0
    var foodItemsId = arrayListOf<Food>()
    var cartListItems = arrayListOf<CartItems>()
    var userId=context.getSharedPreferences("UserDetails",Context.MODE_PRIVATE).getString("user_id","user")

    class ViewHolderMenu(view: View): RecyclerView.ViewHolder(view)
    {
        val tvSerialNumber: TextView = view.findViewById(R.id.tvSerialNumber)
        val tvItemName: TextView = view.findViewById(R.id.tvItemName)
        val tvItemPrice: TextView = view.findViewById(R.id.tvItemPrice)
        val btnAddToCart: Button = view.findViewById(R.id.btnAddToCart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderMenu {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.restaurant_menu_single_row, parent, false)

        return ViewHolderMenu(view)
    }

    override fun getItemCount(): Int {
        return restaurantMenu.size
    }
    override fun onBindViewHolder(holder: ViewHolderMenu, position: Int) {
        val restaurantMenuItem = restaurantMenu[position]

        buttonProceedToCart.setOnClickListener {
            val placeOrder=PlaceOrder(userId.toString(),restaurantId,totalCost.toString(),foodItemsId)
            val intent = Intent(context, CartActivity::class.java)
            intent.putExtra("details", Gson().toJson(placeOrder))
            intent.putExtra("restaurantName",restaurantName)
            intent.putExtra("cartItems", Gson().toJson(cartListItems))
            context.startActivity(intent)
        }

        holder.btnAddToCart.setOnClickListener {

            if (holder.btnAddToCart.text.toString() == "Remove") {

                itemSelectedCount--
                val item=Food(restaurantMenuItem.id)
                totalCost-=restaurantMenuItem.cost_for_one.toInt()
                foodItemsId.remove(item)

                val cartItem=CartItems(restaurantMenuItem.name,restaurantMenuItem.cost_for_one)
                cartListItems.remove(cartItem)

                holder.btnAddToCart.text = "Add"
                holder.btnAddToCart.setBackgroundColor(Color.rgb(31, 171, 137))

            } else {

                itemSelectedCount++
                val item=Food(restaurantMenuItem.id)
                totalCost+=restaurantMenuItem.cost_for_one.toInt()
                foodItemsId.add(item)

                val cartItem=CartItems(restaurantMenuItem.name,restaurantMenuItem.cost_for_one)
                cartListItems.add(cartItem)

                holder.btnAddToCart.text = "Remove"
                holder.btnAddToCart.setBackgroundColor( Color.rgb(255, 196, 0))
            }

            if (itemSelectedCount > 0) {
                buttonProceedToCart.visibility = View.VISIBLE
            } else {
                buttonProceedToCart.visibility = View.GONE
            }
        }

        holder.tvSerialNumber.text = (position + 1).toString()
        holder.tvItemName.text = restaurantMenuItem.name
        holder.tvItemPrice.text = "Rs. ${restaurantMenuItem.cost_for_one}"
    }

    fun getSelectedItemCount(): Int {
        return itemSelectedCount
    }

}