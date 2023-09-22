package com.example.myshot.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myshot.R
import com.example.myshot.adapter.AllPhotographer
import com.example.myshot.dataClass.TopPhotoData
import com.example.myshot.databinding.FragmentPhotographerBinding
import com.google.firebase.firestore.FirebaseFirestore

@Suppress("DEPRECATION")
class PhotographersFragment : Fragment() {

    private lateinit var binging: FragmentPhotographerBinding
    private lateinit var photographerList: MutableList<TopPhotoData>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binging = FragmentPhotographerBinding.inflate(inflater, container, false)
        return binging.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments?.getString("key")

        photographerList = mutableListOf()

        if (id != null) {
            fetPhotographers(id)
        }

        binging.backButton.setOnClickListener {
            requireFragmentManager().popBackStack()
        }

    }

    private fun fetPhotographers(id: String) {

        binging.selectPhotographer.text=id

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
        val photoRecycler: RecyclerView = binging.photographRecycle

        photoRecycler.apply {
            layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            adapter = AllPhotographer {
                    val bundle=Bundle()
                bundle.putString("key",it.PhotographerName)
                findNavController().navigate(R.id.action_photographers_to_details)
            }
        }
        (photoRecycler.adapter as AllPhotographer).epList = photographerList
    }

}