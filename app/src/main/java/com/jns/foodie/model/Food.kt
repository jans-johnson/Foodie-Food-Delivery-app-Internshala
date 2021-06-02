package com.jns.foodie.model

import com.google.gson.annotations.SerializedName

data class Food (
        @SerializedName("food_item_id")
        val id :String
        )