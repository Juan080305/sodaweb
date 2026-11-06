package com.sodaweb.repository;

import com.sodaweb.domain.Producto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    public List<Producto> findByActivoTrue();

    //Consulta JPQL que busca productos por parte de su descripción y, si se indica, por categoría (HU-10)
    @Query(value = "SELECT p FROM Producto p "
            + "WHERE LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :descripcion, '%')) "
            + "AND (:idCategoria IS NULL OR p.categoria.idCategoria = :idCategoria) "
            + "ORDER BY p.descripcion ASC")
    public List<Producto> consultaBusqueda(String descripcion, Integer idCategoria);

    //Consulta JPQL que recupera los productos que llegaron a su stock mínimo, ordenados por existencias (HU-12)
    @Query(value = "SELECT p FROM Producto p WHERE p.existencias <= p.stockMinimo ORDER BY p.existencias ASC")
    public List<Producto> consultaAlertas();
}
