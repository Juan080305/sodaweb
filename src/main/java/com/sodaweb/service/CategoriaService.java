package com.sodaweb.service;

import com.sodaweb.domain.Categoria;
import com.sodaweb.repository.CategoriaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoriaService {

    // Las últimas versiones de Spring, recomiendan utilizar final y constructor en lugar de @Autowired
    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional(readOnly = true)
    public List<Categoria> getCategorias(boolean activo) {
        if (activo) { // Solo las categorías activas se muestran en el menú público
            return categoriaRepository.findByActivoTrue();
        }
        return categoriaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Categoria> getCategoria(Integer idCategoria) {
        return categoriaRepository.findById(idCategoria);
    }

    @Transactional
    public void save(Categoria categoria) {
        // Se eliminan espacios sobrantes para evitar descripciones "repetidas"
        categoria.setDescripcion(categoria.getDescripcion().trim());
        if (categoria.getRutaImagen() != null && categoria.getRutaImagen().isBlank()) {
            categoria.setRutaImagen(null);
        }
        categoriaRepository.save(categoria);
    }

    @Transactional
    public void delete(Integer idCategoria) {
        // Verifica si la categoría existe antes de intentar eliminarla
        if (!categoriaRepository.existsById(idCategoria)) {
            // Lanza una excepción para indicar que la categoría no fue encontrada
            throw new IllegalArgumentException("La categoría con ID " + idCategoria + " no existe.");
        }
        try {
            categoriaRepository.deleteById(idCategoria);
            categoriaRepository.flush();
        } catch (DataIntegrityViolationException e) {
            // Lanza una nueva excepción para encapsular el problema de integridad de datos
            throw new IllegalStateException("No se puede eliminar la categoría. Tiene datos asociados.", e);
        }
    }
}
