package com.example.myshot.fragment

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.myshot.R
import com.example.myshot.adapter.SimilarIdeasAdapter
import com.example.myshot.dataClass.IdeasData
import com.example.myshot.databinding.FragmentIdeasDetailsBinding
import com.example.myshot.decorationClass.MarginClassGridLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class IdeasDetailsFragment : Fragment() {

    private lateinit var binding: FragmentIdeasDetailsBinding
    private lateinit var ideasList: MutableList<IdeasData>
    private lateinit var isLikedData: ArrayList<Long>
    private val db = FirebaseFirestore.getInstance()
    private val mAuth = FirebaseAuth.getInstance()
    private val userID = mAuth.currentUser?.uid
    private val documentRef = userID?.let {
        db.collection("users")
            .document(it)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIdeasDetailsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val name = arguments?.getString("key_name").toString()
        binding.typeOfIdeas.text = capitalizeWords(name)

        val photographerName = arguments?.getString("key_photographer_name").toString()
        val id = arguments?.getLong("key_id")

        init()

        if (id != null) {
            callFetchIsLikedName(photographerName,id)
        }

        // setPhotographerDetails(id)

        fetchSimilarIdeas(name)

        binding.ideasDetailsBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.photographerDetails.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("key", photographerName)
            findNavController().navigate(R.id.action_ideasDetailsFragment_to_details, bundle)
        }

    }

    private fun init() {
        ideasList = mutableListOf()
        isLikedData = ArrayList()
    }

    private fun callFetchIsLikedName(name:String,id: Long) {
        CoroutineScope(Dispatchers.Main).launch {
            fetchIsLikedName(name,id)
        }
    }

    private suspend fun fetchIsLikedName(name: String,id:Long) {
        try {
            val documentSnapshot = documentRef?.get()?.await()
            if (documentSnapshot != null && documentSnapshot.exists()) {
                val data = documentSnapshot.data?.get("isLikedIdeasArray") as? ArrayList<Long>
                if (data != null) {
                    isLikedData = data
                }
                setPhotographerDetails(name)
                fetchIsLiked(id)
            } else {

            }
        } catch (exception: Exception) {
            Toast.makeText(context, "$exception", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchSimilarIdeas(id: String) {

        db.collection("ideas")
            .document(id)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    val data = documentSnapshot.data
                    if (data != null) {
                        for ((key, value) in data) {
                            if (value is List<*> && value.isNotEmpty() && value[0] is Map<*, *>) {
                                val arrayOfMaps = value as List<Map<String, Any>>
                                for (map in arrayOfMaps) {
                                    val name = map["name"].toString()
                                    val image = map["image"].toString()
                                    val id = map["id"] as Long
                                    val liked = false
                                    ideasList.add(IdeasData(id, name, image, liked))
                                }
                                setAdapter(id)
                                break
                            }
                        }
                    } else {

                    }
                } else {

                }
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "Error getting document: ", exception)
            }
    }

    private fun setPhotographerDetails(id: String) {

        val fieldName = "name"

        db.collection("photographerInfo")
            .whereEqualTo(fieldName, id)
            .get()
            .addOnSuccessListener {

                for (i in it) {
                    val data = i.data

                    binding.name.text = capitalizeWords(id)

                    Glide.with(this)
                        .load(data["image"])
                        .into(binding.photographerImage)

                }
            }
    }

    private fun capitalizeWords(sentence: String): String {
        val words = sentence.split(" ")
        val capitalizedWords = words.map { it.capitalize() }
        return capitalizedWords.joinToString(" ")
    }

    private fun setAdapter(id: String) {

        binding.similarPhotoRecycler.apply {
            layoutManager = GridLayoutManager(activity, 2)
            adapter = SimilarIdeasAdapter(context) {
                val bundle = Bundle()
                bundle.putString("key_name", id)
                bundle.putString("key_id", it.name)
                findNavController().navigate(
                    R.id.action_ideasDetailsFragment_self,
                    bundle
                )
            }
        }
        (binding.similarPhotoRecycler.adapter as SimilarIdeasAdapter).epList = ideasList

        val spacing = 8
        val spanCount = 2 // 2 columns grid
        val includeEdge = false // Include spacing at the edges of the RecyclerView
        binding.similarPhotoRecycler.addItemDecoration(
            MarginClassGridLayout(
                spanCount,
                spacing,
                includeEdge
            )
        )

    }

    private fun fetchIsLiked(id: Long) {

        var liked = id in isLikedData

        if (liked) {
            binding.isLiked.setImageResource(R.drawable.baseline_favorite_24)

        } else {
            binding.isLiked.setImageResource(R.drawable.baseline_favorite_border_24)
        }

        val zoomIn = AnimationUtils.loadAnimation(context, R.anim.zoom_in)

        binding.isLikedButton.setOnClickListener {
            liked = !liked
            if (liked) {
                setIsLiked(id)
                binding.isLiked.setImageResource(R.drawable.baseline_favorite_24)

            } else {
                removeIsLiked(id)
                binding.isLiked.setImageResource(R.drawable.baseline_favorite_border_24)
            }
            binding.isLikedButton.startAnimation(zoomIn)

        }

    }

    private fun setIsLiked(id: Long) {

        documentRef?.get()?.addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val existingArray =
                    documentSnapshot.get("isLikedIdeasArray") as? List<Long> ?: emptyList()

                val updatedArray = existingArray.toMutableList().apply {
                    add(id)
                }

                val data = hashMapOf(
                    "isLikedIdeasArray" to updatedArray
                )

                documentRef.update(data as Map<String, Any>)
                    .addOnSuccessListener {
                        println("New string added to Firestore array successfully!")
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(
                            context,
                            "Failed to update Firestore: $exception",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            } else {
                println("Document does not exist")
            }
        }?.addOnFailureListener { exception ->
            Toast.makeText(
                context,
                "Failed to retrieve Firestore document: $exception",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    private fun removeIsLiked(name: Long) {

        documentRef?.get()?.addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val existingArray =
                    documentSnapshot.get("isLikedIdeasArray") as? List<Long> ?: emptyList()

                val updatedArray = existingArray.toMutableList().apply {
                    remove(name)
                }

                val data = hashMapOf(
                    "isLikedIdeasArray" to updatedArray
                )

                documentRef.update(data as Map<String, Any>)
                    .addOnSuccessListener {
                        println("String removed from Firestore array successfully!")
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(
                            context,
                            "Failed to update Firestore: $exception",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            } else {
                println("Document does not exist")
            }
        }?.addOnFailureListener { exception ->
            Toast.makeText(
                context,
                "Failed to retrieve Firestore document: $exception",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    companion object {

        fun newInstance() = IdeasDetailsFragment()
    }
}