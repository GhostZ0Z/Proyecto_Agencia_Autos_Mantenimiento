/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;


import DAO.AnioDAO;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import modelo.Anio;
import vista.PanelAnios;

public class AnioControlador {

    private final PanelAnios vista;
    private final AnioDAO dao;
    private Anio seleccionado; // null = modo "nuevo"

    public AnioControlador(PanelAnios vista, AnioDAO dao) {
        this.vista = vista;
        this.dao = dao;
        conectarEventos();
        cargarTabla();
    }

    public PanelAnios getVista() { return vista; }

    private void conectarEventos() {
        vista.getBtnGuardar().addActionListener(e -> guardar());
        vista.getBtnNuevo().addActionListener(e -> nuevo());
        vista.getBtnEliminar().addActionListener(e -> eliminar());
        vista.getBtnBuscar().addActionListener(e -> buscar());

        vista.getTabla().getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            int fila = vista.getFilaSeleccionada();
            if (fila >= 0) {
                seleccionado = new Anio(vista.getIdFila(fila), vista.getAnioFila(fila));
                vista.setTextoAnio(String.valueOf(seleccionado.getAnio()));
            }
        });
    }

    private void cargarTabla() {
        try {
            vista.mostrarAnios(dao.listar());
        } catch (SQLException ex) {
            vista.mostrarMensaje("Error al cargar: " + ex.getMessage());
        }
    }

    private void guardar() {
        int valor;
        try {
            valor = Integer.parseInt(vista.getTextoAnio());
        } catch (NumberFormatException ex) {
            vista.mostrarMensaje("Escribe un año válido (solo números).");
            return;
        }
        try {
            if (seleccionado == null) {
                dao.insertar(new Anio(0, valor));
            } else {
                seleccionado.setAnio(valor);
                dao.actualizar(seleccionado);
            }
            nuevo();
            cargarTabla();
        } catch (SQLException ex) {
            vista.mostrarMensaje("No se pudo guardar: " + ex.getMessage());
        }
    }

    private void eliminar() {
        if (seleccionado == null) {
            vista.mostrarMensaje("Selecciona un año de la tabla.");
            return;
        }
        if (!vista.confirmar("¿Eliminar el año " + seleccionado.getAnio() + "?")) return;
        try {
            dao.eliminar(seleccionado.getId());
            nuevo();
            cargarTabla();
        } catch (SQLException ex) {
            vista.mostrarMensaje("No se puede eliminar (puede estar en uso): " + ex.getMessage());
        }
    }

    private void buscar() {
        String texto = vista.getTextoBuscar();
        try {
            List<Anio> todos = dao.listar();
            if (texto.isEmpty()) {
                vista.mostrarAnios(todos);
            } else {
                vista.mostrarAnios(todos.stream()
                        .filter(a -> String.valueOf(a.getAnio()).contains(texto))
                        .collect(Collectors.toList()));
            }
        } catch (SQLException ex) {
            vista.mostrarMensaje("Error al buscar: " + ex.getMessage());
        }
    }

    private void nuevo() {
        seleccionado = null;
        vista.limpiar();
    }
}