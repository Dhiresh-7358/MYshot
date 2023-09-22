package com.example.myshot.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myshot.R
import com.example.myshot.adapter.AllPhotographer
import com.example.myshot.adapter.TopPhotoAdapter
import com.example.myshot.dataClass.TopPhotoData
import com.example.myshot.databinding.FragmentPhotographerBinding
import com.google.firebase.firestore.FirebaseFirestore

class PhotographerFragment : Fragment() {

    private lateinit var binging: FragmentPhotographerBinding
    private lateinit var photographerList: MutableList<TopPhotoData>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binging = FragmentPhotographerBinding.inflate(inflater, container, false)
        return binging.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        photographerList = mutableListOf()

        fetPhotographers()

//        if (photographerList.isEmpty()) {
//            fetPhotographers()
//        }
//        else{
//            setAdapter()
//        }

        binging.backButton.setOnClickListener {
            requireFragmentManager().popBackStack()
        }

    }

    private fun fetPhotographers() {

        val db = FirebaseFirestore.getInstance()

        db.collection("photographerInfo")
            .get()
            .addOnSuccessListener {

                for (i in it) {
                    val data = i.data
                    photographerList.add(
                        TopPhotoData(
                            data["image"].toString(), data["name"].toString(),
                            data["rating"].toString(), data["price"].toString()
                        )
                    )
                }
                setAdapter()
            }



    }

    private fun setAdapter() {
        val PhotoRecycler: RecyclerView = binging.photographRecycle

        PhotoRecycler.apply {
            layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            adapter = AllPhotographer() {
            }
        }
        (PhotoRecycler.adapter as AllPhotographer).epList = photographerList
    }

}