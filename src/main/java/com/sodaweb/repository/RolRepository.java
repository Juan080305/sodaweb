package com.sodaweb.repository;

import com.sodaweb.domain.Rol;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolRepository extends JpaRepository<Rol, Integer> {

    public Optional<Rol> findByRol(String rol);

    public List<Rol> findAllByOrderByIdRolAsc();
}
