package com.example.examenmoisesmirandasantana.Model

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

// Clase DatabaseHelper que extiende SQLiteOpenHelper para manejar la base de datos de la aplicación.
class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    // Bloque companion object para definir constantes que serán usadas en toda la clase.
    // Son como los valores estáticos en Java
    companion object {
        // Nombre de la base de datos.
        private const val DATABASE_NAME = "TareasDatabase"
        // Versión de la base de datos, útil para manejar actualizaciones esquemáticas.
        private const val DATABASE_VERSION = 3
        // Nombre de la tabla donde se almacenarán los discos.
        private const val TABLE_TAREAS = "tareas"
        // Nombres de las columnas de la tabla.
        private const val KEY_ID = "id"
        private const val KEY_NOMBRE = "nombre"
        private const val KEY_FECHA = "fecha"
        private const val KEY_COMPLETADA = "completada"
    }

    // Método llamado cuando la base de datos se crea por primera vez.
    override fun onCreate(db: SQLiteDatabase) {
        // Define la sentencia SQL para crear la tabla de discos.
        val createTareasTable = ("CREATE TABLE " + TABLE_TAREAS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NOMBRE + " TEXT,"
                + KEY_FECHA + " INTEGER," + KEY_COMPLETADA + " INTEGER" + ")")
        db.execSQL(createTareasTable)
    }

    // Método llamado cuando se necesita actualizar la base de datos, por ejemplo, cuando se incrementa DATABASE_VERSION.
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Elimina la tabla existente y crea una nueva.
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TAREAS")
        onCreate(db)
    }

    fun getTareas(completada : Boolean): ArrayList<Tarea> {
        val discosList = ArrayList<Tarea>()

        val selectQuery = "SELECT  * FROM $TABLE_TAREAS WHERE $KEY_COMPLETADA = ${if (completada) 1 else 0}"

        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var nombre: String
        var fecha: String
        var completada: Boolean

        if (cursor.moveToFirst()) {
            do {
                // Obtiene los índices de las columnas.
                val idIndex = cursor.getColumnIndex(KEY_ID)
                val nombreIndex = cursor.getColumnIndex(KEY_NOMBRE)
                val fechaIndex = cursor.getColumnIndex(KEY_FECHA)
                val completadaIndex = cursor.getColumnIndex(KEY_COMPLETADA)

                // Verifica que los índices sean válidos.
                if (idIndex != -1 && nombreIndex != -1 && fechaIndex != -1) {
                    // Lee los valores y los añade a la lista de discos.
                    id = cursor.getInt(idIndex)
                    nombre = cursor.getString(nombreIndex)
                    fecha = cursor.getString(fechaIndex)
                    completada = cursor.getInt(completadaIndex) == 1

                    val tarea = Tarea(id = id, nombre = nombre, fecha = fecha, completada = completada)
                    discosList.add(tarea)
                }
            } while (cursor.moveToNext())
        }

        // Cierra el cursor para liberar recursos.
        cursor.close()
        return discosList
    }

    fun getTarea(id : Int): Tarea {
        val db = this.readableDatabase
        val selectQuery = "SELECT  * FROM $TABLE_TAREAS WHERE $KEY_ID = $id"
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
        }

        val nombre: String
        val fecha: String
        val completada: Boolean

        if (cursor != null) {
            cursor.moveToFirst()
            // Obtiene los índices de las columnas.
            val nombreIndex = cursor.getColumnIndex(KEY_NOMBRE)
            val fechaIndex = cursor.getColumnIndex(KEY_FECHA)
            val completadaIndex = cursor.getColumnIndex(KEY_COMPLETADA)

            // Verifica que los índices sean válidos.
            if (nombreIndex != -1 && fechaIndex != -1) {
                // Lee los valores y los añade a la lista de discos.
                nombre = cursor.getString(nombreIndex)
                fecha = cursor.getString(fechaIndex)
                val completadoNumero = cursor.getInt(completadaIndex)
                completada = cursor.getInt(completadaIndex) == 1

                val tarea = Tarea(id = id, nombre = nombre, fecha = fecha, completada = completada)
                return tarea
            }
        }

        // Cierra el cursor para liberar recursos.
        cursor?.close()
        return Tarea()
    }


    // Método para actualizar un disco en la base de datos.
    fun updateTarea(tarea: Tarea): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        // Prepara los valores a actualizar.
        contentValues.put(KEY_NOMBRE, tarea.nombre)
        contentValues.put(KEY_FECHA, tarea.fecha)
        contentValues.put(KEY_COMPLETADA, tarea.completada)

        // Actualiza la fila correspondiente y retorna el número de filas afectadas.
        return db.update(TABLE_TAREAS, contentValues, "$KEY_ID = ?", arrayOf(tarea.id.toString()))
    }

    // Método para eliminar un disco de la base de datos.
    fun deleteTarea(tarea: Tarea): Int {
        val db = this.writableDatabase
        // Elimina la fila correspondiente y retorna el número de filas afectadas.
        val success = db.delete(TABLE_TAREAS, "$KEY_ID = ?", arrayOf(tarea.id.toString()))
        db.close()
        return success
    }

    // Método para añadir un nuevo disco a la base de datos.
    fun addTarea(tarea: Tarea): Long {
        try {
            val db = this.writableDatabase
            val contentValues = ContentValues()
            contentValues.put(KEY_NOMBRE, tarea.nombre)
            contentValues.put(KEY_FECHA, tarea.fecha)
            contentValues.put(KEY_COMPLETADA, tarea.completada)

            val success = db.insert(TABLE_TAREAS, null, contentValues)
            db.close()
            return success
        } catch (e: Exception) {
            // Maneja la excepción en caso de error al insertar.
            Log.e("Error", "Error al agregar disco", e)
            return -1
        }
    }
}


