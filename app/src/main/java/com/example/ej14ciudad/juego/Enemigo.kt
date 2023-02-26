package com.example.ej14ciudad.juego

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ej14ciudad.R
import com.example.ej14ciudad.databinding.ActivityEnemigoBinding

class Enemigo : AppCompatActivity() {
    private lateinit var binding: ActivityEnemigoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnemigoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imageView.setBackgroundResource(R.drawable.enemigo)

        binding.button.setOnClickListener {
            Intent (this, Combate::class.java).apply {
                //putExtra("email", email)
                startActivity(this)
            }
        }

        binding.button2.setOnClickListener {
            Intent (this, MainActivity::class.java).apply {
                //putExtra("email", email)
                startActivity(this)
            }
        }
    }
}