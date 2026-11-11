/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import modelo.Usuario;

/**
 * SINGLETON de sesión: guarda quién inició sesión mientras el programa está abierto.
 * Es la "variable global de sesión" que pide el requisito 3: al cerrar el sistema se
 * llama a cerrar(), que borra al usuario y cierra la base de datos.
 */
public class Sesion {

    private static Sesion instancia;
    private Usuario usuarioActual;

    private Sesion() {
    }

    public static synchronized Sesion getInstancia() {
        if (instancia == null) {
            instancia = new Sesion();
        }
        return instancia;
    }

    public void iniciar(Usuario usuario) {
        this.usuarioActual = usuario;
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public boolean haySesion() {
        return usuarioActual != null;
    }

    /** Cierra la sesión: destruye el usuario actual y cierra la conexión a la BD. */
    public void cerrar() {
        usuarioActual = null;
        ConexionDB.getInstancia().cerrar();
    }
}

