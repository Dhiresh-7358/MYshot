package com.example.myshot.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myshot.dataClass.CategoryData
import com.example.myshot.R
import com.example.myshot.dataClass.IdeasData

class IdeasWishlistAdapter(private val listener: (IdeasData) -> Unit) :
    RecyclerView.Adapter<IdeasWishlistAdapter.MyViewHolder>() {

    var epList = mutableListOf<IdeasData>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.r_ideas_wishlist, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return epList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        with(holder) {
          //  img.setImageResource(epList[position].ideasImage as Int)
            nam.setImageResource(R.drawable.baseline_favorite_border_24)
        }
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img: ImageView = itemView.findViewById(R.id.idea_img)
        val nam: ImageView = itemView.findViewById(R.id.liked_button)


        init {
            itemView.setOnClickListener {
                listener.invoke(epList[adapterPosition])
            }
        }
    }
}