package com.jns.foodie.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RestaurantDao {
    @Insert
    fun insertRestaurant(restaurantEntity: RestaurantEntity)

    @Delete
    fun deleteRestauarnt(restaurantEntity: RestaurantEntity)

    @Query("select * from restaurants")
    fun getRestaurants():List<RestaurantEntity>

    @Query("select * from restaurants where restaurant_id = :restaurantId")
    fun  getRestaurantById(restaurantId:String): RestaurantEntity

}