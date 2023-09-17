package com.example.myshot.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myshot.R
import com.example.myshot.adapter.AllPhotographer
import com.example.myshot.dataClass.TopPhotoData
import com.example.myshot.databinding.FragmentPhotographerBinding

class PhotographerFragment : Fragment() {

    private lateinit var binging: FragmentPhotographerBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val photographerList: MutableList<TopPhotoData> = mutableListOf()
        photographerList.add(TopPhotoData(R.drawable.anna_unsplash,"The Square","5.0","80,000"))
        photographerList.add(TopPhotoData(R.drawable.alvaro_cvg_mw8izdx7n8e_unsplash,"Mac Studio","4.8","60,000"))
        photographerList.add(TopPhotoData(R.drawable.anna_unsplash,"Yogi Creations","4.9","1,20,000"))
        photographerList.add(TopPhotoData(R.drawable.anna_unsplash,"The Square","5.0","80,000"))
        photographerList.add(TopPhotoData(R.drawable.alvaro_cvg_mw8izdx7n8e_unsplash,"Mac Studio","4.8","60,000"))
        photographerList.add(TopPhotoData(R.drawable.anna_unsplash,"Yogi Creations","4.9","1,20,000"))

        val photoRecycler: RecyclerView =binging.photographRecycle

        photoRecycler.apply {
            layoutManager= LinearLayoutManager(activity, RecyclerView.VERTICAL,false)
            adapter= AllPhotographer(){

            }
         //   isNestedScrollingEnabled = false
        }
        (photoRecycler.adapter as AllPhotographer).epList=photographerList

        binging.backButton.setOnClickListener {
            requireFragmentManager().popBackStack()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binging= FragmentPhotographerBinding.inflate(inflater,container,false)
        return binging.root
    }


}