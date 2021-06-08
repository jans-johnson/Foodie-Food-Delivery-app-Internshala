package com.jns.foodie.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.jns.foodie.R
import com.jns.foodie.adapter.OnboardingItemsAdapter
import com.jns.foodie.model.OnboardingItems
import java.text.FieldPosition

class OnBoardingActivity : AppCompatActivity() {

    lateinit var onboardingItemsAdapter: OnboardingItemsAdapter
    lateinit var llIndicators: LinearLayout
    lateinit var ivNext: ImageView
    lateinit var btnGetStarted: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding)
        llIndicators=findViewById(R.id.llIndicators)
        ivNext=findViewById(R.id.ivNext)
        btnGetStarted=findViewById(R.id.btnGetStarted)
        val onBoardingViewPager=findViewById<ViewPager2>(R.id.onBoardingViewPager)

        ivNext.setOnClickListener{
            if (onBoardingViewPager.currentItem+1<onboardingItemsAdapter.itemCount)
            {
                onBoardingViewPager.currentItem+=1
            }
            else
                goToLogin()
        }

        btnGetStarted.setOnClickListener {
            goToLogin()
        }

        onboardingItemsAdapter= OnboardingItemsAdapter(
            listOf(
                OnboardingItems(
                    R.drawable.ic_onboarding1,
                    "Choose Your Restaurant",
                    "Select from a list of top Restaurants in your Locality"
                ),
                OnboardingItems(
                    R.drawable.ic_onboarding2,
                    "Make Your Order",
                    "Choose your favourite dishes and Place your order"
                ),
                OnboardingItems(
                    R.drawable.ic_onboarding3,
                    "Sit Back And Relax",
                "Wait a few minutes for our agents to deliver the food at your doorstep"
                )
            )
        )
        onBoardingViewPager.adapter=onboardingItemsAdapter
        onBoardingViewPager.registerOnPageChangeCallback(object :
        ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentPosition(position)
            }
        })

        val indicators= arrayOfNulls<ImageView>(onboardingItemsAdapter.itemCount)
        val layoutParams=LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        layoutParams.setMargins(8,0,8,0)

        for (i in indicators.indices){
            indicators[i]= ImageView(applicationContext)
            indicators[i]?.let {
                it.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator__inactive_onboarding
                    )
                )
                it.layoutParams=layoutParams
                llIndicators.addView(it)
            }
        }
        setCurrentPosition(0)




    }

    fun setCurrentPosition(position: Int){
        val childCount=llIndicators.childCount
        for (i in 0 until childCount){
            val imageView=llIndicators.getChildAt(i) as ImageView
            if (i == position){
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(
                                applicationContext,
                                R.drawable.indicator_active_onboarding
                        )
                )
            }
            else
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(
                                applicationContext,
                                R.drawable.indicator__inactive_onboarding
                        )
                )

        }
    }

    fun goToLogin()
    {
        val sharedPreferences= getSharedPreferences(
                "UserDetails",
                Context.MODE_PRIVATE
        )
        sharedPreferences?.edit()?.putBoolean("isNotFirstTime", true)?.apply()
        val intent=Intent(this,LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}