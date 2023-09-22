package com.example.myshot.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myshot.R
import com.example.myshot.activity.Profile
import com.example.myshot.activity.SelectCity
import com.example.myshot.adapter.CategoryAdapter
import com.example.myshot.adapter.IdeaAdapter
import com.example.myshot.adapter.TopPhotoAdapter
import com.example.myshot.dataClass.CategoryData
import com.example.myshot.dataClass.TopPhotoData
import com.example.myshot.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.*

//import com.squareup.picasso.Picasso

class HomeFragment : Fragment() {
    private lateinit var binging: FragmentHomeBinding
    private lateinit var categoryList: MutableList<CategoryData>
    private lateinit var topPhotographerList: MutableList<TopPhotoData>
    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View {

        binging = FragmentHomeBinding.inflate(inflater, container, false)
        mAuth = FirebaseAuth.getInstance() // Initialize mAuth
        return binging.root
    }

    companion object {
        const val REQUEST_CODE = 123 // You can choose any unique request code
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setSharedPref()

        setProfileImage()

        setCategoryList()

        topPhotographerList = mutableListOf()

        if(topPhotographerList.isEmpty()){
            fetchTopPhotographer()
        }
        else{
            setTopPhotographerAdapter()
        }

        setIdeaList()

        binging.city.setOnClickListener {
            val intent = Intent(requireContext(), SelectCity::class.java)
            startActivityForResult(intent, REQUEST_CODE)
        }

        binging.profile.setOnClickListener {
            val intent = Intent(requireContext(), Profile::class.java)
            startActivity(intent)
        }

        binging.seeAllTop.setOnClickListener {
            navigateToDestinationFragment()
        }

    }

    private fun setIdeaList() {

        var ideaList: MutableList<CategoryData> = mutableListOf()
        ideaList.add(CategoryData(R.drawable._2dd76f0e1ea89675802c343ff4ee261, "Bridal portrait"))
        ideaList.add(CategoryData(R.drawable._c42b830b8533e6fff4b5caf9e9894fd, "Wedding style"))
        ideaList.add(
            CategoryData(
                R.drawable.ccad32362b224d1351d2063ee1fa224d,
                "Romantic couple shot"
            )
        )
        ideaList.add(CategoryData(R.drawable.c0a48af4fd27fbeefc9448a397ef7a10, "Mehandi"))

        val ideaRecycler: RecyclerView = binging.ideaRec

        ideaRecycler.apply {
            layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
            adapter = IdeaAdapter() {

            }
        }
        (ideaRecycler.adapter as IdeaAdapter).epList = ideaList
    }

    private fun setCategoryList() {

        categoryList = mutableListOf()
        categoryList.add(CategoryData(R.drawable.wedding_rings, "Wedding"))
        categoryList.add(CategoryData(R.drawable.traveler, "Travel"))
        categoryList.add(CategoryData(R.drawable.residential, "Commercial"))
        categoryList.add(CategoryData(R.drawable.food, "Food"))
        categoryList.add(CategoryData(R.drawable.wedding_rings, "College"))

        val categoryRecycler: RecyclerView = binging.categoryRec

        categoryRecycler.apply {
            layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
            adapter = CategoryAdapter() {
                val categoryName=it.categoryName
                //supportFragmentManager.beginTransaction().replace(R.id.container, CategoryFragment()).commit()

            }
        }
        (categoryRecycler.adapter as CategoryAdapter).epList = categoryList
    }

    private fun setSharedPref() {
        val sharedPreferences =
            requireActivity().getSharedPreferences("myshot_preferences", Context.MODE_PRIVATE)
        val userCity = sharedPreferences.getString("city", "Delhi")

        binging.city.text = userCity
    }

    private fun setProfileImage() {

        val db = FirebaseFirestore.getInstance()
        val documentID = mAuth.currentUser?.uid

        if (documentID != null) {
            db.collection("users").document(documentID).get()
                .addOnSuccessListener {
                    if (it.exists()) {
                        val x = it.getString("name")?.capitalize()
                        val firstAlphabet: Char? = x?.get(0)
                        binging.userFirstWord.text = firstAlphabet.toString()
                    }
                }
        }
    }

    private fun fetchTopPhotographer() {

        val db = FirebaseFirestore.getInstance()

        val fieldName = "rating"

        db.collection("photographerInfo")
            .orderBy(fieldName, Query.Direction.DESCENDING)
            .limit(5)
            .get()
            .addOnSuccessListener {

                topPhotographerList.clear()
                for (document in it) {
                    val data = document.data
                    topPhotographerList.add(
                        TopPhotoData(
                            data["image"].toString(), data["name"].toString(),
                            data["rating"].toString(), data["price"].toString()
                        )
                    )
                }

              //  (topPhotoRecycler.adapter as? TopPhotoAdapter).notifyDataSetChanged()
                setTopPhotographerAdapter()

            }

    }

    private fun setTopPhotographerAdapter() {
        val topPhotoRecycler: RecyclerView = binging.topPhotoRec

        topPhotoRecycler.apply {
            layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
            adapter = TopPhotoAdapter() {
            }
        }
        (topPhotoRecycler.adapter as TopPhotoAdapter).epList = topPhotographerList

        // Toast.makeText(requireContext(), "fetch data successfully", Toast.LENGTH_SHORT).show()
    }

    private fun navigateToDestinationFragment() {
        val photographerFragment = PhotographerFragment()
        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager

        val transaction: FragmentTransaction = fragmentManager.beginTransaction()

        // Replace the current fragment with the destination fragment
        transaction.replace(R.id.container, photographerFragment)

        // Optional: Add the transaction to the back stack (for back navigation)
        transaction.addToBackStack(null)

        // Commit the transaction
        transaction.commit()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val district = data?.getStringExtra("district")
            binging.city.text = district
        }


    }


}