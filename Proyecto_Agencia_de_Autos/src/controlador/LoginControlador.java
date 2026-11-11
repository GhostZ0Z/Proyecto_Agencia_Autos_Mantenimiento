/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import dao.UsuarioDAO;
import java.sql.SQLException;
import modelo.Usuario;
import util.Sesion;

/**
 * CONTROLADOR (la C de MVC): recibe lo que captura la vista, valida, pide los datos al DAO
 * y decide qué pasa. La vista no habla con la BD directamente.
 */
public class LoginControlador {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    /**
     * @return el Usuario si las credenciales son correctas (y deja la sesión iniciada), o null si no.
     * @throws IllegalArgumentException si falta capturar usuario o contraseña
     * @throws SQLException si hay un problema con la base de datos
     */
    public Usuario iniciarSesion(String usuario, String password) throws SQLException {
        if (usuario == null || usuario.trim().isEmpty() || password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Captura tu usuario y contraseña.");
        }
        Usuario u = usuarioDAO.autenticar(usuario.trim(), password);
        if (u != null) {
            Sesion.getInstancia().iniciar(u);
        }
        return u;
    }
}

