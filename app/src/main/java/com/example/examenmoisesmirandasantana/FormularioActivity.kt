package com.example.examenmoisesmirandasantana

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.example.examenmoisesmirandasantana.Model.DatabaseHelper
import com.example.examenmoisesmirandasantana.Model.Tarea
import java.util.Calendar

class FormularioActivity : AppCompatActivity() {

    var idTarea = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario)

        val btnVolver = findViewById<View>(R.id.btnVolverFormulario)
        val btnLimpiar = findViewById<View>(R.id.btnLimpiar)
        val btnGuardar = findViewById<View>(R.id.btnGuardar)
        val btnFecha = findViewById<View>(R.id.btnFecha)
        val btnHora = findViewById<View>(R.id.btnHora)


        if (intent.hasExtra("modificar")){
            val modificar = intent.getBooleanExtra("modificar", false)
            btnLimpiar.isEnabled = false
            if (!modificar){
               deshabilitarCampos()
            }else{
                val checkbox = findViewById<CheckBox>(R.id.checkBox)
                checkbox.isEnabled = true
            }
        }


        if (intent.hasExtra("id")){
            val id = intent.getIntExtra("id", 0)
            val dbHandler = DatabaseHelper(this)
            val tarea = dbHandler.getTarea(id)
            idTarea = tarea.id
            val etNombre = findViewById<EditText>(R.id.etNombre)
            val etFecha = findViewById<EditText>(R.id.etFecha)
            val etHora = findViewById<EditText>(R.id.etHora)
            val checkBox = findViewById<CheckBox>(R.id.checkBox)
            checkBox.isChecked = tarea.completada
            etNombre.setText(tarea.nombre)
            val fechaYHora = tarea.fecha.split(" ")
            etFecha.setText(fechaYHora[0])
            etHora.setText(fechaYHora[1])
        }


        btnVolver.setOnClickListener {
            finish()
        }

        btnLimpiar.setOnClickListener {
            limpiarCampos()
        }

        btnFecha.setOnClickListener {
            abrirDatePickerDialog()
        }

        btnHora.setOnClickListener {
            abrirTimePickerDialog()
        }



        btnGuardar.setOnClickListener {
            if (idTarea > 0){
                actalizarTarea()
            }else{
                crearTarea()
            }
        }

    }

    private fun limpiarCampos(){
        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etFecha = findViewById<EditText>(R.id.etFecha)
        etNombre.setText("")
        etFecha.setText("")

    }

    private fun crearTarea(){
        val nombre = findViewById<EditText>(R.id.etNombre).text
        val fecha = findViewById<EditText>(R.id.etFecha).text
        val hora = findViewById<EditText>(R.id.etHora).text

        if (nombre.isNotEmpty() && fecha.isNotEmpty()){
            var fechaYHora = "$fecha $hora"
            val tarea = Tarea(1, nombre.toString(), fechaYHora, false)
            val dbHandler = DatabaseHelper(this)
            val status = dbHandler.addTarea(tarea)
            if (status > -1){
                Toast.makeText(this, "Tarea añadida correctamente", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this, "Error al añadir la tarea", Toast.LENGTH_LONG).show()
            }
        }else{
            Toast.makeText(this, "Nombre y fecha son requeridos", Toast.LENGTH_LONG).show()
        }
    }


    private fun actalizarTarea(){
        val nombre = findViewById<EditText>(R.id.etNombre).text
        val fecha = findViewById<EditText>(R.id.etFecha).text
        val hora = findViewById<EditText>(R.id.etHora).text

        if (nombre.isNotEmpty() && fecha.isNotEmpty()){
            var fechaYHora = "$fecha $hora"
            val tarea = Tarea(idTarea, nombre.toString(), fechaYHora, false)
            val dbHandler = DatabaseHelper(this)
            val status = dbHandler.updateTarea(tarea)
            if (status > -1){
                Toast.makeText(this, "Tarea actualizada correctamente", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this, "Error al actualizar la tarea", Toast.LENGTH_LONG).show()
            }
        }else{
            Toast.makeText(this, "Nombre y fecha son requeridos", Toast.LENGTH_LONG).show()
        }

    }

    private fun abrirDatePickerDialog() {
        // Obtengo una instancia del calendario y fijo el año, mes y día actuales
        val calendario = Calendar.getInstance()
        val año = calendario.get(Calendar.YEAR)
        val mes = calendario.get(Calendar.MONTH)
        val dia = calendario.get(Calendar.DAY_OF_MONTH)

        // Creo un datePickerDialog y le paso el contexto, el listener y el año, mes y día
        // seleccionados
        val datePickerDialog = DatePickerDialog(this,
            DatePickerDialog.OnDateSetListener { _, añoSeleccionado, mesSeleccionado, diaSeleccionado ->
                // Este código se ejecuta cuando el usuario selecciona una fecha
                // Actualizo el texto del botón con la fecha seleccionada
                val etFecha = findViewById<EditText>(R.id.etFecha)
                etFecha.setText("$diaSeleccionado/${mesSeleccionado + 1}/$añoSeleccionado")
            }, año, mes, dia)

        datePickerDialog.show()
    }

    private fun abrirTimePickerDialog() {
        // Obtengo una instancia del calendario y fijo la hora y minutos actuales
        val calendario = Calendar.getInstance()
        val hora = calendario.get(Calendar.HOUR_OF_DAY)
        val minutos = calendario.get(Calendar.MINUTE)

        // Creo un timePickerDialog y le paso el contexto, la hora seleccionada y el minuto
        // y establezco una alarma
        val timePickerDialog = TimePickerDialog(this,
            TimePickerDialog.OnTimeSetListener { _, horaSeleccionada, minutoSeleccionado ->
                val etHora = findViewById<EditText>(R.id.etHora)
                etHora.setText("$horaSeleccionada:$minutoSeleccionado")

            }, hora, minutos, true)

        timePickerDialog.show()
    }

    private fun deshabilitarCampos(){
        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etFecha = findViewById<EditText>(R.id.etFecha)
        val checkbox = findViewById<CheckBox>(R.id.checkBox)

        checkbox.isEnabled = true
        etNombre.isEnabled = false
        etFecha.isEnabled = false
    }






}