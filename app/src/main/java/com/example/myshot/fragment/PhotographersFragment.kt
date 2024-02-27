package com.example.myshot.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myshot.R
import com.example.myshot.adapter.AllPhotographer
import com.example.myshot.dataClass.TopPhotoData
import com.example.myshot.databinding.FragmentPhotographerBinding
import com.example.myshot.decorationClass.MarginClassVerticalLinearLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Suppress("DEPRECATION")

interface FetchIsLikedNameCallback {
    fun onFetchComplete()
}

class PhotographersFragment : Fragment() {

    private lateinit var binging: FragmentPhotographerBinding
    private lateinit var photographerList: MutableList<TopPhotoData>
    private lateinit var photoRecycler: RecyclerView
    private lateinit var searchList: MutableList<TopPhotoData>
    private val mAuth = FirebaseAuth.getInstance()
    private val userID = mAuth.currentUser?.uid
    private var isFunCalled: Boolean = false
    private lateinit var isLikedData: ArrayList<String>
    private val db = FirebaseFirestore.getInstance()
    private val documentRef = userID?.let {
        db.collection("users")
            .document(it)
    }


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

        inIt()

        fetchIsLikedName(object : FetchIsLikedNameCallback {
            override fun onFetchComplete() {
                if (id != null) {
                    fetPhotographers(id)
                }
            }
        })

        searchPhotographer()

        binging.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun searchPhotographer() {
        binging.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(p0: String?): Boolean {

                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {

                searchList.clear()
                if (p0 != null) {
                    if (p0.isNotEmpty()) {
                        filterList(p0)
                    }

                }
                return true
            }

        })
    }

    private fun filterList(p0: String?) {
        for (i in photographerList) {
            if (p0 != null) {
                if (i.PhotographerName.lowercase().contains(p0.lowercase())) {
                    searchList.add(i)
                }
            }
        }

        photoRecycler.adapter?.notifyDataSetChanged()

        if (searchList.isEmpty()) {
            Toast.makeText(requireContext(), "Photographer is not found!!!", Toast.LENGTH_SHORT)
                .show()
        } else {

        }
    }

    private fun inIt() {
        photoRecycler = binging.photographRecycle
        photographerList = mutableListOf()
        searchList = mutableListOf()
        isLikedData = ArrayList()
    }

    private fun fetPhotographers(id: String) {

        binging.selectPhotographer.text = id

        val db = FirebaseFirestore.getInstance()

        db.collection("photographerInfo")
            .get()
            .addOnSuccessListener {

                for (i in it) {
                    val data = i.data
                    photographerList.add(
                        TopPhotoData(
                            data["image"].toString(), data["name"].toString(),
                            data["rating"].toString(), data["price"].toString(),
                            likedMatch(data["name"].toString())
                        )
                    )
                }
                setAdapter()

            }
    }

    private fun likedMatch(name: String): Boolean {
        return name in isLikedData
    }

    private fun fetchIsLikedName(callback: FetchIsLikedNameCallback) {
        documentRef?.get()
            ?.addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    isFunCalled = true
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


    private fun setAdapter() {

        searchList.addAll(photographerList)

        photoRecycler.apply {
            layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            adapter = AllPhotographer(
                { photoItemsModel -> adapterClick(photoItemsModel) },
                requireContext()
            )
        }
        (photoRecycler.adapter as AllPhotographer).epList = searchList

        val margin = MarginClassVerticalLinearLayout(40,40,40,40)
        photoRecycler.addItemDecoration(margin)
    }

    private fun adapterClick(photoItemsModel: TopPhotoData) {
        val bundle = Bundle()
        bundle.putString("key", photoItemsModel.PhotographerName)
        findNavController().navigate(R.id.action_photographers_to_details, bundle)
    }

}

