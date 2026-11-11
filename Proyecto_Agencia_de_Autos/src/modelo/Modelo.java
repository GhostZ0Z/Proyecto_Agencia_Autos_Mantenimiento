/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/** Catálogo de modelos; cada modelo pertenece a una Marca (tabla modelos). */
public class Modelo {

    private int id;
    private Marca marca;
    private String nombre;

    public Modelo() {
    }

    public Modelo(int id, Marca marca, String nombre) {
        this.id = id;
        this.marca = marca;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /** Lo que se muestra al usar el objeto en un JComboBox o JList. */
    @Override
    public String toString() {
        return nombre;
    }
}
