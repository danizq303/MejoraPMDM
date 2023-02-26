package com.example.ej14ciudad.juego

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ej14ciudad.R
import com.example.ej14ciudad.databinding.ActivityMercaderBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson


class Mercader : AppCompatActivity() {
    private lateinit var binding: ActivityMercaderBinding
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMercaderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imageView.setImageResource(R.drawable.mercader)

        val prefs = getSharedPreferences("PREFERENCE_FILE_KEY", MODE_PRIVATE)
        val email = prefs.getString("email", null)
        val isJuego = prefs.getBoolean("isJuego", false)

        var personaje = Personaje()

        db.collection("users").document(email!!).get().addOnSuccessListener {
            personaje = Gson().fromJson(it.get("personaje").toString(), Personaje::class.java)

        }

        binding.button.setOnClickListener {
            binding.imageView.setImageResource(R.drawable.objeto)
            binding.button.visibility = View.INVISIBLE
            binding.button2.visibility = View.INVISIBLE
            binding.button3.visibility = View.VISIBLE
            binding.button4.visibility = View.VISIBLE
            binding.button5.visibility = View.VISIBLE
            binding.editTextNumber.visibility = View.VISIBLE
            binding.textView2.visibility = View.VISIBLE
        }

        binding.button2.setOnClickListener {
            if (isJuego)
                irJuego()
            else
                Intent (this, MainActivity::class.java).apply {
                    startActivity(this)
                }
        }

        binding.button3.setOnClickListener {
            binding.imageView.setImageResource(R.drawable.mercader)
            binding.button.visibility = View.VISIBLE
            binding.button2.visibility = View.VISIBLE
            binding.button3.visibility = View.INVISIBLE
            binding.button4.visibility = View.INVISIBLE
            binding.button5.visibility = View.INVISIBLE
            binding.editTextNumber.visibility = View.INVISIBLE
            binding.textView2.visibility = View.INVISIBLE
        }

        binding.button4.setOnClickListener {
            db.collection("users").document(email).get().addOnSuccessListener {
                personaje = Gson().fromJson(it.get("personaje").toString(), Personaje::class.java)
            }

            val numeroObjetos = personaje.getMochila().getPesoMochila() / 5
            if (numeroObjetos < binding.editTextNumber.text.toString().toInt()) {
                Toast.makeText(this, "No tienes suficientes objetos", Toast.LENGTH_LONG).show()
            } else {
                for (i in 0..binding.editTextNumber.text.toString().toInt()) {
                    personaje.vender2(Articulo("MObjeto", 125))
                }
                Toast.makeText(this, "Venta Realizada", Toast.LENGTH_LONG).show()
            }

            db.collection("users").document(email).update(
                "personaje", Gson().toJson(personaje)
            )

            if (isJuego)
                irJuego()
            else
                Intent (this, MainActivity::class.java).apply {
                    startActivity(this)
                }
        }

        binding.button5.setOnClickListener {
            db.collection("users").document(email).get().addOnSuccessListener {
                personaje = Gson().fromJson(it.get("personaje").toString(), Personaje::class.java)
            }

            val numeroObjetos = personaje.getMochila().getPesoMochila() / 5
            if (numeroObjetos < binding.editTextNumber.text.toString().toInt()) {
                Toast.makeText(this, "No tienes suficiente dinero", Toast.LENGTH_LONG).show()
            } else {
                for (i in 0..binding.editTextNumber.text.toString().toInt()) {
                    personaje.comprar(Articulo("MObjeto", 125))
                }
                Toast.makeText(this, "Compra Realizada", Toast.LENGTH_LONG).show()
            }

            db.collection("users").document(email).update(
                "personaje", Gson().toJson(personaje)
            )

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