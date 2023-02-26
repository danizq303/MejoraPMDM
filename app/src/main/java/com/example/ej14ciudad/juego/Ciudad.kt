package com.example.ej14ciudad.juego

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ej14ciudad.R
import com.example.ej14ciudad.databinding.ActivityCiudadBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson

class Ciudad : AppCompatActivity() {
    private lateinit var binding: ActivityCiudadBinding
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCiudadBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imageView.setBackgroundResource(R.drawable.ciudad)

        binding.button.setOnClickListener {
            val prefs = getSharedPreferences("PREFERENCE_FILE_KEY", MODE_PRIVATE).edit()
            prefs.putBoolean("isJuego", true)
            prefs.apply()
            irJuego()
        }

        binding.button2.setOnClickListener {
            Intent (this, MainActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    private fun irJuego() {
        val prefs = getSharedPreferences("PREFERENCE_FILE_KEY", MODE_PRIVATE)
        val email = prefs.getString("email", null)

        var personaje = Personaje()
        db.collection("users").document(email!!).get().addOnSuccessListener {
            personaje = Gson().fromJson(it.get("personaje").toString(), Personaje::class.java)
        }

        Toast.makeText(
            this,
            "Numero de victorias: ${personaje.battallasGanadas}",
            Toast.LENGTH_LONG
        ).show()

        getSharedPreferences("PREFERENCE_FILE_KEY", MODE_PRIVATE)
        val isJuego = prefs.getBoolean("isJuego", false)

        if (isJuego) {
            when ((1..3).random()) {
                1 -> Intent(this, Objeto::class.java).apply {
                    startActivity(this)
                }
                2 -> Intent(this, Enemigo::class.java).apply {
                    startActivity(this)
                }
                3 -> Intent(this, Mercader::class.java).apply {
                    startActivity(this)
                }
            }
        }
    }
}