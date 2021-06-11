package com.jns.foodie.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.jns.foodie.R


class FaqFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val view= inflater.inflate(R.layout.fragment_faq, container, false)

        val tvFAQ1 :TextView=view.findViewById(R.id.tvFAQ1)
        tvFAQ1.tag="closed"
        val tvFAQA1: TextView=view.findViewById(R.id.tvFAQA1)
        val tvFAQ2 :TextView=view.findViewById(R.id.tvFAQ2)
        tvFAQ2.tag="closed"
        val tvFAQA2: TextView=view.findViewById(R.id.tvFAQA2)
        val tvFAQ3 :TextView=view.findViewById(R.id.tvFAQ3)
        tvFAQ3.tag="closed"
        val tvFAQA3: TextView=view.findViewById(R.id.tvFAQA3)
        val tvFAQ4 :TextView=view.findViewById(R.id.tvFAQ4)
        tvFAQ4.tag="closed"
        val tvFAQA4: TextView=view.findViewById(R.id.tvFAQA4)
        val tvFAQ5 :TextView=view.findViewById(R.id.tvFAQ5)
        tvFAQ5.tag="closed"
        val tvFAQA5: TextView=view.findViewById(R.id.tvFAQA5)
        val tvFAQ6 :TextView=view.findViewById(R.id.tvFAQ6)
        tvFAQ6.tag="closed"
        val tvFAQA6: TextView=view.findViewById(R.id.tvFAQA6)
        val tvFAQ7 :TextView=view.findViewById(R.id.tvFAQ7)
        tvFAQ7.tag="closed"
        val tvFAQA7: TextView=view.findViewById(R.id.tvFAQA7)
        val tvFAQ8 :TextView=view.findViewById(R.id.tvFAQ8)
        tvFAQ8.tag="closed"
        val tvFAQA8: TextView=view.findViewById(R.id.tvFAQA8)

        tvFAQ1.setOnClickListener {
            if (tvFAQ1.tag.equals("closed"))
            {
                tvFAQ1.tag="open"
                tvFAQA1.visibility=View.VISIBLE
                tvFAQ1.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_arrow_up,0);
            }
            else
            {
                tvFAQ1.tag="closed"
                tvFAQA1.visibility=View.GONE
                tvFAQ1.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_arrow_down, 0);
            }
        }

        tvFAQ2.setOnClickListener {
            if (tvFAQ2.tag.equals("closed"))
            {
                tvFAQ2.tag="open"
                tvFAQA2.visibility=View.VISIBLE
                tvFAQ2.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_arrow_up,0);
            }
            else
            {
                tvFAQ2.tag="closed"
                tvFAQA2.visibility=View.GONE
                tvFAQ2.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_arrow_down, 0);
            }
        }

        tvFAQ3.setOnClickListener {
            if (tvFAQ3.tag.equals("closed"))
            {
                tvFAQ3.tag="open"
                tvFAQA3.visibility=View.VISIBLE
                tvFAQ3.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_arrow_up,0);
            }
            else
            {
                tvFAQ3.tag="closed"
                tvFAQA3.visibility=View.GONE
                tvFAQ3.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_arrow_down, 0);
            }
        }

        tvFAQ4.setOnClickListener {
            if (tvFAQ4.tag.equals("closed"))
            {
                tvFAQ4.tag="open"
                tvFAQA4.visibility=View.VISIBLE
                tvFAQ4.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_arrow_up,0);
            }
            else
            {
                tvFAQ4.tag="closed"
                tvFAQA4.visibility=View.GONE
                tvFAQ4.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_arrow_down, 0);
            }
        }

        tvFAQ5.setOnClickListener {
            if (tvFAQ5.tag.equals("closed"))
            {
                tvFAQ5.tag="open"
                tvFAQA5.visibility=View.VISIBLE
                tvFAQ5.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_arrow_up,0);
            }
            else
            {
                tvFAQ5.tag="closed"
                tvFAQA5.visibility=View.GONE
                tvFAQ5.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_arrow_down, 0);
            }
        }

        tvFAQ6.setOnClickListener {
            if (tvFAQ6.tag.equals("closed"))
            {
                tvFAQ6.tag="open"
                tvFAQA6.visibility=View.VISIBLE
                tvFAQ6.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_arrow_up,0);
            }
            else
            {
                tvFAQ6.tag="closed"
                tvFAQA6.visibility=View.GONE
                tvFAQ6.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_arrow_down, 0);
            }
        }

        tvFAQ7.setOnClickListener {
            if (tvFAQ7.tag.equals("closed"))
            {
                tvFAQ7.tag="open"
                tvFAQA7.visibility=View.VISIBLE
                tvFAQ7.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_arrow_up,0);
            }
            else
            {
                tvFAQ7.tag="closed"
                tvFAQA7.visibility=View.GONE
                tvFAQ7.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_arrow_down, 0);
            }
        }

        tvFAQ8.setOnClickListener {
            if (tvFAQ8.tag.equals("closed"))
            {
                tvFAQ8.tag="open"
                tvFAQA8.visibility=View.VISIBLE
                tvFAQ8.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_arrow_up,0);
            }
            else
            {
                tvFAQ8.tag="closed"
                tvFAQA8.visibility=View.GONE
                tvFAQ8.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_arrow_down, 0);
            }
        }


        return view
    }

}