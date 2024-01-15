package com.example.examenmoisesmirandasantana

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.examenmoisesmirandasantana.Model.Tarea
import com.example.examenmoisesmirandasantana.fragments.TareasFragment

// Define la clase DiscosAdapter, que extiende RecyclerView.Adapter y especifica MyViewHolder como su ViewHolder.
class TareasAdapter(context: Context, resource: Int, private val tareas: List<Tarea>) :
    ArrayAdapter<Tarea>(context, resource, tareas) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, convertView, parent)
    }

    private fun createItemView(position: Int, convertView: View?, parent: ViewGroup): View {
        val itemView = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.tarea_item, parent, false)

        val tarea = getItem(position)
        val nombreTextView: TextView = itemView.findViewById(R.id.tvNombreTarea)
        val fechaTextView: TextView = itemView.findViewById(R.id.tvFechaTarea)

        nombreTextView.text = tarea?.nombre
        fechaTextView.text = tarea?.fecha

        //cambiar tamaño de fuente con las sharedPreferences
        val sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE)
        val fontSize = sharedPreferences.getInt("fuente", 12)
        nombreTextView.textSize = fontSize.toFloat()
        fechaTextView.textSize = fontSize.toFloat()


        return itemView
    }

}

