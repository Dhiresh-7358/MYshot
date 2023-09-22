package com.example.myshot.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myshot.databinding.ActivityUpdateprofileBinding



class UpdateProfile : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateprofileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateprofileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            finish()
        }


    }
}