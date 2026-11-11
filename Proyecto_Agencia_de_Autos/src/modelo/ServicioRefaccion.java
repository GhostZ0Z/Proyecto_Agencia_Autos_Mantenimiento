/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/** Línea de detalle: una refacción y su cantidad dentro de un servicio (tabla servicio_refacciones). */
public class ServicioRefaccion {

    private Refaccion refaccion;
    private int cantidad;

    public ServicioRefaccion() {
    }

    public ServicioRefaccion(Refaccion refaccion, int cantidad) {
        this.refaccion = refaccion;
        this.cantidad = cantidad;
    }

    public Refaccion getRefaccion() {
        return refaccion;
    }

    public void setRefaccion(Refaccion refaccion) {
        this.refaccion = refaccion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}

