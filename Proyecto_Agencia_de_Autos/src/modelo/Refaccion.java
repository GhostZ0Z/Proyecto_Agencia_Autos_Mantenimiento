/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/** Refacción o pieza utilizada en los servicios (tabla refacciones). */
public class Refaccion {

    private int id;
    private String nombre;
    private double precio;

    public Refaccion() {
    }

    public Refaccion(int id, String nombre, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    /** Lo que se muestra al usar el objeto en un JComboBox o JList. */
    @Override
    public String toString() {
        return nombre;
    }
}

