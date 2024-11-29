package com.example.agrimarttrader

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.agrimarttrader.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        var user = auth.currentUser
        if(user!=null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.create.setOnClickListener{
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        binding.login.setOnClickListener {
            var phone = binding.phone.text.toString().trim()
            val password = binding.password.text.toString().trim()

            if (phone.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!phone.startsWith("+880") || phone.length != 14) {
                Toast.makeText(this, "Invalid phone number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            phone = phone.replace("+88","")

            login(phone, password)
        }
    }




    private fun login(phone: String, password: String) {
        auth.signInWithEmailAndPassword(phone + "@gmail.com", password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                     startActivity(Intent(this, MainActivity::class.java))
                     finish()
                } else {
                    Toast.makeText(this, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }
}
