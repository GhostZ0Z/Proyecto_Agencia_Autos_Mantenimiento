/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package app;

import util.ConexionDB;
import java.sql.Connection;
import java.sql.SQLException;

public class PruebaConexion {

    public static void main(String[] args) throws SQLException {

        Connection con =
            ConexionDB.getInstancia().getConexion();

        if (con != null) {
            System.out.println("BASE DE DATOS CONECTADA");

        } else {
            System.out.println("NO SE PUDO CONECTAR");
        }
    }
}
