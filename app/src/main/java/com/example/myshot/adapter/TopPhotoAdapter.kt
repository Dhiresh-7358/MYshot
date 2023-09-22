package com.example.myshot.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.myshot.R
import com.example.myshot.dataClass.TopPhotoData
import com.squareup.picasso.Picasso

class TopPhotoAdapter(private val listener: (TopPhotoData) -> Unit) :
    RecyclerView.Adapter<TopPhotoAdapter.MyViewHolder>() {


    var epList = mutableListOf<TopPhotoData>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.r_top_photographer, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return epList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        with(holder) {
            with(epList[position]) {

                Picasso.get().load(this.PhotographerImg).into(pImg)
                pName.text = this.PhotographerName
                pRating.text = this.PhotographerRating.toString()
                pPrice.text = this.PhotographerPrice
            }
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pImg: ImageView = itemView.findViewById(R.id.photographer_img)
        val pName: TextView = itemView.findViewById(R.id.photographer_name)
        val pRating: TextView = itemView.findViewById(R.id.photographer_rating)
        val pPrice: TextView = itemView.findViewById(R.id.photographer_price)
    }
}