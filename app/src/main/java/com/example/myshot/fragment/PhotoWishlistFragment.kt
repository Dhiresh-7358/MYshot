package com.example.myshot.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myshot.R
import com.example.myshot.adapter.AllPhotographer
import com.example.myshot.dataClass.TopPhotoData
import com.example.myshot.databinding.FragmentPhotoWishlistBinding
import com.example.myshot.decorationClass.MarginClassVerticalLinearLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PhotoWishlistFragment : Fragment() {

    private lateinit var binding:FragmentPhotoWishlistBinding

    private lateinit var photographerList: MutableList<TopPhotoData>
    private val mAuth = FirebaseAuth.getInstance()
    private val userID = mAuth.currentUser?.uid
    private lateinit var isLikedData: ArrayList<String>
    private val db = FirebaseFirestore.getInstance()
    private val documentRef = userID?.let {
        db.collection("users")
            .document(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPhotoWishlistBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

        fetchIsLikedName(object : FetchIsLikedNameCallback {
            override fun onFetchComplete() {

                for (name in isLikedData) {
                    fetPhotographers(name)
                }
            }
        })

    }

    private fun init() {
        isLikedData = ArrayList()
        photographerList = mutableListOf()
    }

    private fun fetPhotographers(name: String) {

        db.collection("photographerInfo")
            .whereEqualTo("name", name)
            .get()
            .addOnSuccessListener {
                for (i in it) {
                    val data = i.data
                    photographerList.add(
                        TopPhotoData(
                            data["image"].toString(), data["name"].toString(),
                            data["rating"].toString(), data["price"].toString(),
                            true
                        )
                    )
                    if (isLikedData.size == photographerList.size) {
                        setAdapter()
                    }
                }

            }
    }


    private fun setAdapter() {

        binding.wishlistRecycler.apply {
            layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            adapter = AllPhotographer(
                { photoItemsModel -> adapterClick(photoItemsModel) },
                requireContext()
            )
        }
        (binding.wishlistRecycler.adapter as AllPhotographer).epList = photographerList

        val margin = MarginClassVerticalLinearLayout(40,40,40,40)
        binding.wishlistRecycler.addItemDecoration(margin)
    }

    private fun adapterClick(photoItemsModel: TopPhotoData) {
        val bundle = Bundle()
        bundle.putString("key", photoItemsModel.PhotographerName)
        findNavController().navigate(R.id.action_wishlistFragment_to_details, bundle)
    }

    private fun fetchIsLikedName(callback: FetchIsLikedNameCallback) {
        documentRef?.get()
            ?.addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val data = documentSnapshot.data?.get("isLikedArray") as? ArrayList<String>
                    if (data != null) {
                        isLikedData = data

                    }
                    callback.onFetchComplete()
                }
            }
            ?.addOnFailureListener { exception ->
                Toast.makeText(context, "$exception", Toast.LENGTH_SHORT).show()
            }
    }


    companion object {

        fun newInstance() = PhotoWishlistFragment()
    }
}