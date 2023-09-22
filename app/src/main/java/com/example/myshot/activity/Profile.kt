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
    private lateinit var mAuth: FirebaseAuth

    private lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()

        setUserDetails()

        setLogout()

        binding.manageProfile.setOnClickListener{
            val intent = Intent(this, UpdateProfile::class.java)
            startActivity(intent)
        }

        binding.profileBack.setOnClickListener {

            finish()
        }
    }

    private fun setLogout() {

        binding.logoutButton.setOnClickListener {
            mAuth.signOut()
            SharedPref.putBoolean(SharedConst.IS_USER_LOGGED_IN, false)

            SharedPref.putData(SharedConst.USER_NAME, "null")
            SharedPref.putData(SharedConst.USER_EMAIL, "null")

            startActivity(Intent(this, GetStartActivity::class.java))
            finish()
        }
    }

    private fun setUserDetails() {
        val n = SharedPref.getData(SharedConst.USER_NAME)
        if (n == "null") {

            fetchData()

        } else {
            binding.userName.text = SharedPref.getData(SharedConst.USER_NAME)
            binding.email.text = SharedPref.getData(SharedConst.USER_EMAIL)
        }
    }

    private fun fetchData() {

        val db = FirebaseFirestore.getInstance()

        val documentID = mAuth.currentUser?.uid

        if (documentID != null) {
            db.collection("users").document(documentID).get()
                .addOnSuccessListener {
                    if (it.exists()) {
                        val x = it.getString("name")
                        val y = it.getString("email")
                        binding.userName.text = x
                        binding.email.text = y
                        x?.let { it1 -> SharedPref.putData(SharedConst.USER_NAME, it1) }
                        y?.let { it1 -> SharedPref.putData(SharedConst.USER_EMAIL, it1) }
                    }
                }
        }
    }
}