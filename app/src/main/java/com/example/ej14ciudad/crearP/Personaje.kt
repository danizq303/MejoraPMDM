package com.example.ej14ciudad.crearP

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.example.ej14ciudad.databinding.ActivityPersonajeBinding
import com.example.ej14ciudad.juego.MainActivity
import com.example.ej14ciudad.juego.Personaje
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import kotlin.random.Random

class Personaje : AppCompatActivity() {
    private lateinit var binding: ActivityPersonajeBinding
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonajeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val prefs = getSharedPreferences("PREFERENCE_FILE_KEY", MODE_PRIVATE)
        val email = prefs.getString("email", null)

        val raza = intent.getStringExtra("raza")
        val imagenR = intent.getIntExtra("imagenR", 0)
        val clase = intent.getStringExtra("clase")
        val imagenC = intent.getIntExtra("imagenC", 0)

        binding.textView2.text = "Raza: $raza"
        binding.imageView3.setImageResource(imagenR)
        binding.textView.text = "Clase: $clase"
        binding.imageView2.setImageResource(imagenC)
        binding.textView3.text = "Fuerza: ${Random.nextInt(10, 15)}\nDefensa: ${
            Random.nextInt(
                1,
                5
            )
        }\nTamaño de la mochila: 100\nVida: 200\nMonedero: Vacio"

        binding.editTextTextPersonName.addTextChangedListener {
            if (binding.editTextTextPersonName.text.isNotEmpty()) {
                binding.button7.isEnabled = true
            }
        }

        binding.button6.setOnClickListener {
            Intent(this, Clases::class.java).also {
                startActivity(it)
            }
        }

        binding.button7.setOnClickListener {
            if (email != null) {
                db.collection("users").document(email).set(
                    hashMapOf(
                        "personaje" to Gson().toJson(
                            Personaje(
                                binding.editTextTextPersonName.text.toString(),
                                100,
                                "Joven",
                                raza!!,
                                clase!!
                            )
                        )
                    )
                )
            }
            Toast.makeText(this, "Personaje creado", Toast.LENGTH_SHORT).show()

            Intent(this, MainActivity::class.java).also {
                startActivity(it)
            }
        }
    }
}