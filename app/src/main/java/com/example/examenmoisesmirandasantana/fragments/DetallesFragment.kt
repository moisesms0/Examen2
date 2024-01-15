package com.example.examenmoisesmirandasantana.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.examenmoisesmirandasantana.Model.DatabaseHelper
import com.example.examenmoisesmirandasantana.Model.Tarea
import com.example.examenmoisesmirandasantana.R

class DetallesFragment(var tarea: Tarea) : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        return inflater.inflate(R.layout.fragment_destalles, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val tvNombre = view?.findViewById<TextView>(R.id.tvNombre)
        val tvFecha = view?.findViewById<TextView>(R.id.tvFecha)
        val tvCompletado = view?.findViewById<TextView>(R.id.tvCompletado)


        tvNombre?.text = tarea.nombre
        tvFecha?.text = tarea.fecha
        if (tarea.completada){
            tvCompletado?.text = "Completado"
        }else{
            tvCompletado?.text = "No completado"
        }


    }

}