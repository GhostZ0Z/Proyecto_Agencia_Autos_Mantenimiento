/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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
import modelo.Marca;
import modelo.Modelo;
import modelo.Propietario;
import modelo.Servicio;
import modelo.Usuario;
import modelo.Vehiculo;
import util.ConexionDB;

/**
 * DAO para la gestión de servicios de mantenimiento (registro, modificación,
 * eliminación, consulta por folio/dueño y cambio de estatus).
 */
public class ServicioDAO implements CrudDAO<Servicio> {

    private static final String SQL_SELECT_BASE =
            "SELECT s.id, s.folio, s.persona_entrega, s.tipo, s.descripcion, s.estatus, "
            + "s.fecha_ingreso, s.proximo_servicio, s.usuario_id, "
            + "v.id AS vehiculo_id, v.placas, "
            + "p.id AS propietario_id, p.nombre AS propietario_nombre, p.telefono AS propietario_telefono, "
            + "mo.id AS modelo_id, mo.nombre AS modelo_nombre, "
            + "ma.id AS marca_id, ma.nombre AS marca_nombre, "
            + "a.id AS anio_id, a.anio AS anio_valor "
            + "FROM servicios s "
            + "INNER JOIN vehiculos v ON s.vehiculo_id = v.id "
            + "INNER JOIN propietarios p ON v.propietario_id = p.id "
            + "INNER JOIN modelos mo ON v.modelo_id = mo.id "
            + "INNER JOIN marcas ma ON mo.marca_id = ma.id "
            + "INNER JOIN anios a ON v.anio_id = a.id ";

    private Connection con() throws SQLException {
        return ConexionDB.getInstancia().getConexion();
    }

    private Servicio mapear(ResultSet rs) throws SQLException {
        Marca marca = new Marca(rs.getInt("marca_id"), rs.getString("marca_nombre"));
        Modelo modelo = new Modelo(rs.getInt("modelo_id"), marca, rs.getString("modelo_nombre"));
        Anio anio = new Anio(rs.getInt("anio_id"), rs.getInt("anio_valor"));
        Propietario propietario = new Propietario(
                rs.getInt("propietario_id"),
                rs.getString("propietario_nombre"),
                rs.getString("propietario_telefono")
        );
        Vehiculo vehiculo = new Vehiculo(
                rs.getInt("vehiculo_id"),
                propietario,
                modelo,
                anio,
                rs.getString("placas")
        );
        Usuario usuario = new Usuario();
        usuario.setId(rs.getInt("usuario_id"));

        return new Servicio(
                rs.getInt("id"),
                rs.getString("folio"),
                vehiculo,
                usuario,
                rs.getString("persona_entrega"),
                rs.getString("tipo"),
                rs.getString("descripcion"),
                rs.getString("estatus"),
                rs.getDate("fecha_ingreso"),
                rs.getDate("proximo_servicio")
        );
    }

