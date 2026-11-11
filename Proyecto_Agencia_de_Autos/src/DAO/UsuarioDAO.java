/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import modelo.Usuario;
import util.ConexionDB;
import util.Seguridad;

/**
 * PATRÓN 2: DAO (Data Access Object).
 * Toda la comunicación con la tabla "usuarios" vive aquí; el resto del programa
 * (controladores y vistas) trabaja con objetos Usuario y no sabe nada de SQL.
 */
public class UsuarioDAO implements CrudDAO<Usuario> {

    private static final String COLUMNAS = "id, usuario, password_hash, nombre";

    private Connection con() throws SQLException {
        return ConexionDB.getInstancia().getConexion();
    }

    /** Convierte la fila actual del ResultSet en un objeto Usuario. */
    private Usuario mapear(ResultSet rs) throws SQLException {
        return new Usuario(rs.getInt("id"), rs.getString("usuario"),
                rs.getString("password_hash"), rs.getString("nombre"));
    }

    @Override
    public List<Usuario> listar() throws SQLException {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT " + COLUMNAS + " FROM usuarios ORDER BY nombre";
        try (Statement st = con().createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    @Override
    public Usuario buscarPorId(int id) throws SQLException {
        String sql = "SELECT " + COLUMNAS + " FROM usuarios WHERE id = ?";
        try (PreparedStatement ps = con().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    /** OJO: el Usuario debe traer passwordHash ya calculado con Seguridad.sha256(...). */
    @Override
    public boolean insertar(Usuario u) throws SQLException {
        String sql = "INSERT INTO usuarios (usuario, password_hash, nombre) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getUsuario());
            ps.setString(2, u.getPasswordHash());
            ps.setString(3, u.getNombre());
            boolean ok = ps.executeUpdate() > 0;
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    u.setId(keys.getInt(1));
                }
            }
            return ok;
        }
    }

    @Override
    public boolean actualizar(Usuario u) throws SQLException {
        String sql = "UPDATE usuarios SET usuario = ?, password_hash = ?, nombre = ? WHERE id = ?";
        try (PreparedStatement ps = con().prepareStatement(sql)) {
            ps.setString(1, u.getUsuario());
            ps.setString(2, u.getPasswordHash());
            ps.setString(3, u.getNombre());
            ps.setInt(4, u.getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean eliminar(int id) throws SQLException {
        try (PreparedStatement ps = con().prepareStatement("DELETE FROM usuarios WHERE id = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Método propio del login: busca un usuario con ese nombre Y ese hash de contraseña.
     * Devuelve el Usuario si las credenciales son correctas, o null si no lo son.
     */
    public Usuario autenticar(String usuario, String passwordPlano) throws SQLException {
        String sql = "SELECT " + COLUMNAS + " FROM usuarios WHERE usuario = ? AND password_hash = ?";
        try (PreparedStatement ps = con().prepareStatement(sql)) {
            ps.setString(1, usuario);
            ps.setString(2, Seguridad.sha256(passwordPlano));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }
}

