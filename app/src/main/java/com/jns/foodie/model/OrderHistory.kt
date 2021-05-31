package com.jns.foodie.model

import org.json.JSONArray

data class OrderHistory (
        var order_id : String ,
        var restaurant_name : String,
        var total_cost : String ,
        var order_placed_at : String ,
        var food_items : JSONArray
        )
