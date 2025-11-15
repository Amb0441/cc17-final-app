package com.example.finalapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragmentTutor : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    // UI Elements
    private lateinit var ivEdit: ImageView
    private lateinit var tvName: TextView
    private lateinit var tvUsername: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile_tutor, container, false)

        try {
            // Initialize Firebase
            auth = FirebaseAuth.getInstance()
            firestore = FirebaseFirestore.getInstance()

            // Initialize views
            initViews(view)

            // Set up listeners
            setupListeners()

            // Load user profile data
            loadUserProfile()

        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }

        return view
    }

    private fun initViews(view: View) {
        // Initialize edit icon (pencil icon)
        ivEdit = view.findViewById(R.id.ivEdit)

        // Initialize text views for displaying user data
        tvName = view.findViewById(R.id.tvName)
        tvUsername = view.findViewById(R.id.tvUsername)

        // Make sure the icon is clickable
        ivEdit.isClickable = true
        ivEdit.isFocusable = true
    }

    private fun setupListeners() {
        // Set up click listener for pencil icon to open edit profile
        ivEdit.setOnClickListener {
            try {
                val intent = Intent(requireContext(), EditProfileActivity::class.java)
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error opening edit profile", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
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
                        tvName.text = fullName
                        tvUsername.text = "@$username"

                        // Optional: Verify this is a tutor account
                        if (userType != "tutor") {
                            Toast.makeText(
                                requireContext(),
                                "Warning: This account is not registered as a tutor",
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
        /**
         * Use this factory method to create a new instance of
         * this fragment.
         *
         * @return A new instance of fragment ProfileFragment.
         */
        @JvmStatic
        fun newInstance() = ProfileFragmentTutor()
    }
}