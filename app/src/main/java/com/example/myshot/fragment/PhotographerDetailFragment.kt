package com.example.myshot.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ScrollView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myshot.R
import com.example.myshot.adapter.ReviewsAdapter
import com.example.myshot.adapter.ServicesAdapter
import com.example.myshot.dataClass.ReviewsData
import com.example.myshot.databinding.FragmentPhotographDetailBinding
import com.example.myshot.decorationClass.MarginClassHorizontalLinearLayout
import com.example.myshot.decorationClass.MarginClassVerticalLinearLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.DecimalFormat


class PhotographerDetailFragment : Fragment() {

    private lateinit var binding: FragmentPhotographDetailBinding
    private lateinit var reviewsList: MutableList<ReviewsData>
    private lateinit var servicesList: ArrayList<String>
    private lateinit var locationsList: ArrayList<String>
    private lateinit var nameId: String
    private lateinit var contact: String
    private lateinit var scrollView: ScrollView
    private lateinit var isLikedData: ArrayList<String>
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
        binding = FragmentPhotographDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nameId = arguments?.getString("key").toString()

        setInit()

        callFetchIsLikedName()

        setReviews()

        setButtonWhileScrolling()

        setServices()

        setLocations()

        binding.contactNowButton.setOnClickListener {
            openWhatsApp(contact)
        }

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun openWhatsApp(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("https://api.whatsapp.com/send?phone=$phoneNumber")
        startActivity(intent)
    }

