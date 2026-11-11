/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import DAO.MarcaDAO;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import modelo.Marca;
import vista.PanelMarcas;

public class MarcaControlador {
 
    private final PanelMarcas vista;
    private final MarcaDAO dao;
    private Marca seleccionado; // null = modo "nuevo"

    public MarcaControlador(PanelMarcas vista, MarcaDAO dao) {
        this.vista = vista;
        this.dao = dao;
        conectarEventos();
        cargarTabla();
    }

    public PanelMarcas getVista() { return vista; }

    private void conectarEventos() {
        vista.getBtnGuardar().addActionListener(e -> guardar());
        vista.getBtnNuevo().addActionListener(e -> nuevo());
        vista.getBtnEliminar().addActionListener(e -> eliminar());
        vista.getBtnBuscar().addActionListener(e -> buscar());

        vista.getTabla().getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            int fila = vista.getFilaSeleccionada();
            if (fila >= 0) {
                seleccionado = new Marca(vista.getIdFila(fila), vista.getNombreFila(fila));
                vista.setTextoNombre(String.valueOf(seleccionado.getNombre()));
            }
        });
    }

    private void cargarTabla() {
        try {
            vista.mostrarMarcas(dao.listar());
        } catch (SQLException ex) {
            vista.mostrarMensaje("Error al cargar: " + ex.getMessage());
        }
    }

    private void guardar() {
        String valor = vista.getTextoNombre();
        if (valor.isEmpty()) {
            vista.mostrarMensaje("Escribe el nombre de la marca.");
            return;
        }
        try {
            if (seleccionado == null) {
                dao.insertar(new Marca(0, valor));
            } else {
                seleccionado.setNombre(valor);
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
            vista.mostrarMensaje("Selecciona una marca de la tabla.");
            return;
        }
        if (!vista.confirmar("¿Eliminar la marca " + seleccionado.getNombre() + "?")) return;
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
            List<Marca> todos = dao.listar();
            if (texto.isEmpty()) {
                vista.mostrarMarcas(todos);
            } else {
                vista.mostrarMarcas(todos.stream()
                        .filter(a -> String.valueOf(a.getNombre()).contains(texto))
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