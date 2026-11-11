/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vista;

import DAO.AnioDAO;
import DAO.MarcaDAO;
import DAO.ModeloDAO;
import controlador.AnioControlador;
import controlador.MarcaControlador;
import controlador.ModeloControlador;
import java.awt.CardLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import util.Sesion;

public class MenuPrincipal extends JFrame {

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contenedor = new JPanel(cardLayout);
    private ModeloControlador modeloCtrl;
    private PanelServicios panelServicios;
    private PanelDashboard panelDashboard;

    public MenuPrincipal() {
        setTitle("Sistema de Mantenimiento - Agencia de Automóviles");
        setSize(900, 600);
        setLocationRelativeTo(null);
        // Nosotros controlamos el cierre para poder liberar recursos
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                salir();
            }
        });

        setJMenuBar(crearBarraMenu());
        registrarPaneles();
        add(contenedor);
    }

    private JMenuBar crearBarraMenu() {
        JMenuBar barra = new JMenuBar();

        // --- Catálogos (Alan) ---
        JMenu mCatalogos = new JMenu("Catálogos");
        mCatalogos.add(crearItem("Años", "anios"));
        mCatalogos.add(crearItem("Marcas", "marcas"));
        mCatalogos.add(crearItem("Modelos", "modelos"));
        barra.add(mCatalogos);

        // --- Operación (Saúl + Tablero) ---
        JMenu mOperacion = new JMenu("Operación");
        mOperacion.add(crearItem("Servicios", "servicios"));
        mOperacion.add(crearItem("Dashboard (Tablero)", "dashboard"));
        barra.add(mOperacion);

        // --- Sistema ---
        JMenu mSistema = new JMenu("Sistema");
        JMenuItem itemSalir = new JMenuItem("Salir");
        itemSalir.addActionListener(e -> salir());
        mSistema.add(itemSalir);
        barra.add(mSistema);

        return barra;
    }

    private JMenuItem crearItem(String texto, String nombrePanel) {
        JMenuItem item = new JMenuItem(texto);
        item.addActionListener(e -> {
            if ("modelos".equals(nombrePanel) && modeloCtrl != null) {
                modeloCtrl.cargarTabla();
            }
            if ("servicios".equals(nombrePanel) && panelServicios != null) {
                panelServicios.cargarCombosCatalogo();
                panelServicios.consultarTabla();
            }
            if ("dashboard".equals(nombrePanel) && panelDashboard != null) {
                panelDashboard.cargarDatos();
            }
            cardLayout.show(contenedor, nombrePanel);
        });
        return item;
    }

    private void registrarPaneles() {
        // Panel de bienvenida (se muestra al abrir)
        JPanel inicio = new JPanel();
        inicio.add(new JLabel("Bienvenido al sistema"));
        contenedor.add(inicio, "inicio");

        // Catálogos de Alan: Años, Marcas y Modelos
        AnioControlador anioCtrl = new AnioControlador(new PanelAnios(), new AnioDAO());
        MarcaControlador marcaCtrl = new MarcaControlador(new PanelMarcas(), new MarcaDAO());
        modeloCtrl = new ModeloControlador(new PanelModelos(), new ModeloDAO());

        contenedor.add(anioCtrl.getVista(), "anios");
        contenedor.add(marcaCtrl.getVista(), "marcas");
        contenedor.add(modeloCtrl.getVista(), "modelos");

        // Módulo de Servicios de Saúl
        panelServicios = new PanelServicios();
        contenedor.add(panelServicios, "servicios");

        // Tablero (Dashboard con gráfica por día o total)
        panelDashboard = new PanelDashboard();
        contenedor.add(panelDashboard, "dashboard");

        cardLayout.show(contenedor, "inicio");
    }

    private void salir() {
        int r = JOptionPane.showConfirmDialog(this,
                "¿Seguro que deseas salir del sistema?",
                "Salir", JOptionPane.YES_NO_OPTION);
        if (r != JOptionPane.YES_OPTION) {
            return;
        }
        cerrarSistema();
    }

    private void cerrarSistema() {
        try {
            Sesion.getInstancia().cerrar();
        } catch (Exception ex) {
            System.err.println("Error al cerrar la BD: " + ex.getMessage());
        }

        dispose();
        System.exit(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuPrincipal().setVisible(true));
    }
}
