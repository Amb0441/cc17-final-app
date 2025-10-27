package com.example.finalapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast

class Home_learner : Fragment() {

    private lateinit var notificationIconContainer: FrameLayout
    private lateinit var settingsIconContainer: FrameLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_learner, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the fragmented icon containers
        notificationIconContainer = view.findViewById(R.id.notificationIconContainer)
        settingsIconContainer = view.findViewById(R.id.settingsIconContainer)
        val viewAllButton = view.findViewById<Button>(R.id.btn_view_all)

        // Set up click listener for notification bell icon
        notificationIconContainer.setOnClickListener {
            // Handle notification icon click
            Toast.makeText(requireContext(), "Notifications clicked", Toast.LENGTH_SHORT).show()

            // Navigate to NotificationFragment
            navigateToFragment(NotificationFragment())
        }

        // Set up click listener for settings icon
        settingsIconContainer.setOnClickListener {
            // Handle settings icon click
            Toast.makeText(requireContext(), "Settings clicked", Toast.LENGTH_SHORT).show()

            // Navigate to SettingsFragment
            navigateToFragment(SettingFragment())
        }

        viewAllButton.setOnClickListener {
            // Handle view all messages click
        }
    }

    // Helper method to navigate to another fragment
    private fun navigateToFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}
