package com.example.myshot.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myshot.R
import com.example.myshot.fragment.HomeFragment
import com.example.myshot.fragment.PhotoDetailFragmnet

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().replace(R.id.container, PhotoDetailFragmnet()).commit()

    }


}