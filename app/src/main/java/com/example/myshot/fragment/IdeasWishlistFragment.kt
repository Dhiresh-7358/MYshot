package com.example.myshot.fragment

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myshot.R
import com.example.myshot.adapter.IdeasWishlistAdapter
import com.example.myshot.dataClass.IdeasData
import com.example.myshot.dataClass.TopPhotoData
import com.example.myshot.databinding.FragmentIdeasWishlistBinding
import com.example.myshot.decorationClass.MarginClassGridLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class IdeasWishlistFragment : Fragment() {

    private lateinit var binding: FragmentIdeasWishlistBinding
    private lateinit var ideasList: MutableList<IdeasData>
    private lateinit var isLikedData: ArrayList<Long>
    private val db = FirebaseFirestore.getInstance()
    private val mAuth = FirebaseAuth.getInstance()
    private val userID = mAuth.currentUser?.uid
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
        binding = FragmentIdeasWishlistBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        inti()

        // setIdeasRecycler()

        callFetchIsLikedName()

    }

    private fun inti() {
        ideasList = mutableListOf()
        isLikedData = ArrayList()
    }

    private fun callFetchIsLikedName() {
        CoroutineScope(Dispatchers.Main).launch {
            fetchIsLikedName()
        }
    }

    private suspend fun fetchIsLikedName() {
        try {
            val documentSnapshot = documentRef?.get()?.await()
            if (documentSnapshot != null && documentSnapshot.exists()) {
                val data = documentSnapshot.data?.get("isLikedIdeasArray") as? ArrayList<Long>
                if (data != null) {
                    isLikedData = data

                    for (i in isLikedData) {
                        fetPhotographers(i)
                    }

                }
            } else {

            }
        } catch (exception: Exception) {
            Toast.makeText(context, "$exception", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetPhotographers(id: Long) {

        val fieldName = "id"
        val fieldValueToMatch = id

        db.collection("ideas")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val data = document.data
                    if (data != null) {
                        for ((key, value) in data) {
                            if (value is List<*>) {
                                for (element in value) {
                                    if (element is Map<*, *>) {
                                        val id = element[fieldName]
                                        if (id is Long && id == fieldValueToMatch) {
                                            Log.d("ram", element["id"].toString())
                                            val idd = element["id"]
                                            val name = element["name"]
                                            val image = element["image"]
                                            val ideaCategoryName = document.id
                                            ideasList.add(
                                                IdeasData(
                                                    idd as Long,
                                                    name as String, image as String, true,
                                                    ideaCategoryName
                                                )
                                            )
                                            if (isLikedData.size == ideasList.size) {
                                                setIdeasRecycler()
                                            }
                                            break
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        Log.d("ram", "Document data is null for document ${document.id}")
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("ram", "Error getting documents", exception)
            }

    }


    private fun setIdeasRecycler() {

//        ideasList.add(IdeasData(R.drawable._2dd76f0e1ea89675802c343ff4ee261, false))
//        ideasList.add(IdeasData(R.drawable._c42b830b8533e6fff4b5caf9e9894fd, false))
//        ideasList.add(IdeasData(R.drawable.ccad32362b224d1351d2063ee1fa224d, false))
//        ideasList.add(IdeasData(R.drawable.c0a48af4fd27fbeefc9448a397ef7a10, false))

        binding.ideaWishlistRecycler.apply {
            layoutManager = GridLayoutManager(activity, 2)
            adapter = IdeasWishlistAdapter(requireContext()) {
                val bundle = Bundle()
                bundle.putString("key_name", it.categoryIdeaName)
                bundle.putString("key_photographer_name", it.name)
                bundle.putLong("key_id", it.id)
                findNavController().navigate(
                    R.id.action_wishlistFragment_to_ideasDetailsFragment,
                    bundle
                )
            }
        }
        (binding.ideaWishlistRecycler.adapter as IdeasWishlistAdapter).epList = ideasList

        val spacing = 8
        val spanCount = 2 // 2 columns grid
        val includeEdge = false // Include spacing at the edges of the RecyclerView
        binding.ideaWishlistRecycler.addItemDecoration(
            MarginClassGridLayout(
                spanCount,
                spacing,
                includeEdge
            )
        )

    }

    companion object {

        fun newInstance() = IdeasWishlistFragment()
    }
}