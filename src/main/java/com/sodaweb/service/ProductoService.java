package com.sodaweb.service;

import com.sodaweb.domain.Producto;
import com.sodaweb.repository.ProductoRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Transactional(readOnly = true)
    public List<Producto> getProductos(boolean activo) {
        if (activo) {
            return productoRepository.findByActivoTrue();
        }
        return productoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Producto> getProducto(Integer idProducto) {
        return productoRepository.findById(idProducto);
    }

    @Transactional
    public void save(Producto producto) {
        producto.setDescripcion(producto.getDescripcion().trim());
        if (producto.getRutaImagen() != null && producto.getRutaImagen().isBlank()) {
            producto.setRutaImagen(null);
        }
        productoRepository.save(producto);
    }

    @Transactional
    public void delete(Integer idProducto) {
        // Verifica si el producto existe antes de intentar eliminarlo
        if (!productoRepository.existsById(idProducto)) {
            // Lanza una excepción para indicar que el producto no fue encontrado
            throw new IllegalArgumentException("El producto con ID " + idProducto + " no existe.");
        }
        try {
            productoRepository.deleteById(idProducto);
            productoRepository.flush();
        } catch (DataIntegrityViolationException e) {
            // Lanza una nueva excepción para encapsular el problema de integridad de datos
            throw new IllegalStateException("No se puede eliminar el producto. Tiene pedidos asociados.", e);
        }
    }

    @Transactional(readOnly = true)
    public List<Producto> consultaBusqueda(String descripcion, Integer idCategoria) {
        return productoRepository.consultaBusqueda(descripcion == null ? "" : descripcion.trim(), idCategoria);
    }

    @Transactional(readOnly = true)
    public List<Producto> consultaAlertas() {
        return productoRepository.consultaAlertas();
    }
}
