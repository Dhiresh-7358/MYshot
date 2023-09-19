package com.example.myshot.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Binder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myshot.dataClass.CategoryData
import com.example.myshot.R
import com.example.myshot.activity.Profile
import com.example.myshot.activity.SelectCity
import com.example.myshot.adapter.CategoryAdapter
import com.example.myshot.adapter.IdeaAdapter
import com.example.myshot.adapter.TopPhotoAdapter
import com.example.myshot.dataClass.TopPhotoData
import com.example.myshot.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
//import com.squareup.picasso.Picasso

class HomeFragment : Fragment() {
    private lateinit var binging: FragmentHomeBinding
    private lateinit var categoryList: MutableList<CategoryData>
    private lateinit var topPhotographerList: MutableList<TopPhotoData>
    private lateinit var mList:MutableList <String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View? {

        binging = FragmentHomeBinding.inflate(inflater, container, false)
        return binging.root
    }

    companion object {
        const val REQUEST_CODE = 123 // You can choose any unique request code
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireActivity().getSharedPreferences("myshot_preferences", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("city", "Delhi")
        binging.city.text=username

        mList= mutableListOf()

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

            }
        }
        (categoryRecycler.adapter as CategoryAdapter).epList = categoryList

        topPhotographerList = mutableListOf()

      //  fetchTopPhotographer()
        //     setImage()

        topPhotographerList.add(
            TopPhotoData(
                R.drawable.anna_unsplash,
                "The Square",
                "5.0",
                "80,000"
            )
        )
        topPhotographerList.add(
            TopPhotoData(
                R.drawable.alvaro_cvg_mw8izdx7n8e_unsplash,
                "Mac Studio",
                "4.8",
                "60,000"
            )
        )
        topPhotographerList.add(
            TopPhotoData(
                R.drawable.anna_unsplash,
                "Yogi Creations",
                "4.9",
                "1,20,000"
            )
        )


        val topPhotoRecycler: RecyclerView = binging.topPhotoRec

        topPhotoRecycler.apply {
            layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
            adapter = TopPhotoAdapter() {

            }
        }
        (topPhotoRecycler.adapter as TopPhotoAdapter).epList = topPhotographerList


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


        //when clicked on city button
        val city: View = binging.city
        city.setOnClickListener {
            val intent = Intent(requireContext(), SelectCity::class.java)
            startActivityForResult(intent, REQUEST_CODE)
        }

        binging.profile.setOnClickListener {
            val intent = Intent(requireContext(), Profile::class.java)
            startActivity(intent)
        }

        val seeAll: View = binging.seeAllTop
        seeAll.setOnClickListener {
            navigateToDestinationFragment()
        }

    }

    private fun setImage() {
        Log.d("fire","fun calllled")
//        Picasso.get().load(uri).into(binging.photographerImage)
        Log.d("fire","fun end")
    }

    private fun fetchTopPhotographer() {
        val db = FirebaseFirestore.getInstance()

        val fieldName = "rating"

        db.collection("photographerInfo")
            .orderBy(fieldName, Query.Direction.DESCENDING)
            .limit(5)
            .get()
            .addOnSuccessListener {

            for (i in it){
                mList.add(i.data["name"].toString())
            }
                Toast.makeText(requireContext(), "fetch data ${mList[0]}", Toast.LENGTH_SHORT).show()
//
//             Picasso.get().load(mList[0]).into(binging.photographerImage)
             //   binging.photographerImage.setImageResource(mList[0])


//                topPhotographerList.clear()
//                for (document in it) {
//                    val data = document.data
//                    topPhotographerList.add(
//                        TopPhotoData(
//                            data["image"] as Int, data["name"].toString(),
//                            data["rating"] as String, data["price"].toString()
//                        )
//                    )
//                }
//                Toast.makeText(requireContext(), "fetch data succ", Toast.LENGTH_SHORT).show()
            }

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
            // Now you have the district data from SelectCity Activity
            // You can use it as needed in your HomeFragment
            binging.city.text = district
        }


    }




}