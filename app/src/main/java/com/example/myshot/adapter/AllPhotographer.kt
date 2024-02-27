package com.example.myshot.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.myshot.R
import com.example.myshot.dataClass.TopPhotoData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import java.text.DecimalFormat

class AllPhotographer(
    private val listener: (TopPhotoData) -> Unit,
    private val context: Context
) : RecyclerView.Adapter<AllPhotographer.MyViewHolder>() {

    private val mAuth = FirebaseAuth.getInstance()
    private val userID = mAuth.currentUser?.uid
    private val db = FirebaseFirestore.getInstance()
    private val documentRef = userID?.let {
        db.collection("users")
            .document(it)
    }

    var epList = mutableListOf<TopPhotoData>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.r_photographer, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return epList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        with(holder) {
            with(epList[position]) {

                var liked = this.isLiked

                if (liked) {
                    isLikeImg.setImageResource(R.drawable.baseline_favorite_24)

                } else {
                    isLikeImg.setImageResource(R.drawable.baseline_favorite_border_24)
                }

                val zoomIn = AnimationUtils.loadAnimation(context, R.anim.zoom_in)

                isLike.setOnClickListener {
                    liked = !liked
                    if (liked) {
                        setIsLiked(this.PhotographerName)
                        isLikeImg.setImageResource(R.drawable.baseline_favorite_24)

                    } else {
                        removeIsLiked(this.PhotographerName)
                        isLikeImg.setImageResource(R.drawable.baseline_favorite_border_24)
                    }
                    isLike.startAnimation(zoomIn)

                }

                Picasso.get().load(this.PhotographerImg).into(pImg)

                val capitalizedSentence = capitalizeWords(this.PhotographerName)
                pName.text = capitalizedSentence

                pRating.text = this.PhotographerRating.toFloat().toString()

                val formattedAmount = formatRupeesWithCommas(this.PhotographerPrice.toLong())
                pPrice.text = formattedAmount
            }
        }

    }

    private fun removeIsLiked(name: String) {

        documentRef?.get()?.addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val existingArray = documentSnapshot.get("isLikedArray") as? List<String> ?: emptyList()

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
                        Toast.makeText(context, "Failed to update Firestore: $exception", Toast.LENGTH_SHORT).show()
                    }
            } else {
                println("Document does not exist")
            }
        }?.addOnFailureListener { exception ->
            Toast.makeText(context, "Failed to retrieve Firestore document: $exception", Toast.LENGTH_SHORT).show()
        }

    }

    private fun setIsLiked(name: String) {

        documentRef?.get()?.addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val existingArray = documentSnapshot.get("isLikedArray") as? List<String> ?: emptyList()

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

        val pImg: ImageView = itemView.findViewById(R.id.photograph_img)
        val pName: TextView = itemView.findViewById(R.id.photograph_name)
        val pRating: TextView = itemView.findViewById(R.id.photograph_rating)
        val pPrice: TextView = itemView.findViewById(R.id.photograph_price)
        val isLike: CardView = itemView.findViewById(R.id.is_liked)
        val isLikeImg: ImageView = itemView.findViewById(R.id.liked_button)

        init {
            itemView.setOnClickListener {
                listener.invoke(epList[adapterPosition])
            }
        }
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
}