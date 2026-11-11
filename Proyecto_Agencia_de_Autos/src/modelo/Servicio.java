/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/** Servicio preventivo o correctivo de un vehículo. Un servicio puede usar varias refacciones (tabla servicios). */
public class Servicio {

    private int id;
    private String folio;
    private Vehiculo vehiculo;
    private Usuario usuario;
    private String personaEntrega;
    private String tipo;
    private String descripcion;
    private String estatus;
    private Date fechaIngreso;
    private Date proximoServicio;
    private List<ServicioRefaccion> refacciones = new ArrayList<>();

    public Servicio() {
    }

    public Servicio(int id, String folio, Vehiculo vehiculo, Usuario usuario, String personaEntrega, String tipo, String descripcion, String estatus, Date fechaIngreso, Date proximoServicio) {
        this.id = id;
        this.folio = folio;
        this.vehiculo = vehiculo;
        this.usuario = usuario;
        this.personaEntrega = personaEntrega;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.estatus = estatus;
        this.fechaIngreso = fechaIngreso;
        this.proximoServicio = proximoServicio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getPersonaEntrega() {
        return personaEntrega;
    }

    public void setPersonaEntrega(String personaEntrega) {
        this.personaEntrega = personaEntrega;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Date getProximoServicio() {
        return proximoServicio;
    }

    public void setProximoServicio(Date proximoServicio) {
        this.proximoServicio = proximoServicio;
    }

    public List<ServicioRefaccion> getRefacciones() {
        return refacciones;
    }

    public void setRefacciones(List<ServicioRefaccion> refacciones) {
        this.refacciones = refacciones;
    }
}

