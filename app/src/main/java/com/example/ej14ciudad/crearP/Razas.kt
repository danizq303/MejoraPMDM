package com.example.ej14ciudad.crearP

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ej14ciudad.R
import com.example.ej14ciudad.databinding.ActivityRazasBinding

class Razas : AppCompatActivity() {
    private lateinit var binding : ActivityRazasBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRazasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var raza = ""
        var imagenR = 0

        binding.button.setOnClickListener {
            binding.imageView.setImageResource(R.drawable.elfo)
            raza = "Elfo"
            imagenR = R.drawable.elfo
        }

        binding.button2.setOnClickListener {
            binding.imageView.setImageResource(R.drawable.humano)
            raza = "Humano"
            imagenR = R.drawable.humano
        }

        binding.button3.setOnClickListener {
            binding.imageView.setImageResource(R.drawable.enano)
            raza = "Enano"
            imagenR = R.drawable.enano
        }

        binding.button4.setOnClickListener {
            binding.imageView.setImageResource(R.drawable.goblin)
            raza = "Goblin"
            imagenR = R.drawable.goblin
        }

        binding.button5.setOnClickListener {
            val clase = intent.getStringExtra("clase")
            val imagenC = intent.getIntExtra("imagenC", 0)
            Intent(this, Personaje::class.java).also {
                it.putExtra("raza", raza)
                it.putExtra("imagenR", imagenR)
                it.putExtra("clase", clase)
                it.putExtra("imagenC", imagenC)
                startActivity(it)
            }
        }
    }
}