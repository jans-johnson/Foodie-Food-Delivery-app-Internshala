package com.jns.foodie.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jns.foodie.R
import com.jns.foodie.model.Restaurant
import com.squareup.picasso.Picasso

class HomeAdapter(var itemList:ArrayList<Restaurant>): RecyclerView.Adapter<HomeAdapter.ViewHolderDashboard>() {

    class ViewHolderDashboard(view: View) : RecyclerView.ViewHolder(view) {
        val ivCardRestaurant: ImageView = view.findViewById(R.id.ivCardRestaurant)
        val tvCardName: TextView = view.findViewById(R.id.tvCardName)
        val tvCardPrice: TextView = view.findViewById(R.id.tvCardPrice)
        val tvCardRating: TextView = view.findViewById(R.id.tvCardRating)
        val llContent: LinearLayout = view.findViewById(R.id.llContent)
        val ivCardFavorite: ImageView = view.findViewById(R.id.ivCardFavourite)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderDashboard {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.home_recycler_view_single_row,parent,false)

        return ViewHolderDashboard(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolderDashboard, position: Int) {

        val restaurant=itemList[position]

        holder.tvCardName.text=restaurant.restaurantName
        holder.tvCardPrice.text=restaurant.cost_for_one+"/person"
        holder.tvCardRating.text=restaurant.restaurantRating

        Picasso.get().load(restaurant.restaurantImage).error(R.drawable.restaurant_default).into(holder.ivCardRestaurant)
    }

    //for the search option
    fun filterList(filteredList: ArrayList<Restaurant>) {
        itemList = filteredList
        notifyDataSetChanged()
    }


}