    private fun setLocations() {

        val fieldId = "name"
        val fieldName = "location".lowercase()

        db.collection("photographerInfo")
            .whereEqualTo(fieldId, nameId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    val data = document.data
                    if (data.containsKey(fieldName)) {
                        val array = data[fieldName] as? List<String>
                        if (array != null) {
                            locationsList.addAll(array)


                            binding.locationRecycler.apply {
                                layoutManager =
                                    LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
                                adapter = ServicesAdapter()

                            }
                            (binding.locationRecycler.adapter as ServicesAdapter).epList =
                                locationsList

                            val margin = MarginClassHorizontalLinearLayout(0, 60, 30, 5)
                            binding.locationRecycler.addItemDecoration(margin)
                        }

                    }
                }
            }
            .addOnFailureListener { exception ->

            }


    }

    private fun setServices() {

        val fieldId = "name"
        val fieldName = "services".lowercase()

        db.collection("photographerInfo")
            .whereEqualTo(fieldId, nameId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    val data = document.data
                    if (data.containsKey(fieldName)) {
                        val array = data[fieldName] as? List<String>
                        if (array != null) {
                            servicesList.addAll(array)

                            binding.servicesRecycler.apply {
                                Log.d("ram", servicesList.size.toString())
                                layoutManager =
                                    LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
                                adapter = ServicesAdapter()

                            }
                            (binding.servicesRecycler.adapter as ServicesAdapter).epList =
                                servicesList

                            val margin = MarginClassHorizontalLinearLayout(0, 60, 30, 5)
                            binding.servicesRecycler.addItemDecoration(margin)
                        }


                    }
                }
            }
            .addOnFailureListener { exception ->

            }

    }


    private fun setButtonWhileScrolling() {
        scrollView.viewTreeObserver.addOnScrollChangedListener {
            val scrollY = scrollView.scrollY

            if (scrollY > 500) {
                binding.contactNowButton.visibility = View.VISIBLE
            } else {
                binding.contactNowButton.visibility = View.INVISIBLE
            }
        }
    }

    private fun fetchDetails() {

        val fieldName = "name"

        db.collection("photographerInfo")
            .whereEqualTo(fieldName, nameId)
            .get()
            .addOnSuccessListener {

                for (i in it) {
                    val data = i.data

                    binding.manageProfile.text = capitalizeWords(data["name"].toString())

                    binding.name.text = capitalizeWords(data["name"].toString())

                    contact = data["contact"].toString()
                    binding.rating.text = data["rating"].toString()
                    binding.address.text = data["address"].toString()

                    binding.photoPrice.text =
                        "Rs." + formatRupeesWithCommas(data["price"].toString().toLong())

                    fetchIsLiked(data["name"].toString())

                    Glide.with(this)
                        .load(data["image"])
                        .into(binding.image)
                }
            }
    }

    private fun fetchIsLiked(name: String) {

        var liked = name in isLikedData

        if (liked) {
            binding.isLiked.setImageResource(R.drawable.baseline_favorite_24)

        } else {
            binding.isLiked.setImageResource(R.drawable.baseline_favorite_border_24)
        }

        val zoomIn = AnimationUtils.loadAnimation(context, R.anim.zoom_in)

        binding.isLikedButton.setOnClickListener {
            liked = !liked
            if (liked) {
                setIsLiked(name)
                binding.isLiked.setImageResource(R.drawable.baseline_favorite_24)

            } else {
                removeIsLiked(name)
                binding.isLiked.setImageResource(R.drawable.baseline_favorite_border_24)
            }
            binding.isLikedButton.startAnimation(zoomIn)

        }

    }

    private fun setInit() {
        reviewsList = mutableListOf()
        scrollView = binding.scrollView
        isLikedData = ArrayList()
        locationsList = ArrayList()
        servicesList = ArrayList()
    }

    private fun setReviews() {

        reviewsList.add(
            ReviewsData(
                "Stark",
                "5.0",
                "Perfect Picture!!",
                "We are India’s leading wedding photography and filmmaking company. Our award winning team documents stories of love from all around the world.",
                "Featured in Indian and international publications like Wedisson Awards, WeddingSutra and Better Photography."
            )
        )
        reviewsList.add(
            ReviewsData(
                "Square",
                "4.7",
                "Perfect Picture!!",
                "We are India’s leading wedding photography and filmmaking company. Our award winning team documents stories of love from all around the world.",
                "Featured in Indian and international publications like Wedisson Awards, WeddingSutra and Better Photography."
            )
        )
        reviewsList.add(
            ReviewsData(
                "Dhiresh",
                "3.6",
                "Perfect Picture!!",
                "We are India’s leading wedding photography and filmmaking company. Our award winning team documents stories of love from all around the world.",
                "Featured in Indian and international publications like Wedisson Awards, WeddingSutra and Better Photography."
            )
        )
        reviewsList.add(
            ReviewsData(
                "Dark",
                "4.6",
                "Perfect Picture!!",
                "We are India’s leading wedding photography and filmmaking company. Our award winning team documents stories of love from all around the world.",
                "Featured in Indian and international publications like Wedisson Awards, WeddingSutra and Better Photography."
            )
        )

        binding.reviewRecycler.apply {
            layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
            adapter = ReviewsAdapter()
        }
        (binding.reviewRecycler.adapter as ReviewsAdapter).epList = reviewsList

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

    private suspend fun fetchIsLikedName() {
        try {
            val documentSnapshot = documentRef?.get()?.await()
            if (documentSnapshot != null && documentSnapshot.exists()) {
                val data = documentSnapshot.data?.get("isLikedArray") as? ArrayList<String>
                if (data != null) {
                    isLikedData = data
                }
                // Call the next function after fetching is complete
                fetchDetails()
            } else {
                // Handle case where document does not exist
            }
        } catch (exception: Exception) {
            Toast.makeText(context, "$exception", Toast.LENGTH_SHORT).show()
        }
    }

    private fun callFetchIsLikedName() {
        CoroutineScope(Dispatchers.Main).launch {
            fetchIsLikedName()
        }
    }

    private fun setIsLiked(name: String) {

        documentRef?.get()?.addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val existingArray =
                    documentSnapshot.get("isLikedArray") as? List<String> ?: emptyList()

                val updatedArray = existingArray.toMutableList().apply {
                    add(name)
                }

                val data = hashMapOf(
                    "isLikedArray" to updatedArray
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

    private fun removeIsLiked(name: String) {

        documentRef?.get()?.addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val existingArray =
                    documentSnapshot.get("isLikedArray") as? List<String> ?: emptyList()

                val updatedArray = existingArray.toMutableList().apply {
                    remove(name)
                }

                val data = hashMapOf(
                    "isLikedArray" to updatedArray
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

        fun newInstance() = PhotographerDetailFragment()

    }


}

