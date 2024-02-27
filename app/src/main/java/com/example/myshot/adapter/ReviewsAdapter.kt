package com.example.myshot.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myshot.R
import com.example.myshot.dataClass.ReviewsData
import com.example.myshot.dataClass.TopPhotoData

class ReviewsAdapter() :
    RecyclerView.Adapter<ReviewsAdapter.MyViewHolder>() {

    var epList = mutableListOf<ReviewsData>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val reviewsName: TextView = itemView.findViewById(R.id.name)
        val rat: TextView = itemView.findViewById(R.id.rating)
        val reviews0: TextView = itemView.findViewById(R.id.review_highLight)
        val reviews1: TextView = itemView.findViewById(R.id.review_desc1)
        val reviews2: TextView = itemView.findViewById(R.id.review_desc2)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewsAdapter.MyViewHolder {

        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.r_review, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ReviewsAdapter.MyViewHolder, position: Int) {
        with(holder) {
            with(epList[position]) {

                reviewsName.text = this.name
                rat.text=this.rating
                reviews0.text=this.reviewHighlights
                reviews1.text=this.review1
                reviews2.text=this.review2

            }
        }

    }

    override fun getItemCount(): Int {
        return epList.size
    }
}