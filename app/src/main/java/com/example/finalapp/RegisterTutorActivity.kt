package com.example.finalapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.io.ByteArrayOutputStream

class RegisterTutorActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var imageButton: ImageButton
    private var selectedImageUri: Uri? = null
    private var base64Image: String? = null

    private val resultContract = registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
        result?.let {
            selectedImageUri = it
            imageButton.setImageURI(it)
            base64Image = encodeImage(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_tutor_activity)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        imageButton = findViewById(R.id.UploadButton)
        imageButton.setOnClickListener { resultContract.launch("image/*") }

        val firstNameInput = findViewById<EditText>(R.id.stud_signup_firstname)
        val lastNameInput = findViewById<EditText>(R.id.stud_signup_lastname)
        val usernameInput = findViewById<EditText>(R.id.stud_signup_username)
        val emailInput = findViewById<EditText>(R.id.stud_signup_email)
        val passwordInput = findViewById<EditText>(R.id.stud_signup_password)
        val confirmPasswordInput = findViewById<EditText>(R.id.stud_signup_password_confirm)
        val linkedinInput = findViewById<EditText>(R.id.linkedin_link)
        val signupButton = findViewById<View>(R.id.signup_button)

        signupButton.setOnClickListener {
            val firstName = firstNameInput.text.toString().trim()
            val lastName = lastNameInput.text.toString().trim()
            val username = usernameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString()
            val confirmPassword = confirmPasswordInput.text.toString()
            val linkedinLink = linkedinInput.text.toString().trim()

            when {
                firstName.isEmpty() -> {
                    firstNameInput.error = "First name is required"
                    return@setOnClickListener
                }
                lastName.isEmpty() -> {
                    lastNameInput.error = "Last name is required"
                    return@setOnClickListener
                }
                username.isEmpty() -> {
                    usernameInput.error = "Username is required"
                    return@setOnClickListener
                }
                email.isEmpty() -> {
                    emailInput.error = "Email is required"
                    return@setOnClickListener
                }
                !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    emailInput.error = "Invalid email format"
                    return@setOnClickListener
                }
                !email.endsWith("@gmail.com", ignoreCase = true) -> {
                    emailInput.error = "Only Gmail addresses are allowed"
                    return@setOnClickListener
                }
                password.length < 6 -> {
                    passwordInput.error = "Password must be at least 6 characters"
                    return@setOnClickListener
                }
                password != confirmPassword -> {
                    confirmPasswordInput.error = "Passwords do not match"
                    return@setOnClickListener
                }
                linkedinLink.isEmpty() -> {
                    linkedinInput.error = "LinkedIn link is required"
                    return@setOnClickListener
                }
                base64Image == null -> {
                    Toast.makeText(this, "Please upload your certificate image", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            registerTutor(firstName, lastName, username, email, password, linkedinLink)
        }

        findViewById<View>(R.id.login_redirect_text).setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }
    }

    private fun registerTutor(
        firstName: String,
        lastName: String,
        username: String,
        email: String,
        password: String,
        linkedin: String
    ) {
        Toast.makeText(this, "Creating account...", Toast.LENGTH_SHORT).show()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                val userId = authResult.user?.uid ?: return@addOnSuccessListener

                val tutorData = hashMapOf(
                    "firstName" to firstName,
                    "lastName" to lastName,
                    "username" to username,
                    "email" to email,
                    "linkedin" to linkedin,
                    "certificateBase64" to base64Image,
                    "userType" to "tutor",
                    "createdAt" to System.currentTimeMillis()
                )

                firestore.collection("users").document(userId)
                    .set(tutorData)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Tutor registered successfully!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("USER_TYPE", "tutor")
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Failed to save data: ${e.message}", Toast.LENGTH_LONG).show()
                    }
            }
            .addOnFailureListener {
                val errorMessage = when {
                    it.message?.contains("email address is already in use", ignoreCase = true) == true ->
                        "This email is already registered"
                    it.message?.contains("network", ignoreCase = true) == true ->
                        "Network error. Check your connection"
                    else -> "Registration failed: ${it.message}"
                }
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
            }
    }

    private fun encodeImage(uri: Uri): String? {
        val bitmap: Bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        } else {
            @Suppress("DEPRECATION")
            MediaStore.Images.Media.getBitmap(contentResolver, uri)
        }

        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
        val bytes = outputStream.toByteArray()
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }
}
