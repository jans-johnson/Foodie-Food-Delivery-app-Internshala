package com.jns.foodie.adapter

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.jns.foodie.R
import com.jns.foodie.activity.RestaurantMenuActivity
import com.jns.foodie.database.RestaurantDatabase
import com.jns.foodie.database.RestaurantEntity
import com.squareup.picasso.Picasso

class HomeAdapter(val context: Context, var itemList: ArrayList<RestaurantEntity>): RecyclerView.Adapter<HomeAdapter.ViewHolderDashboard>() {

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
        val restaurantEntity=RestaurantEntity(restaurant.restaurantId,restaurant.restaurantName,restaurant.restaurantRating,restaurant.restaurantCost,restaurant.restaurantImage)
        var fav:Boolean=false

        holder.tvCardName.text=restaurant.restaurantName
        holder.tvCardPrice.text=restaurant.restaurantCost+"/person"
        holder.tvCardRating.text=restaurant.restaurantRating

        Picasso.get().load(restaurant.restaurantImage).error(R.drawable.restaurant_default).into(holder.ivCardRestaurant)

        if(DBAsyncTask( context,restaurantEntity,1 ).execute().get()) {
            holder.ivCardFavorite.setImageResource(R.drawable.ic_favourite_fill)
            fav=true
        }
            else {
            holder.ivCardFavorite.setImageResource(R.drawable.ic_favourite_red_border)
            fav=false
        }
        holder.llContent.setOnClickListener {
            val intent = Intent(context, RestaurantMenuActivity::class.java)
            intent.putExtra("restaurantName", holder.tvCardName.text.toString())
            intent.putExtra("restaurantId", restaurant.restaurantId)
            intent.putExtra("restaurantRating",restaurant.restaurantRating)
            intent.putExtra("isFav",fav)
            context.startActivity(intent)
        }


        holder.ivCardFavorite.setOnClickListener{
            if(!DBAsyncTask( context,restaurantEntity,1 ).execute().get()){
                val result=DBAsyncTask(context,restaurantEntity,2).execute().get()

                if (result) {
                    holder.ivCardFavorite.setImageResource(R.drawable.ic_favourite_fill)
                    fav=true
                } else {
                    Toast.makeText(context, "Some error occurred", Toast.LENGTH_SHORT).show()
                }
            }
            //if it was already added to favourites
            else {
                val result = DBAsyncTask(context, restaurantEntity, 3).execute().get()
                if (result) {
                    holder.ivCardFavorite.setImageResource(R.drawable.ic_favourite_red_border)
                    fav=false
                } else {
                    Toast.makeText(context, "Some error occurred", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //for the search option
    fun filterList(filteredList: ArrayList<RestaurantEntity>) {
        itemList = filteredList
        notifyDataSetChanged()
    }


    class DBAsyncTask(val context: Context, val restaurantEntity: RestaurantEntity, val mode: Int) :
            AsyncTask<Void, Void, Boolean>() {

        val db = Room.databaseBuilder(context, RestaurantDatabase::class.java, "restaurant-db").build()

        override fun doInBackground(vararg p0: Void?): Boolean {

            /*
            * Mode 1->check if restaurant is in favourites
            * Mode 2->Save the restaurant into DB as favourites
            * Mode 3-> Remove the favourite restaurant*/
            when (mode) {
                1 -> {
                    val restaurant: RestaurantEntity? = db.restaurantDao()
                            .getRestaurantById(restaurantEntity.restaurantId)
                    db.close()
                    return restaurant != null
                }
                2 -> {
                    db.restaurantDao().insertRestaurant(restaurantEntity)
                    db.close()
                    return true
                }
                3 -> {
                    db.restaurantDao().deleteRestauarnt(restaurantEntity)
                    db.close()
                    return true
                }
                else -> return false

            }
        }
    }


}