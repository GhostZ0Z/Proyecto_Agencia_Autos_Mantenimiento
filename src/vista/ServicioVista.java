/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

import controlador.ServicioControlador;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import modelo.Anio;
import modelo.Modelo;
import modelo.Propietario;
import modelo.Servicio;
import modelo.Vehiculo;

/**
 * VISTA de Servicios (Saúl):
 * 1) Formulario de registro y modificación de servicios (busca por folio y/o dueño) y comprobante de datos básicos.
 * 2) Consulta por folio y/o dueño con cambio de estatus ("En espera", "En proceso", "Finalizado").
 */
public class ServicioVista extends JFrame {

    private final ServicioControlador controlador = new ServicioControlador();
    private final JTabbedPane pestanas = new JTabbedPane();

    // Buscador para modificar en Pestaña 1
    private final JTextField txtBuscarFolioMod = new JTextField(8);
    private final JTextField txtBuscarDuenoMod = new JTextField(12);

    // Campos del formulario de servicio
    private int idServicioActual = 0;
    private int idVehiculoActual = 0;
    private int idPropietarioActual = 0;

    private final JTextField txtFolio = new JTextField(12);
    private final JTextField txtDueno = new JTextField(15);
    private final JTextField txtTelefono = new JTextField(12);
    private final JComboBox<Modelo> cmbModelo = new JComboBox<>();
    private final JComboBox<Anio> cmbAnio = new JComboBox<>();
    private final JTextField txtPlacas = new JTextField(10);
    private final JTextField txtPersonaEntrega = new JTextField(15);
    private final JComboBox<String> cmbTipo = new JComboBox<>(new String[]{"Preventivo", "Correctivo"});
    private final JComboBox<String> cmbEstatus = new JComboBox<>(new String[]{"En espera", "En proceso", "Finalizado"});
    private final JTextField txtFechaIngreso = new JTextField(LocalDate.now().toString(), 10);
    private final JTextField txtProximoServicio = new JTextField(LocalDate.now().plusMonths(6).toString(), 10);
    private final JTextField txtDescripcion = new JTextField(20);

    // Componentes de Pestaña 2 (Consulta y Estatus)
    private final JTextField txtFiltroFolio = new JTextField(8);
    private final JTextField txtFiltroDueno = new JTextField(12);
    private final DefaultTableModel modeloTabla = new DefaultTableModel(
            new Object[]{"Folio", "Dueño", "Vehículo", "Placas", "Tipo", "Ingreso", "Estatus"}, 0);
    private final JTable tabla = new JTable(modeloTabla);
    private List<Servicio> listaActual = new ArrayList<>();

