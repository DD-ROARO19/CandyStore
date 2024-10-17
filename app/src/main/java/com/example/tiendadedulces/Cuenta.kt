package com.example.tiendadedulces

class Cuenta() {

    var usuario: String = ""
    var contra: String = ""

    constructor(user:String, clave:String): this(){
        this.usuario = user
        this.contra = clave
    }

}