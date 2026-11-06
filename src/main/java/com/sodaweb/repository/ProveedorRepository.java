package com.sodaweb.repository;

import com.sodaweb.domain.Proveedor;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProveedorRepository extends JpaRepository<Proveedor, Integer> {

    //Consulta derivada que recupera todos los proveedores ordenados por nombre
    public List<Proveedor> findAllByOrderByNombreAsc();

    //Consulta SQL nativa que busca al proveedor por parte de su nombre (HU-13)
    @Query(nativeQuery = true,
            value = "SELECT * FROM proveedor p WHERE p.nombre LIKE CONCAT('%', :nombre, '%') ORDER BY p.nombre ASC")
    public List<Proveedor> consultaSQL(String nombre);
}
