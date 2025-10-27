package com.example.finalapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

class RegisterAsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_as_a_tutor_or_student)

        val tutorButton = findViewById<Button>(R.id.tutor_register_button)
        tutorButton.setOnClickListener {
            val intent = Intent(this, RegisterTutorActivity::class.java)
            startActivity(intent)
        }

        val studentButton = findViewById<Button>(R.id.student_register_button)
        studentButton.setOnClickListener {
            val intent = Intent(this, RegisterStudentActivity::class.java)
            startActivity(intent)
        }

    }
}