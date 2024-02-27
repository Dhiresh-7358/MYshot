package com.example.myshot.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myshot.R
import com.example.myshot.databinding.FragmentProfilBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import loginProcess.SharedConst
import loginProcess.SharedPref

@Suppress("DEPRECATION")
class ProfileFragment : Fragment() {


    private lateinit var mAuth: FirebaseAuth
    lateinit var binding: FragmentProfilBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfilBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mAuth = FirebaseAuth.getInstance()

        setUserDetails()

        setLogout()

        binding.manageProfile.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_update)
        }

        binding.profileBack.setOnClickListener {
            requireFragmentManager().popBackStack()
        }

        binding.wishlistCard.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_wishlistFragment)
        }

        binding.orderView.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Currently, this functionality isn't accessible",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.manageView.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Currently, this functionality isn't accessible",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.helpView.setOnClickListener {
            openEmailApp()
        }

        binding.aboutAs.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_aboutAsFragment)
        }

        binding.termsOfUse.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_TOUFragment)
        }

        binding.privacyPolicy.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_privacyPolicyFragment)
        }

        binding.faqs.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_FAQsFragment)
        }
    }

    private fun openEmailApp() {

        val emailSend = "dhiresh.yadav.virat@gmail.com"
        val emailSubject = "Help"

        val intent = Intent(Intent.ACTION_SEND)

        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(emailSend))
        intent.putExtra(Intent.EXTRA_SUBJECT, emailSubject)

        intent.type = "message/rfc822"

        startActivity(Intent.createChooser(intent, "Choose an Email client:"))

    }

    private fun setLogout() {

        binding.logoutButton.setOnClickListener {

            binding.logoutText.visibility = View.INVISIBLE
            binding.logoutProgressBar.visibility = View.VISIBLE

            SharedPref.putBoolean(SharedConst.IS_USER_LOGGED_IN, false)

            SharedPref.putData(SharedConst.USER_NAME, "null")
            SharedPref.putData(SharedConst.USER_EMAIL, "null")

            mAuth.signOut()

            binding.logoutProgressBar.visibility = View.INVISIBLE
            binding.logoutText.visibility = View.VISIBLE

            findNavController().navigate(R.id.action_profile_to_getStart)
        }
    }

    private fun setUserDetails() {
        val n = SharedPref.getData(SharedConst.USER_NAME)
        if (n == "null") {

            fetchData()

        } else {
            binding.userName.text =
                capitalizeWords(SharedPref.getData(SharedConst.USER_NAME).toString())
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
                        binding.userName.text = capitalizeWords(x.toString())
                        binding.email.text = y
                        x?.let { it1 -> SharedPref.putData(SharedConst.USER_NAME, it1) }
                        y?.let { it1 -> SharedPref.putData(SharedConst.USER_EMAIL, it1) }
                    }
                }
        }
    }

    private fun capitalizeWords(sentence: String): String {
        val words = sentence.split(" ")
        val capitalizedWords = words.map { it.capitalize() }
        return capitalizedWords.joinToString(" ")
    }

    companion object {
        fun newInstance() = ProfileFragment()
    }
}