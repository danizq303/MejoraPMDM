package com.example.ej14ciudad

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.ej14ciudad.crearP.Clases
import com.example.ej14ciudad.databinding.ActivityAuthBinding
import com.example.ej14ciudad.juego.MainActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setup()
        session()
    }

    override fun onStart() {
        super.onStart()
        binding.authLayout.visibility = View.VISIBLE
    }

    private fun session() {
        val prefs = getSharedPreferences("PREFERENCE_FILE_KEY", MODE_PRIVATE)
        val email = prefs.getString("email", null)

        if (email != null) {
            binding.authLayout.visibility = View.INVISIBLE
            Intent(this, MainActivity::class.java).apply {
                Toast.makeText(this@AuthActivity, "Bienvenido $email", Toast.LENGTH_LONG).show()
                startActivity(this)
            }
        }
    }

    private fun setup() {
        title = "Autenticación"
        binding.signUpButton.setOnClickListener {
            if (binding.editTextTextEmailAddress.text.isNotEmpty() && binding.editTextTextPassword2.text.isNotEmpty()) {
                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(binding.editTextTextEmailAddress.text.toString()
                        , binding.editTextTextPassword2.text.toString()).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "El usuario se ha creado correctamente", Toast.LENGTH_LONG).show()
                        saveMail()
                        Intent(this, Clases::class.java).apply {
                            startActivity(this)
                        }
                    } else {
                        showAlert()
                    }
                }
            }
        }

        binding.signInButton.setOnClickListener {
            if (binding.editTextTextEmailAddress.text.isNotEmpty() && binding.editTextTextPassword2.text.isNotEmpty()) {
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(binding.editTextTextEmailAddress.text.toString()
                        , binding.editTextTextPassword2.text.toString()).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Inicio de sesion correcto", Toast.LENGTH_LONG).show()
                        saveMail()
                        Intent(this, MainActivity::class.java).apply {
                            startActivity(this)
                        }
                    } else {
                        showAlert()
                    }
                }
            }
        }
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando al usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun saveMail() {
        val prefs = getSharedPreferences("PREFERENCE_FILE_KEY", MODE_PRIVATE).edit()
        prefs.putString("email", binding.editTextTextEmailAddress.text.toString())
        prefs.apply()
    }

}