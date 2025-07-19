package com.azamovhudstc.logosmart.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import android.widget.Toast
import com.azamovhudstc.logosmart.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            val username = binding.usernameInput.text.toString().trim()
            val password = binding.passwordInput.text.toString().trim()

            if (username == "lobar" && password == "techaward") {
                Toast.makeText(this, "Xush kelibsiz, $username!", Toast.LENGTH_SHORT).show()
                 startActivity(Intent(this, MainActivity::class.java)) // yoki boshqa sahifaga o‘ting
            } else {
                Toast.makeText(this, "Login yoki parol noto‘g‘ri", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
