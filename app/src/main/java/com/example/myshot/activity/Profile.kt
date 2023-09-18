package com.example.myshot.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myshot.R
import com.example.myshot.databinding.ActivityProfileBinding
import com.example.myshot.databinding.ActivitySelectCityBinding

class Profile : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.profileBack.setOnClickListener{
            finish()
        }
    }
}