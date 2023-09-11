package com.example.myshot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myshot.NavigationBarFragment.HomeFragment
import javax.net.ssl.HostnameVerifier

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().replace(R.id.container,HomeFragment()).commit()

    }




}