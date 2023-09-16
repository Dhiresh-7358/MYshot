package com.example.myshot.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myshot.dataClass.CategoryData
import com.example.myshot.R

class CategoryAdapter(private val listener:(CategoryData)->Unit) : RecyclerView.Adapter<CategoryAdapter.MyViewHolder>() {

    var epList= mutableListOf<CategoryData>()
        set(value){
        field=value
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.category_rec, parent, false)
        return MyViewHolder(itemView)
    }
    override fun getItemCount(): Int {
        return epList.size
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        with(holder){
            img.setImageResource(epList[position].CategoryImg)
            nam.text = epList[position].CategoryName
        }

    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img: ImageView = itemView.findViewById(R.id.categoryImage)
        val nam: TextView = itemView.findViewById(R.id.categoryName)
    }
}