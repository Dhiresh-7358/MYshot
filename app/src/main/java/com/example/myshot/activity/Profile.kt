package com.example.myshot.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.myshot.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import loginProcess.GetStartActivity
import loginProcess.SharedConst
import loginProcess.SharedPref


class Profile : AppCompatActivity() {
    private lateinit var mAuth:FirebaseAuth

    private lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth=FirebaseAuth.getInstance()

        binding.logoutButton.setOnClickListener{
            mAuth.signOut()
            SharedPref.putBoolean(SharedConst.IS_USER_LOGGED_IN, false)
            startActivity(Intent(this, GetStartActivity::class.java))
            finish()
        }

        binding.userName.text=SharedPref.getData("username","User")
        binding.email.text=SharedPref.getData("user_email","user@gmail.com")

        fetchData()

        binding.profileBack.setOnClickListener{

            finish()
        }
    }

    private fun fetchData() {

        val db= FirebaseFirestore.getInstance()

        val documentID= mAuth.currentUser?.uid
        Log.d("fire","id: $documentID")

        if (documentID != null) {
            db.collection("users").document(documentID).get()
                .addOnSuccessListener {
                    if (it.exists()){
                        //binding.userName.text=it.getString("name")
                        //binding.email.text=it.getString("email")
                    }
                }
        }
    }
}