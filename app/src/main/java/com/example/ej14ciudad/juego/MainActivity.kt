package com.example.ej14ciudad.juego

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ej14ciudad.AuthActivity
import com.example.ej14ciudad.R
import com.example.ej14ciudad.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstaceState: Bundle?) {
        super.onCreate(savedInstaceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imageView.setBackgroundResource(R.drawable.paisaje)

        binding.imageButton.setOnClickListener {
            when ((1..4).random()) {
                1 -> Intent (this, Objeto::class.java).apply {
                    startActivity(this)
                }
                2 -> Intent (this, Ciudad::class.java).apply {
                    startActivity(this)
                }
                3 -> Intent (this, Mercader::class.java).apply {
                    startActivity(this)
                }
                4 -> Intent (this, Enemigo::class.java).apply {
                    startActivity(this)
                }
            }
        }

        binding.logOut.setOnClickListener {
            val prefs = getSharedPreferences("PREFERENCE_FILE_KEY", MODE_PRIVATE).edit()
            prefs.clear()
            prefs.apply()
            Intent(this, AuthActivity::class.java).apply {
                startActivity(this)
            }
        }
    }

    override fun onBackPressed() {
        Toast.makeText(this, "Presione el botón de cerrar sesión", Toast.LENGTH_LONG).show()
    }
}