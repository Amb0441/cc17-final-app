package com.example.finalapp
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.view.View


class EditProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        // Initialize back button
        val btnBack = findViewById<View>(R.id.btnBack)
        btnBack.setOnClickListener {
            finish() // Close this activity and return to profile
        }

        // Initialize save button
        val btnSave = findViewById<View>(R.id.btnSave)
        btnSave.setOnClickListener {
            // TODO: Save profile changes to database
            Toast.makeText(this, "Profile saved!", Toast.LENGTH_SHORT).show()
            finish() // Return to profile after saving
        }

        // TODO: Add image picker functionality for banner and profile picture
    }
}