package com.faizalputraramadhan.adakonser

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.faizalputraramadhan.adakonser.database.DatabaseHelper
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {

    private lateinit var etUsername: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var btnLogin: MaterialButton
    private lateinit var btnRegister: MaterialButton
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check if already logged in
        val sharedPref = getSharedPreferences("AdaKonserPrefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)

        if (isLoggedIn) {
            val isAdmin = sharedPref.getBoolean("isAdmin", false)
            navigateToHome(isAdmin)
            return
        }

        setContentView(R.layout.activity_login)

        dbHelper = DatabaseHelper(this)

        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        btnRegister = findViewById(R.id.btnRegister)

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Username dan Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            handleLogin(username, password)
        }

        btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun handleLogin(username: String, password: String) {
        // Check if admin
        if (username == "admin" && password == "admin123") {
            saveLoginState(true, true, "admin")
            Toast.makeText(this, "Login sebagai Admin berhasil!", Toast.LENGTH_SHORT).show()
            navigateToHome(true)
            return
        }

        // Check regular user from database
        val user = dbHelper.loginUser(username, password)
        if (user != null) {
            saveLoginState(true, false, username)
            Toast.makeText(this, "Login berhasil! Selamat datang, $username", Toast.LENGTH_SHORT).show()
            navigateToHome(false)
        } else {
            Toast.makeText(this, "Username atau Password salah!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveLoginState(isLoggedIn: Boolean, isAdmin: Boolean, username: String) {
        val sharedPref = getSharedPreferences("AdaKonserPrefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean("isLoggedIn", isLoggedIn)
            putBoolean("isAdmin", isAdmin)
            putString("username", username)
            apply()
        }
    }

    private fun navigateToHome(isAdmin: Boolean) {
        val intent = if (isAdmin) {
            Intent(this, AdminDashboardActivity::class.java)
        } else {
            Intent(this, MainActivity::class.java)
        }
        startActivity(intent)
        finish()
    }
}