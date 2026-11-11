/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import modelo.Marca;

public class PanelMarcas extends JPanel {

    private final JTextField txtNombre = new JTextField(10);
    private final JTextField txtBuscar = new JTextField(10);
    private final JButton btnGuardar = new JButton("Guardar");
    private final JButton btnNuevo = new JButton("Nuevo");
    private final JButton btnEliminar = new JButton("Eliminar");
    private final JButton btnBuscar = new JButton("Buscar");
    private final DefaultTableModel modeloTabla =
            new DefaultTableModel(new String[]{"ID", "Nombre"}, 0) {
                @Override
                public boolean isCellEditable(int r, int c) { return false; }
            };
    private final JTable tabla = new JTable(modeloTabla);

    public PanelMarcas() {
        setLayout(new BorderLayout(8, 8));

        JPanel arriba = new JPanel(new FlowLayout(FlowLayout.LEFT));
        arriba.add(new JLabel("Buscar Marca:"));
        arriba.add(txtBuscar);
        arriba.add(btnBuscar);

        JPanel formulario = new JPanel(new FlowLayout(FlowLayout.LEFT));
        formulario.add(new JLabel("Marca:"));
        formulario.add(txtNombre);
        formulario.add(btnGuardar);
        formulario.add(btnNuevo);
        formulario.add(btnEliminar);

        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        add(arriba, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(formulario, BorderLayout.SOUTH);
    }

    public void mostrarMarcas(List<Marca> lista) {
        modeloTabla.setRowCount(0);
        for (Marca m : lista) {
            modeloTabla.addRow(new Object[]{m.getId(), m.getNombre()});
        }
    }

    public int getFilaSeleccionada() { return tabla.getSelectedRow(); }
    public int getIdFila(int fila) { return (int) modeloTabla.getValueAt(fila, 0); }
    public String getNombreFila(int fila) { return (String) modeloTabla.getValueAt(fila, 1); }

    public String getTextoNombre() { return txtNombre.getText().trim(); }
    public void setTextoNombre(String t) { txtNombre.setText(t); }
    public String getTextoBuscar() { return txtBuscar.getText().trim(); }

    public void limpiar() {
        txtNombre.setText("");
        txtBuscar.setText("");
        tabla.clearSelection();
    }

    public void mostrarMensaje(String msg) { JOptionPane.showMessageDialog(this, msg); }

    public boolean confirmar(String msg) {
        return JOptionPane.showConfirmDialog(this, msg, "Confirmar",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    public JButton getBtnGuardar() { return btnGuardar; }
    public JButton getBtnNuevo() { return btnNuevo; }
    public JButton getBtnEliminar() { return btnEliminar; }
    public JButton getBtnBuscar() { return btnBuscar; }
    public JTable getTabla() { return tabla; }
}