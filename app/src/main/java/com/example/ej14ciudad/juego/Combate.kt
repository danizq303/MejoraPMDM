package com.example.ej14ciudad.juego

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ej14ciudad.R
import com.example.ej14ciudad.databinding.ActivityCombateBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson

class Combate : AppCompatActivity() {
    private lateinit var binding: ActivityCombateBinding
    private var db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCombateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imageView2.setBackgroundResource(R.drawable.zombie)

        val prefs = getSharedPreferences("PREFERENCE_FILE_KEY", MODE_PRIVATE)
        val email = prefs.getString("email", null)
        val isJuego = prefs.getBoolean("isJuego", false)

        val bool = (0..1).random() == 1
        var personaje = Personaje()
        db.collection("users").document(email!!).get().addOnSuccessListener {
            personaje = Gson().fromJson(it.get("personaje").toString(), Personaje::class.java)
        }

        val vidaPOriginal = personaje.vida
        val vidaPersonaje = personaje.vida
        var vida = if (bool) {
            200
        } else {
            100
        }
        val vidaEnemigo = vida

        val ataque = if (bool) {
            30 / personaje.defensa
        } else {
            20 / personaje.defensa
        }

        binding.textView3.text = binding.textView3.text.toString() + vida.toString()
        binding.textView4.text = binding.textView4.text.toString() + personaje.vida.toString()

        binding.button6.setOnClickListener {
            db = FirebaseFirestore.getInstance()
            db.collection("users").document(email).get().addOnSuccessListener {
                personaje = Gson().fromJson(it.get("personaje").toString(), Personaje::class.java)
            }

            val random = (1..6).random()
            if (random == 4 || random == 5 || random == 6) {
                vida -= personaje.fuerza
                changeHealthImageEnemigo(vida, vidaEnemigo)

                if (vida <= 0) {
                    Toast.makeText(this, "Has ganado", Toast.LENGTH_LONG).show()

                    repeat(3) {
                        personaje.getMochila().addArticulo(Articulo("Cura"))
                    }
                    personaje.monedero[100] = personaje.monedero[100]!! + 1
                    if (!isJuego)
                        personaje.vida = vidaPOriginal

                    //Save personaje
                    db.collection("users").document(email).update(
                        "personaje", Gson().toJson(personaje)
                    )

                    if (isJuego) {
                        personaje.battallasGanadas++
                        irJuego(personaje)
                    } else {
                        Intent(this, MainActivity::class.java).apply {
                            startActivity(this)
                        }
                    }

                } else {
                    binding.textView3.text = "Vida Enemigo: $vida"
                    //Toast.makeText(this, "Has atacado", Toast.LENGTH_SHORT).show()
                }
            } else {
                //Toast.makeText(this, "No puedes atacar", Toast.LENGTH_SHORT).show()
            }

            turnoEnemigo(personaje, ataque, vidaPersonaje, email)
        }

        binding.button7.setOnClickListener {
            //Huir
            val random = (1..6).random()
            db.collection("users").document(email).update(
                "personaje", Gson().toJson(personaje)
            )
            if (random == 5 || random == 6) {
                Toast.makeText(this, "Has huido", Toast.LENGTH_LONG).show()
                Intent(this, MainActivity::class.java).apply {
                    startActivity(this)
                }
            } else {
                Toast.makeText(this, "No puedes huir", Toast.LENGTH_SHORT).show()
                turnoEnemigo(personaje, ataque, vidaPersonaje, email)
            }
        }

        binding.button8.setOnClickListener {
            //Consumir objeto.vida y mostrar Toast de que se ha consumido
            if (vidaPersonaje != personaje.vida && personaje.getMochila()
                    .getContenido()[0].getVida() + personaje.vida > vidaPersonaje
            ) {
                personaje.vida = vidaPersonaje
                binding.textView4.text = "Vida Jugador: ${personaje.vida}"
                changeHealthImage(personaje.vida, vidaPersonaje)

                personaje.getMochila().getContenido().removeAt(0)
            } else {
                if (personaje.getMochila()
                        .getContenido()[0].getVida() + personaje.vida > vidaPersonaje
                ) {
                    Toast.makeText(this, "No puedes curarte mas", Toast.LENGTH_SHORT).show()
                } else {
                    if (personaje.getMochila().getContenido().size > 0) {
                        personaje.vida += personaje.getMochila().getContenido()[0].getVida()
                        binding.textView4.text = "Vida Jugador: ${personaje.vida}"
                        changeHealthImage(personaje.vida, vidaPersonaje)

                        personaje.getMochila().getContenido().removeAt(0)
                        Toast.makeText(this, "Has consumido un objeto", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "No tienes mas objetos", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            db.collection("users").document(email).update(
                "personaje", Gson().toJson(personaje)
            )
        }
    }

    private fun turnoEnemigo(personaje: Personaje, ataque: Int, vidaPersonaje: Int, email: String) {
        personaje.vida -= ataque
        changeHealthImage(personaje.vida, vidaPersonaje)
        if (personaje.vida > 0) {
            binding.textView4.text = "Vida Jugador: ${personaje.vida}"
            db.collection("users").document(email).update(
                "personaje", Gson().toJson(personaje)
            )
        } else {
            Toast.makeText(this, "Has muerto", Toast.LENGTH_LONG).show()
            db.collection("users").document(email).update(
                "personaje", Gson().toJson(personaje)
            )
            Intent(this, Black::class.java).apply {
                startActivity(this)
            }
        }
    }

    private fun changeHealthImage(vida: Int, vidaTotal: Int) {
        val r = vidaTotal / 3
        when (vida) {
            in 1..r -> {
                binding.imageView5.setImageResource(R.drawable.bar2)
            }
            in r..r * 2 -> {
                binding.imageView5.setImageResource(R.drawable.bar3)
            }
            in r * 2..vidaTotal -> {
                binding.imageView5.setImageResource(R.drawable.bar4)
            }
            else -> {
                binding.imageView5.setImageResource(R.drawable.bar1)
            }
        }
    }

    private fun changeHealthImageEnemigo(vida: Int, vidaTotal: Int) {
        val r = vidaTotal / 3
        when (vida) {
            in 1..r -> {
                binding.imageView4.setImageResource(R.drawable.bar2)
            }
            in 33..r * 2 -> {
                binding.imageView4.setImageResource(R.drawable.bar3)
            }
            in 66..vidaTotal -> {
                binding.imageView4.setImageResource(R.drawable.bar4)
            }
            else -> {
                binding.imageView4.setImageResource(R.drawable.bar1)
            }
        }
    }

    private fun irJuego(personaje: Personaje) {
        val prefs = getSharedPreferences("PREFERENCE_FILE_KEY", MODE_PRIVATE)
        val email = prefs.getString("email", null)

        checkEndOfGame(personaje, email)

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

    private fun checkEndOfGame(personaje: Personaje, email: String?) {
        if (personaje.battallasGanadas == 3) {
            personaje.battallasGanadas = 0
            personaje.vida = 200
            db.collection("users").document(email!!).update(
                "personaje", Gson().toJson(personaje)
            )
            val pref = getSharedPreferences("PREFERENCE_FILE_KEY", MODE_PRIVATE).edit()
            pref.putBoolean("isJuego", false)
            pref.apply()

            Toast.makeText(this, "Has ganado el juego", Toast.LENGTH_LONG).show()

            Intent(this, MainActivity::class.java).apply {
                startActivity(this)
            }
        } else {
            Toast.makeText(
                this,
                "Numero de victorias: ${personaje.battallasGanadas}",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}