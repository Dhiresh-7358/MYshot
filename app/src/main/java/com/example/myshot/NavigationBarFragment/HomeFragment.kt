package com.example.myshot.NavigationBarFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myshot.DataClass.CategoryData
import com.example.myshot.R
import com.example.myshot.adapter.CategoryAdapter
import com.example.myshot.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binging: FragmentHomeBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var categoryList: MutableList<CategoryData> = mutableListOf()
        categoryList.add(CategoryData(R.drawable.wedding_rings,"Wedding"))
        categoryList.add(CategoryData(R.drawable.traveler,"Travel"))
        categoryList.add(CategoryData(R.drawable.residential,"Commercial"))
        categoryList.add(CategoryData(R.drawable.food,"Food"))
        categoryList.add(CategoryData(R.drawable.wedding_rings,"College"))

        val categoryRecycler: RecyclerView=binging.categoryRec

        categoryRecycler.apply {
            layoutManager=LinearLayoutManager(activity,RecyclerView.HORIZONTAL,false)
            adapter=CategoryAdapter(){

            }
        }
        (categoryRecycler.adapter as CategoryAdapter).epList=categoryList


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binging= FragmentHomeBinding.inflate(inflater,container,false)
        return binging.root
    }



}