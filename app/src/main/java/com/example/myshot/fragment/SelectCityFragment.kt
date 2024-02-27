package com.example.myshot.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.fragment.findNavController
import com.example.myshot.R
import com.example.myshot.activity.MainActivity
import com.example.myshot.databinding.FragmentSelectCityBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import loginProcess.SharedPref
import java.util.*


class SelectCityFragment : Fragment() {

    private lateinit var binding: FragmentSelectCityBinding
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val permissionId = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelectCityBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        binding.getLocation.setOnClickListener {
          //  getLocation()
        }

        binding.backButton.setOnClickListener{
//            val intent = Intent(this, MainActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
            findNavController().popBackStack()
        }
    }

//    @SuppressLint("MissingPermission", "SetTextI18n")
//    private fun getLocation() {
//        if (checkPermissions()) {
//            Log.d("fire","district: 0")
//            if (isLocationEnabled()) {
//                mFusedLocationClient.lastLocation.addOnSuccessListener { location ->
//                    Log.d("fire","district: 1")
//                    if (location != null) {
//                        val geocoder = Geocoder(this, Locale.getDefault())
//                        val list  = geocoder.getFromLocation(location.latitude, location.longitude, 1) as List<Address>
//                        if (list.isNotEmpty()) {
//                            val address = list[0]
//                            val district = address.subAdminArea
//
//                            SharedPref.putData("city",district)
//
//                            val resultIntent = Intent()
//                            resultIntent.putExtra("district", district)
//
//                            setResult(Activity.RESULT_OK, resultIntent)
//                            finish()
//                        }
//                    } else {
//                        // Handle the case where last known location is null
//                        Toast.makeText(this, "Location will be available soon", Toast.LENGTH_LONG).show()
//                    }
//                }
//            } else {
//                Toast.makeText(this, "Please turn on location", Toast.LENGTH_LONG).show()
//                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
//                startActivity(intent)
//            }
//        } else {
//            requestPermissions()
//        }
//    }
//
//    private fun isLocationEnabled(): Boolean {
//        val locationManager: LocationManager =
//            getSystemService(Context.LOCATION_SERVICE) as LocationManager
//        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
//            LocationManager.NETWORK_PROVIDER
//        )
//    }
//
//    private fun checkPermissions(): Boolean {
//        return ActivityCompat.checkSelfPermission(
//            this,
//            Manifest.permission.ACCESS_COARSE_LOCATION
//        ) == PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(
//                    this,
//                    Manifest.permission.ACCESS_FINE_LOCATION
//                ) == PackageManager.PERMISSION_GRANTED
//    }
//
//    private fun requestPermissions() {
//        ActivityCompat.requestPermissions(
//            this,
//            arrayOf(
//                Manifest.permission.ACCESS_COARSE_LOCATION,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ),
//            permissionId
//        )
//    }
//
//    @SuppressLint("MissingSuperCall")
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<String>,
//        grantResults: IntArray
//    ) {
//        if (requestCode == permissionId && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            getLocation()
//        }
//    }

    companion object {

        fun newInstance() = SelectCityFragment()
    }
}