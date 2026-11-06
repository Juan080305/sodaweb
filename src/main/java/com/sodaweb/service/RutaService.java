package com.sodaweb.service;

import com.sodaweb.domain.Ruta;
import com.sodaweb.repository.RutaRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RutaService {

    private final RutaRepository rutaRepository;

    public RutaService(RutaRepository rutaRepository) {
        this.rutaRepository = rutaRepository;
    }

    // Las rutas se leen de la base de datos para armar los permisos en ProjectConfig
    @Transactional(readOnly = true)
    public List<Ruta> getRutas() {
        return rutaRepository.findAll();
    }
}
