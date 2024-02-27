package com.example.myshot.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myshot.R
import com.example.myshot.adapter.CategoryAdapter
import com.example.myshot.adapter.IdeaAdapter
import com.example.myshot.adapter.TopPhotoAdapter
import com.example.myshot.dataClass.CategoryData
import com.example.myshot.dataClass.TopPhotoData
import com.example.myshot.databinding.FragmentHomeBinding
import com.example.myshot.decorationClass.MarginClassHorizontalLinearLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import loginProcess.SharedConst
import loginProcess.SharedPref
import java.util.*

//import com.square.picasso.Picasso

var isFetch = false

class HomeFragment : Fragment() {
    private lateinit var binging: FragmentHomeBinding
    private lateinit var categoryList: MutableList<CategoryData>
    private lateinit var topPhotographerList: MutableList<TopPhotoData>
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var documentID: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {

        binging = FragmentHomeBinding.inflate(inflater, container, false)
        return binging.root
    }

    companion object {
        const val REQUEST_CODE = 123 // You can choose any unique request code
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        inIt()

        setCity()

        setSharedPref()

        setProfileImage()

        setCategoryList()

        fetchTopPhotographer()

        setIdeaList()

        binging.city.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Currently, this functionality isn't accessible",
                Toast.LENGTH_SHORT
            ).show()
        }

        binging.profile.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_profile)
        }

        binging.seeAllTop.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("key", "Top Photographers")
            findNavController().navigate(R.id.action_home_to_photographers, bundle)
        }

        binging.searchBar.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("key", "All Photographers")
            findNavController().navigate(R.id.action_home_to_photographers, bundle)
        }

//        binging.seeAllIdeas.setOnClickListener {
//            findNavController().navigate(R.id.action_home_to_ideasFragment)
//        }

    }

    private fun inIt() {

        db = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()

        documentID = mAuth.currentUser?.uid.toString()

        categoryList = mutableListOf()
        topPhotographerList = mutableListOf()
    }

    private fun setCity() {

        // SharedPref.getData(SharedConst.USER_CITy).toString()
//        if(city=="null"){
//            findNavController().navigate(R.id.action_home_to_selectCity)
//        }

    }

    private fun setIdeaList() {

        val ideaList: MutableList<CategoryData> = mutableListOf()
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
                val bundle = Bundle()
                bundle.putString("key", it.categoryName)
                Log.d("ram", it.categoryName)
                findNavController().navigate(R.id.action_home_to_ideasFragment, bundle)
            }
        }
        (ideaRecycler.adapter as IdeaAdapter).epList = ideaList

        val margin = MarginClassHorizontalLinearLayout(0, 40, 28, 5)
        ideaRecycler.addItemDecoration(margin)
    }

    private fun setCategoryList() {

        categoryList.add(CategoryData(R.drawable.wedding_rings, "Wedding"))
        categoryList.add(CategoryData(R.drawable.traveler, "Travel"))
        categoryList.add(CategoryData(R.drawable.food, "Food"))
        categoryList.add(CategoryData(R.drawable.wedding_rings, "College"))
        categoryList.add(CategoryData(R.drawable.residential, "Commercial"))

        val categoryRecycler: RecyclerView = binging.categoryRec

        categoryRecycler.apply {
            layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
            adapter = CategoryAdapter() {

                val bundle = Bundle()
                bundle.putString("key", it.categoryName)
                findNavController().navigate(R.id.action_home_to_photographers, bundle)

            }
        }
        (categoryRecycler.adapter as CategoryAdapter).epList = categoryList

        val margin = MarginClassHorizontalLinearLayout(0, 40, 28, 5)
        categoryRecycler.addItemDecoration(margin)
    }

    private fun setSharedPref() {
//        val userCity = SharedPref.getData("city")
//
//        binging.city.text = userCity
    }

    private fun setProfileImage() {


        val name = SharedPref.getData(SharedConst.USER_NAME).toString()

        if (name != "null") {

            val firstAlphabet: Char = name.get(0)
            Log.d("fire", firstAlphabet.toString())
            binging.userFirstWord.text = firstAlphabet.toString()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        } else {
            db.collection("users").document(documentID).get()
                .addOnSuccessListener { it ->
                    if (it.exists()) {
                        val x = it.getString("name")
                        val firstAlphabet: Char? = x?.get(0)
                        binging.userFirstWord.text = firstAlphabet.toString()
                            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
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
                            data["rating"].toString(), data["price"].toString(),

                            )
                    )
                }
                isFetch = true
                setTopPhotographerAdapter()

            }

    }

    private fun setTopPhotographerAdapter() {

        binging.topPhotoRec.setHasFixedSize(true)

        binging.topPhotoRec.apply {
            layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
            adapter = TopPhotoAdapter {

                val bundle = Bundle()
                bundle.putString("key", it.PhotographerName)
                findNavController().navigate(R.id.action_home_to_details, bundle)

            }
        }
        (binging.topPhotoRec.adapter as TopPhotoAdapter).epList = topPhotographerList

        val margin = MarginClassHorizontalLinearLayout(0, 40, 28, 5)
        binging.topPhotoRec.addItemDecoration(margin)
    }

    private fun navigateToDestinationFragment() {
        val photographerFragment = PhotographersFragment()
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