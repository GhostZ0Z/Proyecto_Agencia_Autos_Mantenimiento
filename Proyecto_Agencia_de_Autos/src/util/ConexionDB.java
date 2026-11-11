/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 * PATRÓN 1: SINGLETON.
 * Garantiza que en todo el programa exista UNA sola instancia de ConexionDB,
 * y por lo tanto UNA sola conexión abierta a MariaDB que todos los DAO comparten.
 *
 * Cómo se logra:
 *  1) El constructor es private: nadie puede hacer "new ConexionDB()".
 *  2) La única instancia se guarda en un atributo static.
 *  3) getInstancia() crea la instancia la primera vez y después siempre devuelve la misma.
 *
 * Los datos de conexión se leen de un archivo "db.properties" (cada integrante del equipo
 * tiene el suyo, no se sube a GitHub). Si no existe, usa valores por defecto.
 */
package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {

    private static ConexionDB instancia;
    private Connection conexion;

    private final String URL =
        "jdbc:mariadb://localhost:3306/agencia_autos";
    private final String USER = "root";
    private final String PASSWORD = "0101";

    private ConexionDB() {
        // La conexión se abre en getConexion(), cuando realmente se necesita.
    }

    public static synchronized ConexionDB getInstancia() {
        if (instancia == null) {
            instancia = new ConexionDB();
        }
        return instancia;
    }

    public Connection getConexion() throws SQLException {
        try {
            if (conexion == null || conexion.isClosed()) {
                Class.forName("org.mariadb.jdbc.Driver");
                conexion = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Conexion exitosa con MariaDB");
            }
        } catch (ClassNotFoundException e) {
            throw new SQLException("No se encontró el driver de MariaDB", e);
        }
        return conexion;
    }

    /** Lo usa Sesion.cerrar() para cerrar la BD al salir del sistema. */
    public void cerrar() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
            }
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexion: " + e.getMessage());
        }
        conexion = null;
    }
}