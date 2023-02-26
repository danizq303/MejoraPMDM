package com.example.ej14ciudad.juego

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ej14ciudad.R
import com.example.ej14ciudad.databinding.ActivityObjetoBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson

class Objeto : AppCompatActivity() {
    private lateinit var binding: ActivityObjetoBinding
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityObjetoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imageView.setBackgroundResource(R.drawable.kart)

        val prefs = getSharedPreferences("PREFERENCE_FILE_KEY", MODE_PRIVATE)
        val email = prefs.getString("email", null)
        val isJuego = prefs.getBoolean("isJuego", false)

        binding.button.setOnClickListener {
            var personaje = Personaje()

            if (email != null) {
                db.collection("users").document(email).get().addOnSuccessListener {
                    personaje = Gson().fromJson(it.get("personaje").toString(), Personaje::class.java)
                }

                db.collection("users").document(email).update(
                    "personaje", Gson().toJson(personaje)
                )

                personaje.getMochila().addArticulo(Articulo("A1"))
                Toast.makeText(this, personaje.getMochila().getPesoMochila().toString(), Toast.LENGTH_LONG).show()
            }

            if (isJuego)
                irJuego()
            else
                Intent (this, MainActivity::class.java).apply {
                    startActivity(this)
                }
        }

        binding.button2.setOnClickListener {
            if (isJuego)
                irJuego()
            else
                Intent (this, MainActivity::class.java).apply {
                    startActivity(this)
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