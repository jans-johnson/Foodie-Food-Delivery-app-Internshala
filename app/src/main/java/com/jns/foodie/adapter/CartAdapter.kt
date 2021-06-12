package com.jns.foodie.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jns.foodie.R
import com.jns.foodie.model.CartItems

class CartAdapter(val cartItems:ArrayList<CartItems>) : RecyclerView.Adapter<CartAdapter.ViewHolderCart>(){

    class ViewHolderCart(view: View) : RecyclerView.ViewHolder(view) {
        val tvFoodItem : TextView=view.findViewById(R.id.tvFoodItem)
        val tvPrice:TextView=view.findViewById(R.id.tvPriceCart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderCart {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.cart_single_item, parent, false)
        return ViewHolderCart(view)
    }

    override fun onBindViewHolder(holder: ViewHolderCart, position: Int) {
        holder.tvFoodItem.text= cartItems[position].itemName
        holder.tvPrice.text= cartItems[position].itemPrice
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }
}