    @Override
    public List<Servicio> listar() throws SQLException {
        List<Servicio> lista = new ArrayList<>();
        try (Statement st = con().createStatement();
             ResultSet rs = st.executeQuery(SQL_SELECT_BASE + "ORDER BY s.id DESC")) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    @Override
    public Servicio buscarPorId(int id) throws SQLException {
        try (PreparedStatement ps = con().prepareStatement(SQL_SELECT_BASE + "WHERE s.id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    /** Busca servicios filtrando por folio y/o nombre del dueño del vehículo. */
    public List<Servicio> buscarPorFolioODueno(String folio, String dueno) throws SQLException {
        List<Servicio> lista = new ArrayList<>();
        String sql = SQL_SELECT_BASE
                + "WHERE s.folio LIKE ? AND p.nombre LIKE ? ORDER BY s.id DESC";
        try (PreparedStatement ps = con().prepareStatement(sql)) {
            ps.setString(1, "%" + (folio == null ? "" : folio.trim()) + "%");
            ps.setString(2, "%" + (dueno == null ? "" : dueno.trim()) + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        }
        return lista;
    }

    @Override
    public boolean insertar(Servicio s) throws SQLException {
        Connection c = con();
        Propietario p = s.getVehiculo().getPropietario();
        try (PreparedStatement psProp = c.prepareStatement(
                "INSERT INTO propietarios (nombre, telefono) VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {
            psProp.setString(1, p.getNombre());
            psProp.setString(2, p.getTelefono());
            psProp.executeUpdate();
            try (ResultSet keys = psProp.getGeneratedKeys()) {
                if (keys.next()) {
                    p.setId(keys.getInt(1));
                }
            }
        }

        Vehiculo v = s.getVehiculo();
        try (PreparedStatement psVeh = c.prepareStatement(
                "INSERT INTO vehiculos (propietario_id, modelo_id, anio_id, placas) VALUES (?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {
            psVeh.setInt(1, p.getId());
            psVeh.setInt(2, v.getModelo().getId());
            psVeh.setInt(3, v.getAnio().getId());
            psVeh.setString(4, v.getPlacas());
            psVeh.executeUpdate();
            try (ResultSet keys = psVeh.getGeneratedKeys()) {
                if (keys.next()) {
                    v.setId(keys.getInt(1));
                }
            }
        }

        String sqlServ = "INSERT INTO servicios (folio, vehiculo_id, usuario_id, persona_entrega, "
                + "tipo, descripcion, estatus, fecha_ingreso, proximo_servicio) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement psServ = c.prepareStatement(sqlServ, Statement.RETURN_GENERATED_KEYS)) {
            psServ.setString(1, s.getFolio());
            psServ.setInt(2, v.getId());
            psServ.setInt(3, s.getUsuario() != null && s.getUsuario().getId() > 0 ? s.getUsuario().getId() : 1);
            psServ.setString(4, s.getPersonaEntrega());
            psServ.setString(5, s.getTipo());
            psServ.setString(6, s.getDescripcion());
            psServ.setString(7, s.getEstatus());
            psServ.setDate(8, s.getFechaIngreso());
            psServ.setDate(9, s.getProximoServicio());
            boolean ok = psServ.executeUpdate() > 0;
            try (ResultSet keys = psServ.getGeneratedKeys()) {
                if (keys.next()) {
                    s.setId(keys.getInt(1));
                }
            }
            return ok;
        }
    }

    @Override
    public boolean actualizar(Servicio s) throws SQLException {
        Connection c = con();
        Propietario p = s.getVehiculo().getPropietario();
        try (PreparedStatement psProp = c.prepareStatement(
                "UPDATE propietarios SET nombre = ?, telefono = ? WHERE id = ?")) {
            psProp.setString(1, p.getNombre());
            psProp.setString(2, p.getTelefono());
            psProp.setInt(3, p.getId());
            psProp.executeUpdate();
        }

        Vehiculo v = s.getVehiculo();
        try (PreparedStatement psVeh = c.prepareStatement(
                "UPDATE vehiculos SET modelo_id = ?, anio_id = ?, placas = ? WHERE id = ?")) {
            psVeh.setInt(1, v.getModelo().getId());
            psVeh.setInt(2, v.getAnio().getId());
            psVeh.setString(3, v.getPlacas());
            psVeh.setInt(4, v.getId());
            psVeh.executeUpdate();
        }

        String sqlServ = "UPDATE servicios SET folio = ?, persona_entrega = ?, tipo = ?, "
                + "descripcion = ?, estatus = ?, fecha_ingreso = ?, proximo_servicio = ? WHERE id = ?";
        try (PreparedStatement psServ = c.prepareStatement(sqlServ)) {
            psServ.setString(1, s.getFolio());
            psServ.setString(2, s.getPersonaEntrega());
            psServ.setString(3, s.getTipo());
            psServ.setString(4, s.getDescripcion());
            psServ.setString(5, s.getEstatus());
            psServ.setDate(6, s.getFechaIngreso());
            psServ.setDate(7, s.getProximoServicio());
            psServ.setInt(8, s.getId());
            return psServ.executeUpdate() > 0;
        }
    }

    @Override
    public boolean eliminar(int id) throws SQLException {
        try (PreparedStatement ps = con().prepareStatement("DELETE FROM servicios WHERE id = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    /** Cambia el estatus del servicio a 'En espera', 'En proceso' o 'Finalizado'. */
    public boolean actualizarEstatus(int id, String estatus) throws SQLException {
        try (PreparedStatement ps = con().prepareStatement("UPDATE servicios SET estatus = ? WHERE id = ?")) {
            ps.setString(1, estatus);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        }
    }

    public List<Modelo> listarModelos() throws SQLException {
        List<Modelo> lista = new ArrayList<>();
        String sql = "SELECT mo.id, mo.nombre, ma.id AS marca_id, ma.nombre AS marca_nombre "
                + "FROM modelos mo INNER JOIN marcas ma ON mo.marca_id = ma.id ORDER BY ma.nombre, mo.nombre";
        try (Statement st = con().createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Marca m = new Marca(rs.getInt("marca_id"), rs.getString("marca_nombre"));
                lista.add(new Modelo(rs.getInt("id"), m, rs.getString("marca_nombre") + " " + rs.getString("nombre")));
            }
        }
        return lista;
    }

    public List<Anio> listarAnios() throws SQLException {
        List<Anio> lista = new ArrayList<>();
        try (Statement st = con().createStatement();
             ResultSet rs = st.executeQuery("SELECT id, anio FROM anios ORDER BY anio DESC")) {
            while (rs.next()) {
                lista.add(new Anio(rs.getInt("id"), rs.getInt("anio")));
            }
        }
        return lista;
    }

    /**
     * Devuelve un arreglo int[3] con la cantidad de servicios en:
     * [0] = En espera, [1] = En proceso, [2] = Finalizado.
     * Si fechaDia es vacío o null, cuenta el total de los registros; si no, filtra por fecha_ingreso.
     */
    public int[] contarPorEstatus(String fechaDia) throws SQLException {
        int[] conteos = new int[]{0, 0, 0};
        boolean filtrarDia = (fechaDia != null && !fechaDia.trim().isEmpty());
        String sql = "SELECT estatus, COUNT(*) AS total FROM servicios "
                + (filtrarDia ? "WHERE fecha_ingreso = ? " : "")
                + "GROUP BY estatus";
        try (PreparedStatement ps = con().prepareStatement(sql)) {
            if (filtrarDia) {
                ps.setDate(1, java.sql.Date.valueOf(fechaDia.trim()));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String est = rs.getString("estatus");
                    int total = rs.getInt("total");
                    if ("En espera".equalsIgnoreCase(est)) {
                        conteos[0] = total;
                    } else if ("En proceso".equalsIgnoreCase(est)) {
                        conteos[1] = total;
                    } else if ("Finalizado".equalsIgnoreCase(est)) {
                        conteos[2] = total;
                    }
                }
            }
        }
        return conteos;
    }
}
