/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package DAO;

import java.sql.SQLException;
import java.util.List;

/**
 * Contrato común de todos los DAO. Cada entidad (Marca, Modelo, Servicio...) tendrá su DAO
 * que implementa estas 5 operaciones, que son el CRUD:
 * Create (insertar), Read (listar/buscarPorId), Update (actualizar), Delete (eliminar).
 *
 * @param <T> la entidad con la que trabaja el DAO
 */
public interface CrudDAO<T> {

    List<T> listar() throws SQLException;

    T buscarPorId(int id) throws SQLException;

    boolean insertar(T obj) throws SQLException;

    boolean actualizar(T obj) throws SQLException;

    boolean eliminar(int id) throws SQLException;
}
