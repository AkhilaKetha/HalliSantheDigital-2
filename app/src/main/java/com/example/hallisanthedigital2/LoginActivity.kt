package com.example.hallisanthedigital2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btn = findViewById<Button>(R.id.btnLogin)
        val phone = findViewById<EditText>(R.id.etPhone)

        btn.setOnClickListener {
            if (phone.text.length == 10) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Enter valid 10-digit number", Toast.LENGTH_SHORT).show()
            }
        }
    }
}