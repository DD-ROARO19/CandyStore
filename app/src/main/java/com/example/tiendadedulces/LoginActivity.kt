package com.example.tiendadedulces

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoginActivity : AppCompatActivity() {

    private lateinit var usuario: EditText
    private lateinit var contra: EditText

    private val cuentaObj = Cuenta("admin", "123ed")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        usuario = findViewById(R.id.edit_usuario)
        contra = findViewById(R.id.edit_contra)

    }//onCreate

    fun onClick(v: View?){
        when(v?.id){
            R.id.btn_aceptar -> {
                login()
            }
            R.id.btn_cancelar -> {
                Toast.makeText(this,"Cerrando", Toast.LENGTH_SHORT).show()
                finish()
            }
        }//when
    }//onClick

    private fun login(){
        //verificación de inputs vacios
        if ( usuario.text.isNotEmpty() && usuario.text.isNotBlank() &&
            contra.text.isNotBlank() && contra.text.isNotEmpty() ){
            verificar()
        }else{
            Toast.makeText(this,"Favor de llenar todos los datos", Toast.LENGTH_SHORT).show()
        }//verificación de inputs vacios
    }//login

    private fun verificar(){
        if (usuario.text.toString() == cuentaObj.usuario && contra.text.toString() == cuentaObj.contra){
            val intent = Intent(this, DrawerActivity::class.java)
            startActivity(intent)
        }else{
            Toast.makeText(this,"Datos de cuenta invalidos", Toast.LENGTH_SHORT).show()
        }
    }//verificar

}//Class