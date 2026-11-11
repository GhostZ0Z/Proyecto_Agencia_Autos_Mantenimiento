/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import vista.LoginVista;

/** Punto de entrada del programa: abre la pantalla de login. */
public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Si falla, se queda con el aspecto por defecto de Swing.
        }
        SwingUtilities.invokeLater(() -> new LoginVista().setVisible(true));
    }
}

