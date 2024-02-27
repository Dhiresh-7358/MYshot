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
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

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

                Picasso.get()
                    .load(this.PhotographerImg)
                    .placeholder(R.drawable.logo) // Replace with your default image resource
                    .error(R.drawable.logo) // Replace with your error image resource
                    .into(pImg)

                //Picasso.get().load(this.PhotographerImg).into(pImg)
                val capitalizedSentence = capitalizeWords(this.PhotographerName)
                pName.text = capitalizedSentence

                pRating.text = this.PhotographerRating.toFloat().toString()

                val formattedAmount = formatRupeesWithCommas(this.PhotographerPrice.toLong())
                pPrice.text = formattedAmount
            }
        }
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val pImg: ImageView = itemView.findViewById(R.id.photographer_img)
        val pName: TextView = itemView.findViewById(R.id.photographer_name)
        val pRating: TextView = itemView.findViewById(R.id.photographer_rating)
        val pPrice: TextView = itemView.findViewById(R.id.photographer_price)

        init {
            itemView.setOnClickListener {
                listener.invoke(epList[adapterPosition])
            }
        }
    }

    private fun capitalizeWords(sentence: String): String {
        val words = sentence.split(" ")
        val capitalizedWords = words.map { it.capitalize() }
        return capitalizedWords.joinToString(" ")
    }

    private fun formatRupeesWithCommas(amount: Long): String {
        val formatter = DecimalFormat("#,##,##,###") // Format pattern for Indian numbering system
        return formatter.format(amount)
    }
}