package com.example.tiendadedulces

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.IOException

class CarritoActivity : AppCompatActivity() {

    private lateinit var carritoText: TextView

    private var texto:String = ""
    private var costo:Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_carrito)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        testReadLista()

        carritoText = findViewById(R.id.txtCarrito_list)

        texto = "Dulces seleccionados:\n\n"

        val listaData = leerLista(this)
//        val listaData = leerListaCache(this)
        listaData?.forEach {
            texto += "${it.nombre} - x${it.cantidad}\n"
            costo += (it.costo)?.plus(0)?.times(it.cantidad) ?: 0.0
        }
        val pie = "\nPrecio Total: $$costo"
        texto += pie

        carritoText.text = texto

    }//onCreate

    fun onClick(v: View?){
        when(v?.id){
            R.id.btn_borrar -> {
                borrarCarrito()
            }
        }//when
    }//onClick

    private fun borrarCarrito(){
        val file = File(this.filesDir, "articulosList.json")
//        val file = File(this.cacheDir, "articulosList.txt")
        file.delete()

        carritoText.text = "Dulces seleccionados:\n\n\nPrecio Total: $0.0"
        Toast.makeText(this, "Carrito eliminado", Toast.LENGTH_SHORT).show()
    }

    private fun testReadLista(){
        val testLista = leerLista(this)
//        val testLista = leerListaCache(this)
        if (testLista != null) {
            for(articulo in testLista){
                println("Articulo: ${articulo?.nombre}")
            }
        }
        println("hola - test")
        //println(this.filesDir.toString())
    }


    // Leer lista - Versión de Cache
    private fun leerListaCache(context: Context): MutableList<Articulos>? {
        val cacheFile = File(context.cacheDir, "articulosList.txt")

        return if (cacheFile.exists()) {
            val jsonList = cacheFile.readText()
            val gson = Gson()

            // Convert JSON string back to a list of Person objects
            val type = object : TypeToken<MutableList<Articulos>>() {}.type
            gson.fromJson(jsonList, type)
        } else {
            null
        }
    }
    //
    // Leer lista - Versión de Almacenamiento de Aplicación
    private fun leerLista(context: Context): MutableList<Articulos>?{
        val file = File(context.filesDir, "articulosList.json")

        // Checar si el archivo existe
        if (!file.exists()) {
            println("Archivo no encontrado")
            return null
        }

        return try {
            // Read the file content
            val jsonString = file.readText()

            // Convertir el JSON de nuevo a lista de articulos
            val gson = Gson()
            val type = object : TypeToken<MutableList<Articulos>>() {}.type
            gson.fromJson<MutableList<Articulos>>(jsonString, type)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

}//Class