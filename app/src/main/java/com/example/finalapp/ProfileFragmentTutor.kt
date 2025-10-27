package com.example.finalapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast

class ProfileFragmentTutor : Fragment() {

    private lateinit var ivEdit: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile_tutor, container, false)

        try {
            // Initialize views
            initViews(view)

            // Set up listeners
            setupListeners()
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }

        return view
    }

    private fun initViews(view: View) {
        // Initialize edit icon (pencil icon)
        ivEdit = view.findViewById(R.id.ivEdit)

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

        // TODO: Add other UI initialization and button listeners here
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