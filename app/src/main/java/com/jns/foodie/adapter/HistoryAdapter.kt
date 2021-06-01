package com.jns.foodie.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jns.foodie.R
import com.jns.foodie.model.CartItems
import com.jns.foodie.model.OrderHistory

class HistoryAdapter(val context: Context,val orderList:ArrayList<OrderHistory>) : RecyclerView.Adapter<HistoryAdapter.ViewHolderHistory>() {

    class ViewHolderHistory(view: View) : RecyclerView.ViewHolder(view) {
        val tvRestaurantName: TextView = view.findViewById(R.id.tvRestaurantNameOrderHistory)
        val tvOrderDate: TextView = view.findViewById(R.id.tvOrderDate)
        val tvTotalCost: TextView=view.findViewById(R.id.tvTotalCost)
        val recyclerViewItemsHistory: RecyclerView = view.findViewById(R.id.RecyclerViewItemsHistory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderHistory {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.order_history_single_row, parent, false)

        return ViewHolderHistory(view)
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    override fun onBindViewHolder(holder: ViewHolderHistory, position: Int) {

        holder.tvRestaurantName.text = orderList.get(position).restaurant_name
        holder.tvOrderDate.text = orderList.get(position).order_placed_at.split(" ")[0]
        holder.tvTotalCost.text="Rs ${orderList.get(position).total_cost}"

        val itemJsonList = orderList.get(position).food_items
        val itemList = ArrayList<CartItems>()

        for (i in 0 until itemJsonList.length()) {
            val foodJsonObject = itemJsonList.getJSONObject(i)
            val orderObject = CartItems(
                    foodJsonObject.getString("food_item_id"),
                    foodJsonObject.getString("name"),
                    foodJsonObject.getString("cost"),
                    orderList.get(position).order_id
            )
            itemList.add(orderObject)
        }
        val cartAdapter=CartAdapter(itemList)
        holder.recyclerViewItemsHistory.adapter=cartAdapter
        holder.recyclerViewItemsHistory.layoutManager= LinearLayoutManager(context)
    }
}