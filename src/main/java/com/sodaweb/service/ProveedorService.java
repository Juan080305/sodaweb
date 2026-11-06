package com.sodaweb.service;

import com.sodaweb.domain.Proveedor;
import com.sodaweb.repository.ProveedorRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProveedorService {

    // Las últimas versiones de Spring, recomiendan utilizar final y constructor en lugar de @Autowired
    private final ProveedorRepository proveedorRepository;

    public ProveedorService(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    @Transactional(readOnly = true)
    public List<Proveedor> getProveedores() {
        return proveedorRepository.findAllByOrderByNombreAsc();
    }

    @Transactional(readOnly = true)
    public Optional<Proveedor> getProveedor(Integer idProveedor) {
        return proveedorRepository.findById(idProveedor);
    }

    // HU-13: búsqueda del proveedor por medio de su nombre
    @Transactional(readOnly = true)
    public List<Proveedor> buscar(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            return getProveedores();
        }
        return proveedorRepository.consultaSQL(nombre.trim());
    }

    @Transactional
    public void save(Proveedor proveedor) {
        proveedor.setNombre(proveedor.getNombre().trim());
        proveedor.setContacto(proveedor.getContacto().trim());
        if (proveedor.getCorreo() != null && proveedor.getCorreo().isBlank()) {
            proveedor.setCorreo(null);
        }
        proveedorRepository.save(proveedor);
    }

    @Transactional
    public void delete(Integer idProveedor) {
        // Verifica si el proveedor existe antes de intentar eliminarlo
        if (!proveedorRepository.existsById(idProveedor)) {
            throw new IllegalArgumentException("El proveedor con ID " + idProveedor + " no existe.");
        }
        try {
            proveedorRepository.deleteById(idProveedor);
            proveedorRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("No se puede eliminar el proveedor. Tiene datos asociados.", e);
        }
    }
}
