package com.sodaweb.repository;

import com.sodaweb.domain.Usuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    //Consulta derivada usada al iniciar sesión: solo usuarios activos
    public Optional<Usuario> findByUsernameAndActivoTrue(String username);

    public List<Usuario> findAllByOrderByUsernameAsc();
}