    public ServicioVista() {
        super("Agencia de Automóviles - Servicios de Mantenimiento");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(780, 520);
        setLocationRelativeTo(null);

        pestanas.addTab("Registro y Modificación", crearPanelFormulario());
        pestanas.addTab("Consulta y Estatus", crearPanelConsulta());
        add(pestanas);

        cargarCombosCatalogo();
        consultarTabla();
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Buscar por folio y/o dueño para modificar
        JPanel panelBuscar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBuscar.setBorder(BorderFactory.createTitledBorder("Buscar servicio para modificar (Folio y/o Dueño)"));
        panelBuscar.add(new JLabel("Folio:"));
        panelBuscar.add(txtBuscarFolioMod);
        panelBuscar.add(new JLabel("Dueño:"));
        panelBuscar.add(txtBuscarDuenoMod);
        JButton btnBuscarMod = new JButton("Buscar");
        btnBuscarMod.addActionListener(e -> buscarParaModificar());
        panelBuscar.add(btnBuscarMod);
        panel.add(panelBuscar, BorderLayout.NORTH);

        // Campos básicos del formulario
        JPanel panelCampos = new JPanel(new GridLayout(11, 2, 6, 6));
        panelCampos.setBorder(BorderFactory.createTitledBorder("Datos básicos del servicio"));
        panelCampos.add(new JLabel("Folio:"));
        panelCampos.add(txtFolio);
        panelCampos.add(new JLabel("Nombre del dueño:"));
        panelCampos.add(txtDueno);
        panelCampos.add(new JLabel("Teléfono del dueño:"));
        panelCampos.add(txtTelefono);
        panelCampos.add(new JLabel("Modelo del vehículo:"));
        panelCampos.add(cmbModelo);
        panelCampos.add(new JLabel("Año:"));
        panelCampos.add(cmbAnio);
        panelCampos.add(new JLabel("Placas:"));
        panelCampos.add(txtPlacas);
        panelCampos.add(new JLabel("Persona que entrega el auto:"));
        panelCampos.add(txtPersonaEntrega);
        panelCampos.add(new JLabel("Tipo de mantenimiento:"));
        panelCampos.add(cmbTipo);
        panelCampos.add(new JLabel("Estatus:"));
        panelCampos.add(cmbEstatus);
        panelCampos.add(new JLabel("Fecha ingreso (YYYY-MM-DD):"));
        panelCampos.add(txtFechaIngreso);
        panelCampos.add(new JLabel("Descripción del servicio:"));
        panelCampos.add(txtDescripcion);
        panel.add(panelCampos, BorderLayout.CENTER);

        // Botones inferiores
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnRegistrar = new JButton("Registrar");
        JButton btnModificar = new JButton("Guardar Modificación");
        JButton btnComprobante = new JButton("Ver Comprobante");
        JButton btnLimpiar = new JButton("Limpiar");

        btnRegistrar.addActionListener(e -> registrarServicio());
        btnModificar.addActionListener(e -> modificarServicio());
        btnComprobante.addActionListener(e -> mostrarComprobanteActual());
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnComprobante);
        panelBotones.add(btnLimpiar);
        panel.add(panelBotones, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearPanelConsulta() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Filtro superior por folio y/o dueño
        JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFiltro.setBorder(BorderFactory.createTitledBorder("Consulta por Folio y/o Dueño"));
        panelFiltro.add(new JLabel("Folio:"));
        panelFiltro.add(txtFiltroFolio);
        panelFiltro.add(new JLabel("Dueño:"));
        panelFiltro.add(txtFiltroDueno);

        JButton btnBuscar = new JButton("Buscar");
        JButton btnTodos = new JButton("Mostrar todos");
        btnBuscar.addActionListener(e -> consultarTabla());
        btnTodos.addActionListener(e -> {
            txtFiltroFolio.setText("");
            txtFiltroDueno.setText("");
            consultarTabla();
        });
        panelFiltro.add(btnBuscar);
        panelFiltro.add(btnTodos);
        panel.add(panelFiltro, BorderLayout.NORTH);

        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Botones de estatus y operaciones
        JPanel panelAbajo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnEspera = new JButton("En espera");
        JButton btnProceso = new JButton("En proceso");
        JButton btnFinalizado = new JButton("Finalizado");
        JButton btnIrRegistrar = new JButton("Registrar");
        JButton btnIrModificar = new JButton("Modificar");
        JButton btnEliminar = new JButton("Eliminar");

        btnEspera.addActionListener(e -> cambiarEstatusSeleccionado("En espera"));
        btnProceso.addActionListener(e -> cambiarEstatusSeleccionado("En proceso"));
        btnFinalizado.addActionListener(e -> cambiarEstatusSeleccionado("Finalizado"));
        btnIrRegistrar.addActionListener(e -> {
            limpiarFormulario();
            pestanas.setSelectedIndex(0);
        });
        btnIrModificar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila >= 0 && fila < listaActual.size()) {
                cargarEnFormulario(listaActual.get(fila));
                pestanas.setSelectedIndex(0);
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona una fila de la tabla.");
            }
        });
        btnEliminar.addActionListener(e -> eliminarSeleccionado());

        panelAbajo.add(btnEspera);
        panelAbajo.add(btnProceso);
        panelAbajo.add(btnFinalizado);
        panelAbajo.add(btnIrRegistrar);
        panelAbajo.add(btnIrModificar);
        panelAbajo.add(btnEliminar);
        panel.add(panelAbajo, BorderLayout.SOUTH);

        return panel;
    }

    private void cargarCombosCatalogo() {
        try {
            cmbModelo.removeAllItems();
            for (Modelo m : controlador.listarModelos()) {
                cmbModelo.addItem(m);
            }
            cmbAnio.removeAllItems();
            for (Anio a : controlador.listarAnios()) {
                cmbAnio.addItem(a);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar catálogos: " + ex.getMessage());
        }
    }

    private Servicio construirServicioDesdeFormulario() {
        Propietario p = new Propietario(idPropietarioActual, txtDueno.getText().trim(), txtTelefono.getText().trim());
        Modelo m = (Modelo) cmbModelo.getSelectedItem();
        Anio a = (Anio) cmbAnio.getSelectedItem();
        Vehiculo v = new Vehiculo(idVehiculoActual, p, m, a, txtPlacas.getText().trim());

        Date fechaIng = Date.valueOf(txtFechaIngreso.getText().trim());
        Date fechaProx = Date.valueOf(txtProximoServicio.getText().trim());

        return new Servicio(
                idServicioActual,
                txtFolio.getText().trim(),
                v,
                null,
                txtPersonaEntrega.getText().trim(),
                (String) cmbTipo.getSelectedItem(),
                txtDescripcion.getText().trim(),
                (String) cmbEstatus.getSelectedItem(),
                fechaIng,
                fechaProx
        );
    }

    private void cargarEnFormulario(Servicio s) {
        idServicioActual = s.getId();
        idVehiculoActual = s.getVehiculo().getId();
        idPropietarioActual = s.getVehiculo().getPropietario().getId();

        txtFolio.setText(s.getFolio());
        txtDueno.setText(s.getVehiculo().getPropietario().getNombre());
        txtTelefono.setText(s.getVehiculo().getPropietario().getTelefono());
        txtPlacas.setText(s.getVehiculo().getPlacas());
        txtPersonaEntrega.setText(s.getPersonaEntrega());
        cmbTipo.setSelectedItem(s.getTipo());
        cmbEstatus.setSelectedItem(s.getEstatus());
        txtFechaIngreso.setText(s.getFechaIngreso().toString());
        txtDescripcion.setText(s.getDescripcion());

        for (int i = 0; i < cmbModelo.getItemCount(); i++) {
            if (cmbModelo.getItemAt(i).getId() == s.getVehiculo().getModelo().getId()) {
                cmbModelo.setSelectedIndex(i);
                break;
            }
        }
        for (int i = 0; i < cmbAnio.getItemCount(); i++) {
            if (cmbAnio.getItemAt(i).getId() == s.getVehiculo().getAnio().getId()) {
                cmbAnio.setSelectedIndex(i);
                break;
            }
        }
    }

    private void buscarParaModificar() {
        String f = txtBuscarFolioMod.getText().trim();
        String d = txtBuscarDuenoMod.getText().trim();
        if (f.isEmpty() && d.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingresa el folio y/o el nombre del dueño.");
            return;
        }
        try {
            List<Servicio> encontrados = controlador.buscarPorFolioODueno(f, d);
            if (encontrados.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No se encontró ningún servicio.");
            } else {
                cargarEnFormulario(encontrados.get(0));
                JOptionPane.showMessageDialog(this, "Datos cargados para modificar.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error de BD: " + ex.getMessage());
        }
    }

    private void registrarServicio() {
        try {
            Servicio s = construirServicioDesdeFormulario();
            if (controlador.registrar(s)) {
                mostrarDialogoComprobante(s);
                limpiarFormulario();
                consultarTabla();
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al registrar: " + ex.getMessage());
        }
    }

    private void modificarServicio() {
        if (idServicioActual == 0) {
            JOptionPane.showMessageDialog(this, "Primero busca o selecciona un servicio para modificar.");
            return;
        }
        try {
            Servicio s = construirServicioDesdeFormulario();
            if (controlador.modificar(s)) {
                mostrarDialogoComprobante(s);
                consultarTabla();
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al modificar: " + ex.getMessage());
        }
    }

    private void mostrarComprobanteActual() {
        try {
            Servicio s = construirServicioDesdeFormulario();
            mostrarDialogoComprobante(s);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Completa los datos para ver el comprobante.");
        }
    }

    private void mostrarDialogoComprobante(Servicio s) {
        JTextArea area = new JTextArea(controlador.generarComprobante(s), 14, 40);
        area.setEditable(false);
        JOptionPane.showMessageDialog(this, new JScrollPane(area),
                "Comprobante de Datos Básicos", JOptionPane.INFORMATION_MESSAGE);
    }

    private void limpiarFormulario() {
        idServicioActual = 0;
        idVehiculoActual = 0;
        idPropietarioActual = 0;
        txtFolio.setText("");
        txtDueno.setText("");
        txtTelefono.setText("");
        txtPlacas.setText("");
        txtPersonaEntrega.setText("");
        txtDescripcion.setText("");
        cmbTipo.setSelectedIndex(0);
        cmbEstatus.setSelectedIndex(0);
        txtFechaIngreso.setText(LocalDate.now().toString());
    }

    private void consultarTabla() {
        try {
            listaActual = controlador.buscarPorFolioODueno(txtFiltroFolio.getText(), txtFiltroDueno.getText());
            modeloTabla.setRowCount(0);
            for (Servicio s : listaActual) {
                modeloTabla.addRow(new Object[]{
                    s.getFolio(),
                    s.getVehiculo().getPropietario().getNombre(),
                    s.getVehiculo().getModelo() + " " + s.getVehiculo().getAnio(),
                    s.getVehiculo().getPlacas(),
                    s.getTipo(),
                    s.getFechaIngreso(),
                    s.getEstatus()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al consultar servicios: " + ex.getMessage());
        }
    }

    private void cambiarEstatusSeleccionado(String nuevoEstatus) {
        int fila = tabla.getSelectedRow();
        if (fila < 0 || fila >= listaActual.size()) {
            JOptionPane.showMessageDialog(this, "Selecciona una fila de la tabla.");
            return;
        }
        try {
            controlador.cambiarEstatus(listaActual.get(fila).getId(), nuevoEstatus);
            consultarTabla();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cambiar estatus: " + ex.getMessage());
        }
    }

    private void eliminarSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0 || fila >= listaActual.size()) {
            JOptionPane.showMessageDialog(this, "Selecciona una fila de la tabla.");
            return;
        }
        try {
            controlador.eliminar(listaActual.get(fila).getId());
            consultarTabla();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ServicioVista().setVisible(true));
    }
}
