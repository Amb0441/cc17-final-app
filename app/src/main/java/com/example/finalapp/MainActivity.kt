package com.example.finalapp

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var notificationIconContainer: FrameLayout
    private lateinit var settingsIconContainer: FrameLayout
    private lateinit var topBar: LinearLayout
    private var userType: String = "student" // Track user type

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Initialize views
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        notificationIconContainer = findViewById(R.id.notificationIconContainer)
        settingsIconContainer = findViewById(R.id.settingsIconContainer)
        topBar = findViewById(R.id.top_bar)

        ViewCompat.setOnApplyWindowInsetsListener(bottomNavigationView) { v, insets ->
            val navBarInsets = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            v.setPadding(navBarInsets.left, 0, navBarInsets.right, 0)
            WindowInsetsCompat.CONSUMED
        }

        // Get user type from SignInActivity
        userType = intent.getStringExtra("USER_TYPE") ?: "student"

        // Set up click listener for notification bell icon
        notificationIconContainer.setOnClickListener {
            Toast.makeText(this, "Notifications clicked", Toast.LENGTH_SHORT).show()
            loadFragment(NotificationFragment())
        }

        // Set up click listener for settings icon
        settingsIconContainer.setOnClickListener {
            Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show()
            loadFragment(SettingFragment())
        }

        var selectedFragment: Fragment? = null

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> {
                    // Load appropriate home fragment based on user type
                    if (userType == "tutor") {
                        selectedFragment = HomeFragmentTutor()
                        Toast.makeText(this, "Dashboard Tutor", Toast.LENGTH_SHORT).show()
                    } else {
                        selectedFragment = Home_learner()
                        Toast.makeText(this, "Home Learner", Toast.LENGTH_SHORT).show()
                    }
                    loadFragment(selectedFragment!!)
                    true
                }
                R.id.menu_list -> {
                    // Load appropriate list fragment based on user type
                    if (userType == "tutor") {
                        selectedFragment = ListFragmentTutor()
                        Toast.makeText(this, "List Tutor", Toast.LENGTH_SHORT).show()
                    } else {
                        selectedFragment = ListFragmentLearner()
                        Toast.makeText(this, "List Learner", Toast.LENGTH_SHORT).show()
                    }
                    loadFragment(selectedFragment!!)
                    true
                }
                R.id.menu_leaderboard -> {
                    selectedFragment = LeaderBoardFragment()
                    Toast.makeText(this, "Leaderboard", Toast.LENGTH_SHORT).show()
                    loadFragment(selectedFragment!!)
                    true
                }
                R.id.menu_thread -> {
                    selectedFragment = ThreadFragment()
                    Toast.makeText(this, "Thread", Toast.LENGTH_SHORT).show()
                    loadFragment(selectedFragment!!)
                    true
                }
                R.id.menu_profile -> {
                    // Load appropriate profile fragment based on user type
                    if (userType == "tutor") {
                        selectedFragment = ProfileFragmentTutor()
                        Toast.makeText(this, "Profile Tutor", Toast.LENGTH_SHORT).show()
                    } else {
                        selectedFragment = ProfileFragmentLearner()
                        Toast.makeText(this, "Profile Learner", Toast.LENGTH_SHORT).show()
                    }
                    loadFragment(selectedFragment!!)
                    true
                }
                else -> false
            }
        }

        // Load initial fragment based on user type (only on first creation)
        if (savedInstanceState == null) {
            selectedFragment = if (userType == "tutor") {
                HomeFragmentTutor()
            } else {
                Home_learner()
            }
            loadFragment(selectedFragment!!)
        }

        // Set the bottom navigation to home by default
        bottomNavigationView.selectedItemId = R.id.menu_home
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()

        // Show or hide icons based on fragment type
        updateIconVisibility(fragment)
    }

    private fun updateIconVisibility(fragment: Fragment) {
        // Completely hide top bar for Notification, Setting, Profile, and Leaderboard fragments
        when (fragment) {
            is NotificationFragment,
            is SettingFragment,
            is ProfileFragmentTutor,
            is ProfileFragmentLearner,
            is LeaderBoardFragment -> {
                // Hide the entire top bar to free up space
                topBar.visibility = View.GONE
            }
            else -> {
                // Show top bar for other fragments (Home, List, Thread)
                topBar.visibility = View.VISIBLE
            }
        }
    }
}