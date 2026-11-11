/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import modelo.Marca;
import util.ConexionDB;

/**
 * DAO de ejemplo COMPLETO para un catálogo. Alan: copia este archivo como molde para
 * ModeloDAO y AnioDAO (cambiando tabla, columnas y la entidad). Los DAO de Servicios,
 * Vehículos, etc. siguen exactamente la misma estructura.
 */
public class MarcaDAO implements CrudDAO<Marca> {

    private Connection con() throws SQLException {
        return ConexionDB.getInstancia().getConexion();
    }

    private Marca mapear(ResultSet rs) throws SQLException {
        return new Marca(rs.getInt("id"), rs.getString("nombre"));
    }

    @Override
    public List<Marca> listar() throws SQLException {
        List<Marca> lista = new ArrayList<>();
        try (Statement st = con().createStatement();
             ResultSet rs = st.executeQuery("SELECT id, nombre FROM marcas ORDER BY nombre")) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    @Override
    public Marca buscarPorId(int id) throws SQLException {
        try (PreparedStatement ps = con().prepareStatement("SELECT id, nombre FROM marcas WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    @Override
    public boolean insertar(Marca m) throws SQLException {
        try (PreparedStatement ps = con().prepareStatement(
                "INSERT INTO marcas (nombre) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, m.getNombre());
            boolean ok = ps.executeUpdate() > 0;
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    m.setId(keys.getInt(1));
                }
            }
            return ok;
        }
    }

    @Override
    public boolean actualizar(Marca m) throws SQLException {
        try (PreparedStatement ps = con().prepareStatement("UPDATE marcas SET nombre = ? WHERE id = ?")) {
            ps.setString(1, m.getNombre());
            ps.setInt(2, m.getId());
            return ps.executeUpdate() > 0;
        }
    }

    /** Falla con SQLException si hay modelos que usan esta marca (llave foránea). */
    @Override
    public boolean eliminar(int id) throws SQLException {
        try (PreparedStatement ps = con().prepareStatement("DELETE FROM marcas WHERE id = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}

