/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/** Vehículo con su propietario, modelo y año (tabla vehiculos). */
public class Vehiculo {

    private int id;
    private Propietario propietario;
    private Modelo modelo;
    private Anio anio;
    private String placas;

    public Vehiculo() {
    }

    public Vehiculo(int id, Propietario propietario, Modelo modelo, Anio anio, String placas) {
        this.id = id;
        this.propietario = propietario;
        this.modelo = modelo;
        this.anio = anio;
        this.placas = placas;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Propietario getPropietario() {
        return propietario;
    }

    public void setPropietario(Propietario propietario) {
        this.propietario = propietario;
    }

    public Modelo getModelo() {
        return modelo;
    }

    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }

    public Anio getAnio() {
        return anio;
    }

    public void setAnio(Anio anio) {
        this.anio = anio;
    }

    public String getPlacas() {
        return placas;
    }

    public void setPlacas(String placas) {
        this.placas = placas;
    }
}

