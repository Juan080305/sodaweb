package com.sodaweb.service;
import com.sodaweb.domain.Categoria;
import com.sodaweb.repository.CategoriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service public class CategoriaService {
 private final CategoriaRepository repo;
 public CategoriaService(CategoriaRepository repo){this.repo=repo;}
 @Transactional(readOnly=true) public List<Categoria> listar(){return repo.findAll();}
 @Transactional(readOnly=true) public List<Categoria> listarActivas(){return repo.findByActivaTrueOrderByNombreAsc();}
 @Transactional(readOnly=true) public Categoria buscar(Long id){return repo.findById(id).orElseThrow(()->new IllegalArgumentException("Categoría no encontrada"));}
 @Transactional public Categoria guardar(Categoria categoria){
  String nombre=categoria.getNombre()==null?"":categoria.getNombre().trim();
  if(nombre.isEmpty()) throw new IllegalArgumentException("El nombre es obligatorio");
  boolean repetido=categoria.getId()==null?repo.existsByNombreIgnoreCase(nombre):repo.existsByNombreIgnoreCaseAndIdNot(nombre,categoria.getId());
  if(repetido) throw new IllegalArgumentException("Ya existe una categoría con ese nombre");
  categoria.setNombre(nombre);return repo.save(categoria);
 }
 // Baja lógica: conserva el identificador para productos que se relacionen después.
 @Transactional public void desactivar(Long id){Categoria c=buscar(id);c.setActiva(false);repo.save(c);}
 @Transactional public void activar(Long id){Categoria c=buscar(id);c.setActiva(true);repo.save(c);}
}
