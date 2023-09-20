package com.example.myshot.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myshot.R
import com.example.myshot.dataClass.TopPhotoData
import com.squareup.picasso.Picasso

class TopPhotoAdapter(private val listener:(TopPhotoData)->Unit) : RecyclerView.Adapter<TopPhotoAdapter.MyViewHolder>() {

    var epList= mutableListOf<TopPhotoData>()
        set(value){
            field=value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.r_top_photographer, parent, false)
        return MyViewHolder(itemView)
    }
    override fun getItemCount(): Int {
        return epList.size
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        with(holder){
            with(epList[position]){
               // Picasso.get().load(this).into(pimg)
           // pimg.setImageResource(epList[position].PhotographerImg)
                Log.d("fire","name2: $PhotographerName")
            pnam.text = this.PhotographerName
            prat.text = this.PhotographerRating
            pprice.text = this.PhotographerPrice
            }
//
        }

    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pimg: ImageView = itemView.findViewById(R.id.photographer_img)
        val pnam: TextView = itemView.findViewById(R.id.photographer_name)
        val prat: TextView = itemView.findViewById(R.id.photographer_rating)
        val pprice: TextView = itemView.findViewById(R.id.photographer_price)
    }
}