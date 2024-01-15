package com.example.examenmoisesmirandasantana.fragments

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.ContextMenu.ContextMenuInfo
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.examenmoisesmirandasantana.CompletadasActivity
import com.example.examenmoisesmirandasantana.FormularioActivity
import com.example.examenmoisesmirandasantana.Model.DatabaseHelper
import com.example.examenmoisesmirandasantana.Model.Tarea
import com.example.examenmoisesmirandasantana.R
import com.example.examenmoisesmirandasantana.TareasAdapter


class TareasFragment : Fragment() {

    private lateinit var listView: ListView
    private lateinit var dbHandler: DatabaseHelper
    var menuinfo: ContextMenuInfo? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tareas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        listView = view.findViewById(R.id.lvTareas)
        dbHandler = DatabaseHelper(requireContext())


        super.onViewCreated(view, savedInstanceState)

        viewTareas()



    }

    private fun viewTareas(){
        val tareasList = dbHandler.getTareas(false)
        val adapter = TareasAdapter(requireContext(), R.layout.tarea_item, tareasList)

        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val tarea = tareasList[position]
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerDetalles, DetallesFragment(tarea))
            transaction.commit()



        }

        registerForContextMenu(listView)

    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = requireActivity().menuInflater
        menuinfo = menuInfo

        inflater.inflate(R.menu.context_menu, menu)
    }


    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.title) {
            "Completar" -> {
                val info = menuinfo as AdapterView.AdapterContextMenuInfo
                val tarea = listView.getItemAtPosition(info.position) as Tarea
                tarea.completada = true
                dbHandler.updateTarea(tarea)
                viewTareas()
            }

            "Modificar" -> {

                val info = menuinfo as AdapterView.AdapterContextMenuInfo
                val tarea = listView.getItemAtPosition(info.position) as Tarea
                val intent = Intent(requireContext(), FormularioActivity::class.java)
                intent.putExtra("id", tarea.id)
                intent.putExtra("modificar", true)
                startActivity(intent)
            }

            "Eliminar" -> {
                val info = menuinfo as AdapterView.AdapterContextMenuInfo
                val tarea = listView.getItemAtPosition(info.position) as Tarea
                dbHandler.deleteTarea(tarea)
                viewTareas()
            }

            "Enviar" -> {

            }

        }
        return super.onContextItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        viewTareas()
    }


}