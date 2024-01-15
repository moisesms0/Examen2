package com.example.examenmoisesmirandasantana

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.example.examenmoisesmirandasantana.Model.Tarea
import com.example.examenmoisesmirandasantana.fragments.DetallesFragment
import com.example.examenmoisesmirandasantana.fragments.TareasFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Boton e intent para ver tareas completadas
        val btnTareasCompletadas = findViewById<View>(R.id.btnTareasCompletadas)

        btnTareasCompletadas.setOnClickListener {
            val intent = Intent(this, CompletadasActivity::class.java)
            startActivity(intent)
        }


        // Boton e intent para añadir nueva tarea
        val btnAnadirTarea = findViewById<View>(R.id.btnAñadirTarea)

        btnAnadirTarea.setOnClickListener {
            val intent = Intent(this, FormularioActivity::class.java)
            startActivity(intent)
        }



        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        toolbar.inflateMenu(R.menu.menu)
        setSupportActionBar(toolbar)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.cambiarFuente -> {
                cambiarFuenteAlert()
                }
        }
        return super.onOptionsItemSelected(item)
    }


    fun cambiarFuenteAlert(){
        val dialogView = layoutInflater.inflate(R.layout.alert_et, null)
        val numero = dialogView.findViewById<EditText>(R.id.tamañoFuente)

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Cambiar fuente")
        builder.setMessage("¿Desea cambiar la fuente de la aplicación?")
          builder.setView(dialogView)
        builder.setPositiveButton("Aceptar") { dialog, which ->
            val sharedPrefs = getSharedPreferences("config", MODE_PRIVATE)
            val editor = sharedPrefs.edit()
            editor.putInt("fuente", numero.text.toString().toInt())
            editor.apply()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerTareas, TareasFragment())
            transaction.commit()
        }
        builder.setNegativeButton("Cancelar") { dialog, which ->
            // Do something when user press the negative button
        }
        builder.show()
    }



}