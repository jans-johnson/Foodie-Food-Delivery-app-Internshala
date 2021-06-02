package com.jns.foodie.model

import com.google.gson.annotations.SerializedName

data class PlaceOrder (
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("restaurant_id")
    val restaurantId: String,
    @SerializedName("total_cost")
    val totalCost: String,
    val food: ArrayList<Food>
        )