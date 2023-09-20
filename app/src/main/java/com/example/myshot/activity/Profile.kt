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
        if(SharedPref.containsKey("username")) {
            SharedPref.getData("username")
            SharedPref.getData("user_email")
        }
        else{

        }
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
                        val x=it.getString("name")
                        val y=it.getString("email")
                        binding.userName.text=x
                        binding.email.text=y
                        x?.let { it1 -> SharedPref.putData("username", it1) }
                        y?.let { it1 -> SharedPref.putData("user_email", it1) }
                    }
                }
        }
    }
}