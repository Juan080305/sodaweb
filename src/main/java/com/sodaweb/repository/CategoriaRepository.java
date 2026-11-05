package com.sodaweb.repository;
import com.sodaweb.domain.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface CategoriaRepository extends JpaRepository<Categoria,Long> {
 boolean existsByNombreIgnoreCase(String nombre);
 boolean existsByNombreIgnoreCaseAndIdNot(String nombre,Long id);
 List<Categoria> findByActivaTrueOrderByNombreAsc();
}
