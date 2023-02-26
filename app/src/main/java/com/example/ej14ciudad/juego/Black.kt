package com.example.ej14ciudad.juego

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ej14ciudad.R
import com.example.ej14ciudad.crearP.Clases
import com.example.ej14ciudad.databinding.ActivityBlackBinding

class Black : AppCompatActivity() {
    private lateinit var binding: ActivityBlackBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBlackBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button9.setOnClickListener {
            Intent(this, Clases::class.java).apply {
                startActivity(this)
            }
        }
    }
}