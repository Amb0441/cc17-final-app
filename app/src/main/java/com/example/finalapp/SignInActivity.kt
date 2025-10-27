package com.example.finalapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class SignInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        val usernameInput = findViewById<EditText>(R.id.login_username)
        val loginButton = findViewById<Button>(R.id.login_button)

        loginButton.setOnClickListener {
            val username = usernameInput.text.toString().trim()

            when (username.lowercase()) {
                "student" -> {
                    // Home_learner is a Fragment - navigate via MainActivity
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("USER_TYPE", "student")
                    startActivity(intent)
                    finish()
                }

                "tutor" -> {
                    // HomeFragmentTutor is a Fragment - navigate via MainActivity
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("USER_TYPE", "tutor")
                    startActivity(intent)
                    finish()
                }

                else -> {
                    Toast.makeText(
                        this,
                        "Invalid username. Please enter 'student' or 'tutor'.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        val signupRedirectText = findViewById<View>(R.id.signup_redirect_text)
        signupRedirectText.setOnClickListener {
            val intent = Intent(this, RegisterAsActivity::class.java)
            startActivity(intent)
        }
    }
}
