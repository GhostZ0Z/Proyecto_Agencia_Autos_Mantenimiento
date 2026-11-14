/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import DAO.ServicioDAO;
import java.sql.SQLException;
import java.util.List;
import modelo.Anio;
import modelo.Modelo;
import modelo.Servicio;
import util.Sesion;

/**
 * CONTROLADOR de Servicios (Saúl): valida los datos capturados en la vista,
 * se comunica con ServicioDAO y genera el comprobante de datos básicos.
 */
public class ServicioControlador {

    private final ServicioDAO servicioDAO = new ServicioDAO();

    public List<Servicio> buscarPorFolioODueno(String folio, String dueno) throws SQLException {
        return servicioDAO.buscarPorFolioODueno(folio, dueno);
    }

    public boolean registrar(Servicio s) throws SQLException {
        validarDatos(s);
        if (Sesion.getInstancia().haySesion()) {
            s.setUsuario(Sesion.getInstancia().getUsuarioActual());
        }
        return servicioDAO.insertar(s);
    }

    public boolean modificar(Servicio s) throws SQLException {
        validarDatos(s);
        return servicioDAO.actualizar(s);
    }

    public boolean eliminar(int id) throws SQLException {
        return servicioDAO.eliminar(id);
    }

    public boolean cambiarEstatus(int id, String estatus) throws SQLException {
        return servicioDAO.actualizarEstatus(id, estatus);
    }

    public List<Modelo> listarModelos() throws SQLException {
        return servicioDAO.listarModelos();
    }

    public List<Anio> listarAnios() throws SQLException {
        return servicioDAO.listarAnios();
    }

    private void validarDatos(Servicio s) {
        if (s.getFolio() == null || s.getFolio().trim().isEmpty()) {
            throw new IllegalArgumentException("El folio es obligatorio.");
        }
        if (s.getVehiculo() == null || s.getVehiculo().getPropietario() == null
                || s.getVehiculo().getPropietario().getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del dueño es obligatorio.");
        }
    }

    /** Genera el texto del comprobante de datos básicos correspondiente al servicio. */
    public String generarComprobante(Servicio s) {
        return "=== COMPROBANTE DE DATOS BÁSICOS ===\n\n"
                + "Folio: " + s.getFolio() + "\n"
                + "Dueño: " + s.getVehiculo().getPropietario().getNombre() + "\n"
                + "Teléfono: " + s.getVehiculo().getPropietario().getTelefono() + "\n"
                + "Vehículo: " + s.getVehiculo().getModelo() + " (" + s.getVehiculo().getAnio() + ")\n"
                + "Placas: " + s.getVehiculo().getPlacas() + "\n"
                + "Quien entrega: " + s.getPersonaEntrega() + "\n"
                + "Tipo de servicio: " + s.getTipo() + "\n"
                + "Descripción: " + s.getDescripcion() + "\n"
                + "Fecha de ingreso: " + s.getFechaIngreso() + "\n"
                + "Próximo servicio: " + (s.getProximoServicio() != null ? s.getProximoServicio() : "N/A") + "\n"
                + "Estatus: " + s.getEstatus() + "\n";
    }
}
