package com.example.myshot.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myshot.databinding.ActivityUpdateProfileBinding


class UpdateProfile : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            finish()
        }


    }
}