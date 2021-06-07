package com.jns.foodie.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jns.foodie.R
import com.jns.foodie.model.OnboardingItems

class OnboardingItemsAdapter (private val onboardingItems: List<OnboardingItems>):
    RecyclerView.Adapter<OnboardingItemsAdapter.OnboardingItemViewHolder>() {

    class OnboardingItemViewHolder(view:View) : RecyclerView.ViewHolder(view)
    {
        val ivOnBoarding=view.findViewById<ImageView>(R.id.ivOnBoarding)
        val tvOnBoardingTitle=view.findViewById<TextView>(R.id.tvOnBoardingTitle)
        val tvOnBoardingDescription=view.findViewById<TextView>(R.id.tvOnBoardingDescription)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingItemViewHolder {
        return OnboardingItemViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.onboarding_item_container,
            parent,
            false
        )
        )
    }

    override fun onBindViewHolder(holder: OnboardingItemViewHolder, position: Int) {
        holder.ivOnBoarding.setImageResource(onboardingItems[position].onboardingImage)
        holder.tvOnBoardingTitle.text=onboardingItems[position].title
        holder.tvOnBoardingDescription.text=onboardingItems[position].description
    }

    override fun getItemCount(): Int {
        return onboardingItems.size
    }
}