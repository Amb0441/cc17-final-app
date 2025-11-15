package com.example.finalapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragmentLearner : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    // UI Elements
    private lateinit var tvStudentName: TextView
    private lateinit var tvStudentUsername: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile_learner, container, false)

        // Initialize Firebase
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Initialize UI elements
        tvStudentName = view.findViewById(R.id.tvStudentName)
        tvStudentUsername = view.findViewById(R.id.tvStudentUsername)

        // Load user data
        loadUserProfile()

        return view
    }

    private fun loadUserProfile() {
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val userId = currentUser.uid

            // Fetch user data from Firestore
            firestore.collection("users").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        // Get data from Firestore
                        val firstName = document.getString("firstName") ?: ""
                        val lastName = document.getString("lastName") ?: ""
                        val username = document.getString("username") ?: ""
                        val email = document.getString("email") ?: ""
                        val userType = document.getString("userType") ?: ""

                        // Display the data
                        val fullName = "$firstName $lastName"
                        tvStudentName.text = fullName
                        tvStudentUsername.text = "@$username"

                        // Optional: Verify this is a student account
                        if (userType != "student") {
                            Toast.makeText(
                                requireContext(),
                                "Warning: This account is not registered as a student",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "User profile not found",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(
                        requireContext(),
                        "Error loading profile: ${exception.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
        } else {
            // No user is signed in
            Toast.makeText(
                requireContext(),
                "No user logged in",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProfileFragmentLearner()
    }
}