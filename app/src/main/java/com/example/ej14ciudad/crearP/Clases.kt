package com.example.ej14ciudad.crearP

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ej14ciudad.R
import com.example.ej14ciudad.databinding.ActivityClasesBinding

class Clases : AppCompatActivity() {
    private lateinit var binding : ActivityClasesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClasesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var clase = ""
        var imagenC = 0

        binding.button.setOnClickListener {
            binding.imageView.setImageResource(R.drawable.guerrero)
            clase = "Guerrero"
            imagenC = R.drawable.guerrero
        }

        binding.button2.setOnClickListener {
            binding.imageView.setImageResource(R.drawable.mago)
            clase = "Mago"
            imagenC = R.drawable.mago
        }

        binding.button3.setOnClickListener {
            binding.imageView.setImageResource(R.drawable.berserker)
            clase = "Berserker"
            imagenC = R.drawable.berserker
        }

        binding.button4.setOnClickListener {
            binding.imageView.setImageResource(R.drawable.ladron)
            clase = "Ladron"
            imagenC = R.drawable.ladron
        }

        binding.button5.setOnClickListener {
            Intent(this, Razas::class.java).also {
                it.putExtra("clase", clase)
                it.putExtra("imagenC", imagenC)
                startActivity(it)
            }
        }
    }
}