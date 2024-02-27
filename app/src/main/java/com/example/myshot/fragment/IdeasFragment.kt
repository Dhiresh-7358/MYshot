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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myshot.R
import com.example.myshot.adapter.IdeasAdapter
import com.example.myshot.dataClass.IdeasData
import com.example.myshot.dataClass.TopPhotoData
import com.example.myshot.databinding.FragmentIdeasBinding
import com.example.myshot.decorationClass.MarginClassVerticalLinearLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class IdeasFragment : Fragment() {

    private lateinit var binding: FragmentIdeasBinding
    private lateinit var ideasList: MutableList<IdeasData>
    private lateinit var isLikedData: ArrayList<Long>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIdeasBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments?.getString("key").toString()

        binding.ideaType.text = capitalizeWords(id)

        init()

        fetchIsLikedName(object : FetchIsLikedNameCallback {
            override fun onFetchComplete() {
                if (id != null) {
                    fetIdeas(id.lowercase())
                }
            }
        })

        binding.ideasBack.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun init() {

        ideasList = mutableListOf()
        isLikedData = ArrayList()
    }

    private fun fetchIsLikedName(callback: FetchIsLikedNameCallback) {
        val mAuth = FirebaseAuth.getInstance()
        val userID = mAuth.currentUser?.uid
        val db = FirebaseFirestore.getInstance()

        val documentRef = userID?.let {
            db.collection("users")
                .document(it)
        }
        documentRef?.get()
            ?.addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val data = documentSnapshot.data?.get("isLikedIdeasArray") as? ArrayList<Long>
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

    private fun fetIdeas(id: String) {

        val db = FirebaseFirestore.getInstance()

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
                                    val liked = likedMatch(id)
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
                // Handle failures
                Log.d(TAG, "Error getting document: ", exception)
            }
    }

    private fun likedMatch(id: Long): Boolean {

        return id in isLikedData
    }

    private fun setAdapter(name: String) {


        binding.idealsRecycler.apply {
            layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            adapter = IdeasAdapter(context) {
                val bundle = Bundle()
                bundle.putString("key_name", name)
                bundle.putString("key_photographer_name", it.name)
                bundle.putLong("key_id",it.id)
                findNavController().navigate(
                    R.id.action_ideasFragment_to_ideasDetailsFragment,
                    bundle
                )
            }
        }
        (binding.idealsRecycler.adapter as IdeasAdapter).epList = ideasList

        val margin = MarginClassVerticalLinearLayout(40, 40, 40, 40)
        binding.idealsRecycler.addItemDecoration(margin)
    }

    private fun capitalizeWords(sentence: String): String {
        val words = sentence.split(" ")
        val capitalizedWords = words.map { it.capitalize() }
        return capitalizedWords.joinToString(" ")
    }

    companion object {

        fun newInstance() = IdeasFragment()
    }
}