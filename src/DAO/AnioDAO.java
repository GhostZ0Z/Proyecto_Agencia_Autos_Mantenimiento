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
import modelo.Anio;
import util.ConexionDB;

public class AnioDAO implements CrudDAO<Anio>{
    

    private Connection con() throws SQLException {
        return ConexionDB.getInstancia().getConexion();
    }

    private Anio mapear(ResultSet rs) throws SQLException {
        return new Anio(rs.getInt("id"), rs.getInt("anio"));
    }

    @Override
    public List<Anio> listar() throws SQLException {
        List<Anio> lista = new ArrayList<>();
        try (Statement st = con().createStatement();
             ResultSet rs = st.executeQuery("SELECT id, anio FROM anios ORDER BY anio")) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    @Override
    public Anio buscarPorId(int id) throws SQLException {
        try (PreparedStatement ps = con().prepareStatement("SELECT id, anio FROM anios WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    @Override
    public boolean insertar(Anio a) throws SQLException {
        try (PreparedStatement ps = con().prepareStatement(
                "INSERT INTO anios (anio) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, a.getAnio());
            boolean ok = ps.executeUpdate() > 0;
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    a.setId(keys.getInt(1));
                }
            }
            return ok;
        }
    }

    @Override
    public boolean actualizar(Anio a) throws SQLException {
        try (PreparedStatement ps = con().prepareStatement("UPDATE anios SET anio = ? WHERE id = ?")) {
            ps.setInt(1, a.getAnio());
            ps.setInt(2, a.getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean eliminar(int id) throws SQLException {
        try (PreparedStatement ps = con().prepareStatement("DELETE FROM anios WHERE id = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}
