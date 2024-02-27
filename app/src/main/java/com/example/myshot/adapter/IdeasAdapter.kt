package com.example.myshot.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myshot.dataClass.CategoryData
import com.example.myshot.R
import com.example.myshot.dataClass.IdeasData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class IdeasAdapter(private val context: Context, private val listener: (IdeasData) -> Unit) :
    RecyclerView.Adapter<IdeasAdapter.MyViewHolder>() {

    private val mAuth = FirebaseAuth.getInstance()
    private val userID = mAuth.currentUser?.uid
    private val db = FirebaseFirestore.getInstance()
    private val documentRef = userID?.let {
        db.collection("users")
            .document(it)
    }

    var epList = mutableListOf<IdeasData>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.r_ideals, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return epList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        with(holder) {
            with(epList[position]) {

                var liked = this.ideaIsLiked

                if (liked) {
                    isLikedImage.setImageResource(R.drawable.baseline_favorite_24)

                } else {
                    isLikedImage.setImageResource(R.drawable.baseline_favorite_border_24)
                }

                val zoomIn = AnimationUtils.loadAnimation(context, R.anim.zoom_in)

                isLike.setOnClickListener {
                    liked = !liked
                    if (liked) {
                        setIsLiked(this.id)
                        isLikedImage.setImageResource(R.drawable.baseline_favorite_24)

                    } else {
                          removeIsLiked(this.id)
                        isLikedImage.setImageResource(R.drawable.baseline_favorite_border_24)
                    }
                    isLike.startAnimation(zoomIn)

                }

                Glide.with(context)
                    .load(this.ideasImage)
                    .into(img)
            }
        }
    }

    private fun removeIsLiked(name: Long) {

        documentRef?.get()?.addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val existingArray = documentSnapshot.get("isLikedIdeasArray") as? List<Long> ?: emptyList()

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
                        Toast.makeText(context, "Failed to update Firestore: $exception", Toast.LENGTH_SHORT).show()
                    }
            } else {
                println("Document does not exist")
            }
        }?.addOnFailureListener { exception ->
            Toast.makeText(context, "Failed to retrieve Firestore document: $exception", Toast.LENGTH_SHORT).show()
        }

    }

    private fun setIsLiked(name: Long) {

        documentRef?.get()?.addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val existingArray = documentSnapshot.get("isLikedIdeasArray") as? List<Long> ?: emptyList()

                val updatedArray = existingArray.toMutableList().apply {
                    add(name)
                }

                val data = hashMapOf(
                    "isLikedIdeasArray" to updatedArray
                )

                documentRef.update(data as Map<String, Any>)
                    .addOnSuccessListener {
                        println("New string added to Firestore array successfully!")
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(context, "Failed to update Firestore: $exception", Toast.LENGTH_SHORT).show()
                    }
            } else {
                println("Document does not exist")
            }
        }?.addOnFailureListener { exception ->
            Toast.makeText(context, "Failed to retrieve Firestore document: $exception", Toast.LENGTH_SHORT).show()
        }

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img: ImageView = itemView.findViewById(R.id.idea_img)
        val isLikedImage: ImageView = itemView.findViewById(R.id.liked_button)
        val isLike: CardView = itemView.findViewById(R.id.idea_is_liked)


        init {
            itemView.setOnClickListener {
                listener.invoke(epList[adapterPosition])
            }
        }
    }
}