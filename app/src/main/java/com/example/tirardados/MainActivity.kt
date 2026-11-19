package com.example.tirardados


import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tirardados.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private var suma: Int = 0
    private var numeroUsuario: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.main)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }

        initComponent()
        initEvent()
    }

    private fun initComponent() {
        binding.txtResultado.visibility = View.INVISIBLE
    }

    private fun initEvent() {
        binding.imgCubilete.setOnClickListener {
            val textoNumero = binding.etNumero.text.toString()

            if (textoNumero.isEmpty()) {
                showMSG("Introduce un número entre 3 y 18")
                return@setOnClickListener
            }

            val num = textoNumero.toInt()
            if (num < 3 || num > 18) {
                showMSG("El número debe estar entre 3 y 18")
                return@setOnClickListener
            }

            numeroUsuario = num

            binding.imgCubilete.isEnabled = false
            binding.imgCubilete.alpha = 0.5f
            binding.etNumero.isEnabled = false
            binding.txtResultado.visibility = View.INVISIBLE

            CoroutineScope(Dispatchers.Main).launch {
                for (i in 1..4) {
                    esperarTiempo(1000)
                    mostrarDados()
                }

                esperarTiempo(1000)
                mostrarResultado()
            }
        }
    }

    private suspend fun esperarTiempo(milisegundos: Long) {
        withContext(Dispatchers.IO) {
            delay(milisegundos)
        }
    }

    private fun mostrarDados() {
        val valor1 = (1..6).random()
        val valor2 = (1..6).random()
        val valor3 = (1..6).random()

        asignarImagenDado(binding.imgDado1, valor1)
        asignarImagenDado(binding.imgDado2, valor2)
        asignarImagenDado(binding.imgDado3, valor3)

        suma = valor1 + valor2 + valor3
    }

    private fun asignarImagenDado(imgDado: ImageView, valor: Int) {
        when (valor) {
            1 -> imgDado.setImageResource(R.drawable.dado1)
            2 -> imgDado.setImageResource(R.drawable.dado2)
            3 -> imgDado.setImageResource(R.drawable.dado3)
            4 -> imgDado.setImageResource(R.drawable.dado4)
            5 -> imgDado.setImageResource(R.drawable.dado5)
            6 -> imgDado.setImageResource(R.drawable.dado6)
        }
    }

    private fun mostrarResultado() {
        binding.txtResultado.text = suma.toString()
        binding.txtResultado.visibility = View.VISIBLE

        binding.imgCubilete.isEnabled = true
        binding.imgCubilete.alpha = 1.0f
        binding.etNumero.isEnabled = true

        if (suma == numeroUsuario) {
            mostrarDialogoAcierto()
        } else {
            showMSG("No ha acertado. Salió $suma y elegiste $numeroUsuario")
        }
    }

    private fun mostrarDialogoAcierto() {
        AlertDialog.Builder(this)
            .setTitle("¡Acertado!")
            .setMessage("¡Los dados han sumado $suma! El programa ha adivinado tu número.")
            .setPositiveButton("Aceptar") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showMSG(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}