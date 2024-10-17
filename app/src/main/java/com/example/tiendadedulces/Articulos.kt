package com.example.tiendadedulces

class Articulos() {

    var nombre: String? = "Sin nombre"
    var detalle: String? = "Sin detalles"
    var costo: Double? = 0.0
    var imagen: Int? = 0
    var cantidad: Int = 1

    constructor(nom:String, det:String, cos:Double, img:Int): this(){
        this.nombre = nom
        this.detalle = det
        this.costo = cos
        this.imagen = img
    }

}