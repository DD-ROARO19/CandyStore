package com.example.tiendadedulces

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileWriter
import java.io.IOException

class ArticuloActivity : AppCompatActivity() {

    private lateinit var name: TextView
    private lateinit var desc: TextView
    private lateinit var img: ImageView

    private lateinit var objArt: Articulos
    private var lista: MutableList<Articulos>? = null

    //Variables
    private val CHANNEL_ID = "Canal_notificacion"
    private val textTitle = "Carrito actualizado"
    //private val textContent = "Este es el texto informativo de la notificación"
    private val notificationId = 100
    private val complete_notificationId = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_articulo)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val file = File(this.filesDir, "articulosList.json")
//        val file = File(this.cacheDir, "articulosList.txt")

        // Checar si el archivo existe
        if (!file.exists()) {
            println("Archivo no encontrado")
            crearLista(this)
//            crearListaCache(this)
        }

        lista = leerLista(this)
//        lista = leerListaCache(this)

        objArt = Articulos()

        name = findViewById(R.id.txt_articuloName)
        desc = findViewById(R.id.txt_articuloDetalle)
        img = findViewById(R.id.img_articulo)

        //Datos recibidos
        val infoRecibida = intent.extras

        //Datos
        val nombre: String?
        val detalle: String?
        val costo: String?
        val num: Int?

        //Validar si recibe la info
        if (infoRecibida != null){

            //Recibir los datos del accesorio
            nombre = infoRecibida.getString("nombre")
            detalle = infoRecibida.getString("detalle")
            costo = infoRecibida.getString("costo")
            num = infoRecibida.getInt("numAccesorio")

            //Guardarlos en el objeto
            objArt.nombre = nombre
            objArt.detalle = detalle
            objArt.costo = infoRecibida.getDouble("costoDouble")
            objArt.imagen = num

            //Colocar en TextView
            name.text = nombre
            desc.text = "Descripción del producto:\n${detalle}\nCosto: ${costo}"

            //Cambio de la imagen
            when(num){
                1 -> img.setImageResource(R.mipmap.dulce01)
                2 -> img.setImageResource(R.mipmap.dulce02)
                3 -> img.setImageResource(R.mipmap.dulce03)
                4 -> img.setImageResource(R.mipmap.dulce04)
                5 -> img.setImageResource(R.mipmap.dulce05)
            }
        }else{//if (infoRecibida != null)
            nombre = "Sin nombre"
            detalle = "Sin detalle"
            costo = "$0.00"
            num = 1

            //colocar en TextView
            name.text = nombre
            desc.text = "Descripción del producto:\n${detalle}\nCosto: ${costo}"
        }

        // NOTIFICAIONES

        //Crear canal de notificacion y definir importancia
        createNotificationChannel()

        // Directorio
        //println(this.cacheDir)

    }//onCreate

    private fun createNotificationChannel() {
        val name = "Visual Channel"
        val descriptionText = "Canal para visualizar notificaciones"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply{
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }//createNotificationChannel

    fun onClick(v: View) {
        when(v?.id) {
            R.id.btn_agregar -> {
                agregar()
            }//Agregar
        }
    }//onClick

    private fun agregar() {
        val primerNombre = lista?.get(0)?.nombre
        if (primerNombre == "Sin nombre"){
            lista?.set(0, objArt)
        }else{
            var encontrado: Boolean = false
            lista?.forEach {
                if (objArt.nombre == it?.nombre) {
                    it.cantidad += 1
                    encontrado = true
                }
            }
            if (!encontrado) { lista?.add( objArt ) }
        }
        actualizarLista(this, lista)
//        actualizarListaCache(this, lista)
        testReadLista()
//        notificacionBasica()
//        notificacionToque()
        notificacionProgress()
    }

    private fun testReadLista(){
        val testLista = leerLista(this)
//        val testLista = leerListaCache(this)
        if (testLista != null) {
            for(articulo in testLista){
                println("Articulo: ${articulo?.nombre}")
            }
        }else{
            println("testLista State: $testLista")
        }
        println("hola - test")
    }


    // Leer lista - Versión de Cache
    private fun leerListaCache(context: Context): MutableList<Articulos>? {
        val cacheFile = File(context.cacheDir, "articulosList.txt")

        if (!cacheFile.exists()){
            println("No se encontro cache")
            return null
        }

        return try {
            println("trying giving cache")

            val jsonList = cacheFile.readText()
            val gson = Gson()

            // Convert JSON string back to a list of Person objects
            val type = object : TypeToken<MutableList<Articulos>>() {}.type
//            gson.fromJson(jsonList, type)
            gson.fromJson<MutableList<Articulos>>(jsonList, type)
        } catch (e: IOException) {
            e.printStackTrace()
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


    // Guardar lista - Versión de Cache
    private fun actualizarListaCache(context: Context, lista: MutableList<Articulos>?): Unit? {
        val cacheFile = File(context.cacheDir.absolutePath, "articulosList.txt")
        val gson = Gson()

        if (lista == null){
            println("Guardar: cacheFile == null")
            return null
        }

        // Convert the list of objects to JSON
        val jsonList = gson.toJson(lista)

        // Escribir el JSON al cache
        try {
            println("Writing")
            cacheFile.writeText(jsonList)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
    //
    // Guardar lista - Versión de Almacenamiento de Aplicación
    private fun actualizarLista(context: Context, articulosList: MutableList<Articulos>?): Unit?{
        // Gson object para serializacion
        val gson = Gson()

        if (articulosList == null){
            return null
        }

        // Convertir lista a JSON string
        val jsonString = gson.toJson(articulosList)

        // Archivo donde se guardara la info
        val file = File(context.filesDir, "articulosList.json")

        try {
            // Escribir el JSON al archivo
            val fileWriter = FileWriter(file)
            fileWriter.write(jsonString)
            fileWriter.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }


    // Crear lista - Versión de Cache
    private fun crearListaCache(context: Context) {
        println("Creando lista")
        val listaDeArticulos = List<Articulos>(1) {Articulos()}

        File.createTempFile("articulosList.txt", null, context.cacheDir)

        val cacheFile = File(context.cacheDir, "articulosList.txt")
        val gson = Gson()

        // Convert the list of objects to JSON
        val jsonList = gson.toJson(listaDeArticulos)

        // Write JSON to the cache file
        cacheFile.writeText(jsonList)
    }
    // Crear lista - Versión de Almacenamiento de Aplicación
    private fun crearLista(context: Context){
        println("Creando lista")
        val listaDeArticulos = List<Articulos>(1) {Articulos()}

        // Gson object para serializacion
        val gson = Gson()

        // Convertir lista a JSON string
        val jsonString = gson.toJson(listaDeArticulos)

        // Archivo donde se guardara la info
        val file = File(context.filesDir, "articulosList.json")

        try {
            // Escribir el JSON al archivo
            val fileWriter = FileWriter(file)
            fileWriter.write(jsonString)
            fileWriter.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    @SuppressLint("MissingPermission")
    private fun notificacionBasica() {
        //Definir características de la notificación
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle(textTitle)
            //.setContentText(textContent)
            .setStyle(
                NotificationCompat.BigTextStyle()
                .bigText("¡Se ha agregado un nuevo articulo a su carrito!"))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        //Mostrar notificacion
        with(NotificationManagerCompat.from(this)) {
            // notificationId
            notify(notificationId, builder.build())
        }
        Toast.makeText(applicationContext, "Notificación básica", Toast.LENGTH_SHORT).show()
    }//notificacionBasica

    @SuppressLint("MissingPermission")
    private fun notificacionToque() {
        //createNotificationChannel()

        //Lanzamiento de Activity con el toque a la notificación
        val intent = Intent(this, CarritoActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0,
            intent, PendingIntent.FLAG_IMMUTABLE)

        //Definir características de la notificación
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle(textTitle)
            .setContentText("Toque para abrir su carrito")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            //intent
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        //Mostrar notificacion
        with(NotificationManagerCompat.from(this)) {
            // notificationId
            notify(complete_notificationId, builder.build())
        }
    }//notificacionToque

    private fun createNotificationChannelProgress() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Progress Notification"
            val descriptionText = "Shows progress of a task"
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    @SuppressLint("MissingPermission")
    private fun notificacionProgress() {

        createNotificationChannelProgress()

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Agregando dulces")
            .setContentText("Actualizando contenido del carrito para incluir nuevo articulo.")
            .setSmallIcon(R.drawable.notification_icon)
            //.setContentIntent(pendingIntent)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setProgress(100, 0, false) // Set initial progress to 0

        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(notificationId, builder.build())

        // Coroutine to update the progress every 1500ms
        CoroutineScope(Dispatchers.Main).launch {
            var progress = 0
            while (progress < 100) {
                delay(1500)
                progress += 20
                builder.setProgress(100, progress, false)
                notificationManager.notify(notificationId, builder.build())
            }

            // Once the task is complete, allow the user to dismiss the notification
            builder.setContentText("Carga completada")
                .setOngoing(false)
                .setProgress(0, 0, false)
                .setAutoCancel(true) // Allow dismissal by touch
                .setTimeoutAfter(1500)
            notificationManager.notify(notificationId, builder.build())

            //Despues de "Cargar"
            delay(1500)
            notificationManager.cancel(notificationId)
            notificacionToque()
        }
    }//notificacionProgress

}//MainClass