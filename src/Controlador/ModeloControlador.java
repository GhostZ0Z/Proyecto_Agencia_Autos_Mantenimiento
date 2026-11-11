/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import DAO.MarcaDAO;
import DAO.ModeloDAO;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import modelo.Marca;
import modelo.Modelo;
import vista.PanelModelos;

public class ModeloControlador {

    private final PanelModelos vista;
    private final ModeloDAO dao;
    private final MarcaDAO marcaDAO = new MarcaDAO();
    private Modelo seleccionado;

    public ModeloControlador(PanelModelos vista, ModeloDAO dao) {
        this.vista = vista;
        this.dao = dao;
        conectarEventos();
        cargarTabla();
    }

    public PanelModelos getVista() { return vista; }

    private void conectarEventos() {
        vista.getBtnGuardar().addActionListener(e -> guardar());
        vista.getBtnNuevo().addActionListener(e -> nuevo());
        vista.getBtnEliminar().addActionListener(e -> eliminar());
        vista.getBtnBuscar().addActionListener(e -> buscar());

        vista.getTabla().getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            int fila = vista.getFilaSeleccionada();
            if (fila >= 0) {
                seleccionado = new Modelo(vista.getIdFila(fila), vista.getMarcaFila(fila), vista.getNombreFila(fila));
                vista.setMarcaSeleccionada(seleccionado.getMarca());
                vista.setTextoNombre(seleccionado.getNombre());
            }
        });
    }

    public void cargarTabla() {
        try {
            vista.cargarMarcas(marcaDAO.listar());
            vista.mostrarModelos(dao.listar());
        } catch (SQLException ex) {
            vista.mostrarMensaje("Error al cargar: " + ex.getMessage());
        }
    }

    private void guardar() {
        Marca m = vista.getMarcaSeleccionada();
        String nombre = vista.getTextoNombre();
        if (m == null || nombre.isEmpty()) {
            vista.mostrarMensaje("Selecciona una marca y escribe el nombre del modelo.");
            return;
        }
        try {
            if (seleccionado == null) {
                dao.insertar(new Modelo(0, m, nombre));
            } else {
                seleccionado.setMarca(m);
                seleccionado.setNombre(nombre);
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
            vista.mostrarMensaje("Selecciona un modelo de la tabla.");
            return;
        }
        if (!vista.confirmar("¿Eliminar el modelo " + seleccionado.getNombre() + "?")) return;
        try {
            dao.eliminar(seleccionado.getId());
            nuevo();
            cargarTabla();
        } catch (SQLException ex) {
            vista.mostrarMensaje("No se puede eliminar (puede estar en uso): " + ex.getMessage());
        }
    }

    private void buscar() {
        String texto = vista.getTextoBuscar().toLowerCase();
        try {
            List<Modelo> todos = dao.listar();
            if (texto.isEmpty()) {
                vista.mostrarModelos(todos);
            } else {
                vista.mostrarModelos(todos.stream()
                        .filter(m -> m.getNombre().toLowerCase().contains(texto)
                                || m.getMarca().getNombre().toLowerCase().contains(texto))
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
