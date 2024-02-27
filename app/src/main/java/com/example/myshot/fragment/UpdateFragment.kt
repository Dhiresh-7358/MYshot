package com.example.myshot.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myshot.databinding.FragmentUpdateBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import loginProcess.SharedConst
import loginProcess.SharedPref

@Suppress("DEPRECATION")
class UpdateFragment : Fragment() {

    private lateinit var binding: FragmentUpdateBinding
    private lateinit var name: String
    private lateinit var email: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpdateBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDetails()

        binding.backButton.setOnClickListener {
            requireFragmentManager().popBackStack()
        }

        binding.saveButton.setOnClickListener {
            updateDetails(requireContext())
        }

    }

    private fun updateDetails(context: Context) {

        val db = FirebaseFirestore.getInstance()
        val mAuth = FirebaseAuth.getInstance()
        val documentID = mAuth.currentUser?.uid

        val newName = binding.nameUpdate.text.toString()
        val newEmail = binding.emailUpdate.text.toString()

        var b1 = false

        if (newName.isNotEmpty()) {
            if (name != newName) {

                b1 = true
                val updates = hashMapOf(
                    "name" to newName,
                )

                if (documentID != null) {
                    db.collection("users").document(documentID)
                        .update(updates as Map<String, Any>)
                        .addOnSuccessListener {
                            SharedPref.putData(SharedConst.USER_NAME, newName)
                            Toast.makeText(
                                context,
                                "Name is update successfully ",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_SHORT)
                                .show()
                        }
                }
            }
        } else {
            Toast.makeText(context, "Name is can't be empty", Toast.LENGTH_SHORT)
                .show()
        }

        if (newEmail.isNotEmpty()) {
            if (email != newEmail) {

                b1 = true

                val updates = hashMapOf(
                    "email" to newEmail
                )

                if (documentID != null) {
                    db.collection("users").document(documentID)
                        .update(updates as Map<String, Any>)
                        .addOnSuccessListener {
                            SharedPref.putData(SharedConst.USER_EMAIL, newEmail)
                            Toast.makeText(
                                requireContext(),
                                "Email is  update successfully ",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Error: $it", Toast.LENGTH_SHORT)
                                .show()
                        }
                }
            }
        } else {
            Toast.makeText(context, "Email is can't be empty", Toast.LENGTH_SHORT)
                .show()
        }

        if (b1) {
            findNavController().popBackStack()
        }

    }

    private fun setDetails() {

        name = SharedPref.getData(SharedConst.USER_NAME).toString()
        email = SharedPref.getData(SharedConst.USER_EMAIL).toString()

        binding.nameUpdate.setText(name)
        binding.emailUpdate.setText(email)

        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            val phoneNumber = user.phoneNumber
            binding.mobileNumber.text= phoneNumber
        } else {
            // User is not signed in
        }


    }

    companion object {
        fun newInstance() = UpdateFragment()
    }
}