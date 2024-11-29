package com.example.agrimarttrader

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.agrimarttrader.Class.MyClass
import com.example.agrimarttrader.Model.Trader
import com.example.agrimarttrader.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().getReference("Trader")
        auth = FirebaseAuth.getInstance()

        binding.signup.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        val name = binding.name.text.toString().trim()
        var phone = binding.phone.text.toString().trim()
        val password = binding.password.text.toString().trim()
        val confirmPassword = binding.cpassword.text.toString().trim()

        if (name.isEmpty()) {
            binding.name.error = "Name is required"
            return
        }
        if (phone.isEmpty()) {
            binding.phone.error = "Phone number is required"
            return
        }
        if (!phone.matches(Regex("^\\+880\\d{10}$"))) {
            binding.phone.error = "Enter a valid phone number (+880XXXXXXXXXX)"
            return
        }
        if (password.isEmpty() || password.length < 6) {
            binding.password.error = "Password must be at least 6 characters"
            return
        }
        if (password != confirmPassword) {
            binding.cpassword.error = "Passwords do not match"
            return
        }

        phone = phone.replace("+88","")

        auth.createUserWithEmailAndPassword(phone + "@gmail.com", password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    val traderId = phone
                    val trader = Trader(traderId, name, phone, "", password, "0")

                    if (traderId != null) {
                        database.child(traderId).setValue(trader)
                            .addOnCompleteListener { dbTask ->
                                if (dbTask.isSuccessful) {
                                    Toast.makeText(this, "Sign-up successful!", Toast.LENGTH_SHORT).show()
                                    auth.signOut()
                                    startActivity(Intent(this,LoginActivity::class.java))
                                } else {
                                    Toast.makeText(this, "Database error: ${dbTask.exception?.message}", Toast.LENGTH_LONG).show()
                                }
                            }
                    }
                } else {
                    Toast.makeText(this, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }
}
