package com.example.examenmoisesmirandasantana

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.examenmoisesmirandasantana.Model.DatabaseHelper
import com.example.examenmoisesmirandasantana.Model.Tarea
import com.example.examenmoisesmirandasantana.fragments.DetallesFragment

class CompletadasActivity : AppCompatActivity() {

    private lateinit var listView: ListView

    private lateinit var dbHandler: DatabaseHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_completadas)


        val btnVolver = findViewById<View>(R.id.btnVolver)

        btnVolver.setOnClickListener {
            finish()
        }

        dbHandler = DatabaseHelper(this)
        listView = findViewById(R.id.lvTareasCompletadas)

        viewTareas()


    }



    private fun viewTareas(){
        val tareasList = dbHandler.getTareas(true)
        val adapter = TareasAdapter(this, R.layout.tarea_item, tareasList)

        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val tarea = tareasList[position]
            val intent = Intent(this, FormularioActivity::class.java)
            intent.putExtra("id", tarea.id)
            intent.putExtra("modificar", false)
            startActivity(intent)
        }
    }


    override fun onResume() {
        super.onResume()
        viewTareas()
    }


}