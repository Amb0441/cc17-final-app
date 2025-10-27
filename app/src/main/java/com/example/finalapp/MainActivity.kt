package com.example.finalapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private var userType: String = "student" // Track user type

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottom_navigation)

        ViewCompat.setOnApplyWindowInsetsListener(bottomNavigationView) { v, insets ->
            val navBarInsets = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            v.setPadding(navBarInsets.left, 0, navBarInsets.right, 0)
            WindowInsetsCompat.CONSUMED
        }

        // Get user type from SignInActivity
        userType = intent.getStringExtra("USER_TYPE") ?: "student"

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
    }
}