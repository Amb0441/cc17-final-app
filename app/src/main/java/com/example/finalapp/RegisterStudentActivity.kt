package com.example.finalapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterStudentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_student_activity)

        // Get references to input fields
        val firstNameInput = findViewById<EditText>(R.id.stud_signup_firstname)
        val lastNameInput = findViewById<EditText>(R.id.stud_signup_lastname)
        val usernameInput = findViewById<EditText>(R.id.stud_signup_username)
        val emailInput = findViewById<EditText>(R.id.stud_signup_email)
        val passwordInput = findViewById<EditText>(R.id.stud_signup_password)
        val confirmPasswordInput = findViewById<EditText>(R.id.stud_signup_password_confirm)

        val signupButton = findViewById<View>(R.id.signup_button)
        signupButton.setOnClickListener {
            // Get input values
            val firstName = firstNameInput.text.toString().trim()
            val lastName = lastNameInput.text.toString().trim()
            val username = usernameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString()
            val confirmPassword = confirmPasswordInput.text.toString()

            // Validate inputs
            when {
                firstName.isEmpty() -> {
                    firstNameInput.error = "First name is required"
                    firstNameInput.requestFocus()
                }
                lastName.isEmpty() -> {
                    lastNameInput.error = "Last name is required"
                    lastNameInput.requestFocus()
                }
                username.isEmpty() -> {
                    usernameInput.error = "Username is required"
                    usernameInput.requestFocus()
                }
                email.isEmpty() -> {
                    emailInput.error = "Email is required"
                    emailInput.requestFocus()
                }
                !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    emailInput.error = "Please enter a valid email"
                    emailInput.requestFocus()
                }
                password.isEmpty() -> {
                    passwordInput.error = "Password is required"
                    passwordInput.requestFocus()
                }
                password.length < 6 -> {
                    passwordInput.error = "Password must be at least 6 characters"
                    passwordInput.requestFocus()
                }
                confirmPassword.isEmpty() -> {
                    confirmPasswordInput.error = "Please confirm your password"
                    confirmPasswordInput.requestFocus()
                }
                password != confirmPassword -> {
                    confirmPasswordInput.error = "Passwords do not match"
                    confirmPasswordInput.requestFocus()
                }
                else -> {
                    // All validations passed
                    // TODO: Save user data to database/backend here

                    Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()

                    // Navigate to MainActivity (or whatever Activity hosts fragment_home_learner)
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
            }
        }

        val loginRedirectText = findViewById<View>(R.id.login_redirect_text)
        loginRedirectText.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}