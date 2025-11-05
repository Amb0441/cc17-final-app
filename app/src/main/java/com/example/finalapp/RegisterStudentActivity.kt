package com.example.finalapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterStudentActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_student_activity)

        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Input fields
        val firstNameInput = findViewById<EditText>(R.id.stud_signup_firstname)
        val lastNameInput = findViewById<EditText>(R.id.stud_signup_lastname)
        val usernameInput = findViewById<EditText>(R.id.stud_signup_username)
        val emailInput = findViewById<EditText>(R.id.stud_signup_email)
        val passwordInput = findViewById<EditText>(R.id.stud_signup_password)
        val confirmPasswordInput = findViewById<EditText>(R.id.stud_signup_password_confirm)

        val signupButton = findViewById<View>(R.id.signup_button)
        val loginRedirectText = findViewById<View>(R.id.login_redirect_text)

        signupButton.setOnClickListener {
            val firstName = firstNameInput.text.toString().trim()
            val lastName = lastNameInput.text.toString().trim()
            val username = usernameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString()
            val confirmPassword = confirmPasswordInput.text.toString()

            // Validation
            when {
                firstName.isEmpty() -> firstNameInput.error = "First name is required"
                lastName.isEmpty() -> lastNameInput.error = "Last name is required"
                username.isEmpty() -> usernameInput.error = "Username is required"
                email.isEmpty() -> emailInput.error = "Email is required"
                !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> emailInput.error = "Invalid email"
                password.length < 6 -> passwordInput.error = "Password must be 6+ characters"
                password != confirmPassword -> confirmPasswordInput.error = "Passwords do not match"
                else -> registerStudent(firstName, lastName, username, email, password)
            }
        }

        loginRedirectText.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }
    }

    private fun registerStudent(firstName: String, lastName: String, username: String, email: String, password: String) {
        Toast.makeText(this, "Creating account...", Toast.LENGTH_SHORT).show()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        val userData = hashMapOf(
                            "firstName" to firstName,
                            "lastName" to lastName,
                            "username" to username,
                            "email" to email,
                            "userType" to "student",
                            "createdAt" to System.currentTimeMillis()
                        )

                        firestore.collection("users").document(userId)
                            .set(userData)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()

                                val intent = Intent(this, MainActivity::class.java)
                                intent.putExtra("USER_TYPE", "student")
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                                finish()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Failed to save data: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                    }
                } else {
                    val error = when {
                        task.exception?.message?.contains("email address is already in use") == true ->
                            "This email is already registered"
                        task.exception?.message?.contains("network") == true ->
                            "Network error. Check your connection"
                        else -> "Registration failed: ${task.exception?.message}"
                    }
                    Toast.makeText(this, error, Toast.LENGTH_LONG).show()
                }
            }
    }
}
