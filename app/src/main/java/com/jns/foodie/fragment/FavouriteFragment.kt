package com.jns.foodie.fragment

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.jns.foodie.R
import com.jns.foodie.adapter.HomeAdapter
import com.jns.foodie.database.RestaurantDatabase
import com.jns.foodie.database.RestaurantEntity
import java.lang.ref.WeakReference


class FavouriteFragment : Fragment() {

    lateinit var recyclerViewFavourite: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var favoriteProgressLayout: RelativeLayout
    lateinit var noFavouriteLayout: RelativeLayout
    lateinit var recyclerAdapter:HomeAdapter
    var restaurantInfoList=ArrayList<RestaurantEntity>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favourite, container, false)

        layoutManager=LinearLayoutManager(activity)
        recyclerViewFavourite=view.findViewById(R.id.recyclerViewFavourite)
        favoriteProgressLayout=view.findViewById(R.id.favoriteProgressLayout)
        noFavouriteLayout=view.findViewById(R.id.noFavouriteLayout)

        return view
    }

    override fun onResume() {

        restaurantInfoList= RetrieveFavourites(WeakReference(activity as Context)).execute().get() as ArrayList<RestaurantEntity>

        if (restaurantInfoList.isEmpty())
            noFavouriteLayout.visibility=View.VISIBLE
        else
            noFavouriteLayout.visibility=View.GONE

        if(activity!=null){
            recyclerAdapter= HomeAdapter(activity as Context,restaurantInfoList)
            recyclerViewFavourite.adapter=recyclerAdapter
            recyclerViewFavourite.layoutManager=layoutManager
        }

        super.onResume()
    }

    //weakReference to prevent Memory Leak
    private class RetrieveFavourites(val context: WeakReference<Context>): AsyncTask<Void,Void,List<RestaurantEntity>>() {
        override fun doInBackground(vararg p0: Void?): List<RestaurantEntity> {

            val db = Room.databaseBuilder(context.get()!!, RestaurantDatabase::class.java, "restaurant-db").build()

            return db.restaurantDao().getRestaurants()

        }

    }
    }
