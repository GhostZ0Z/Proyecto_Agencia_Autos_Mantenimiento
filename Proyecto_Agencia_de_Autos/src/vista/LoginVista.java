/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

import controlador.LoginControlador;
import java.awt.GridLayout;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import modelo.Usuario;

/** VISTA (la V de MVC): solo dibuja la pantalla y le pasa los datos al controlador. */
public class LoginVista extends JFrame {

    private final JTextField txtUsuario = new JTextField(15);
    private final JPasswordField txtPassword = new JPasswordField(15);
    private final JButton btnEntrar = new JButton("Entrar");
    private final LoginControlador controlador = new LoginControlador();

    public LoginVista() {
        super("Agencia de Automóviles - Acceso de administradores");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(3, 2, 8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.add(new JLabel("Usuario:"));
        panel.add(txtUsuario);
        panel.add(new JLabel("Contraseña:"));
        panel.add(txtPassword);
        panel.add(new JLabel());
        panel.add(btnEntrar);
        add(panel);

        getRootPane().setDefaultButton(btnEntrar);   // Enter = clic en "Entrar"
        btnEntrar.addActionListener(e -> entrar());

        pack();
        setResizable(false);
        setLocationRelativeTo(null);
    }

    private void entrar() {
        try {
            Usuario u = controlador.iniciarSesion(txtUsuario.getText(),
                    new String(txtPassword.getPassword()));
            if (u == null) {
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.",
                        "Acceso denegado", JOptionPane.ERROR_MESSAGE);
                txtPassword.setText("");
            } else {
                // TODO (Alan): aquí se abre el MenuPrincipal y se cierra el login con dispose().
                JOptionPane.showMessageDialog(this, "Bienvenido, " + u.getNombre());
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Faltan datos", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "No se pudo conectar con la base de datos:\n" + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
    }
}
