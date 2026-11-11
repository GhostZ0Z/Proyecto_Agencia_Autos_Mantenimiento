/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import modelo.Marca;
import modelo.Modelo;
import util.ConexionDB;

public class ModeloDAO implements CrudDAO<Modelo> {

    private Connection con() throws SQLException {
        return ConexionDB.getInstancia().getConexion();
    }

    private Modelo mapear(ResultSet rs) throws SQLException {
        Marca m = new Marca(rs.getInt("marca_id"), rs.getString("marca_nombre"));
        return new Modelo(rs.getInt("id"), m, rs.getString("nombre"));
    }

    @Override
    public List<Modelo> listar() throws SQLException {
        List<Modelo> lista = new ArrayList<>();
        String sql = "SELECT mo.id, mo.marca_id, mo.nombre, ma.nombre AS marca_nombre "
                + "FROM modelos mo INNER JOIN marcas ma ON mo.marca_id = ma.id ORDER BY ma.nombre, mo.nombre";
        try (Statement st = con().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    @Override
    public Modelo buscarPorId(int id) throws SQLException {
        String sql = "SELECT mo.id, mo.marca_id, mo.nombre, ma.nombre AS marca_nombre "
                + "FROM modelos mo INNER JOIN marcas ma ON mo.marca_id = ma.id WHERE mo.id = ?";
        try (PreparedStatement ps = con().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    @Override
    public boolean insertar(Modelo m) throws SQLException {
        try (PreparedStatement ps = con().prepareStatement(
                "INSERT INTO modelos (marca_id, nombre) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, m.getMarca().getId());
            ps.setString(2, m.getNombre());
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
    public boolean actualizar(Modelo m) throws SQLException {
        try (PreparedStatement ps = con().prepareStatement("UPDATE modelos SET marca_id = ?, nombre = ? WHERE id = ?")) {
            ps.setInt(1, m.getMarca().getId());
            ps.setString(2, m.getNombre());
            ps.setInt(3, m.getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean eliminar(int id) throws SQLException {
        try (PreparedStatement ps = con().prepareStatement("DELETE FROM modelos WHERE id = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}
