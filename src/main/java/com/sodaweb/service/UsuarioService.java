package com.sodaweb.service;

import com.sodaweb.domain.Rol;
import com.sodaweb.domain.Usuario;
import com.sodaweb.repository.RolRepository;
import com.sodaweb.repository.UsuarioRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, RolRepository rolRepository,
            PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<Usuario> getUsuarios() {
        return usuarioRepository.findAllByOrderByUsernameAsc();
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> getUsuario(Integer idUsuario) {
        return usuarioRepository.findById(idUsuario);
    }

    /* HU-14: se registra el usuario con su tipo de rol y su contraseña cifrada.
       Al modificar, si la contraseña viene vacía se conserva la anterior. */
    @Transactional
    public void save(Usuario usuario, Integer idRol, String clave) {
        usuario.setUsername(usuario.getUsername().trim());
        usuario.setNombre(usuario.getNombre().trim());
        boolean claveNueva = clave != null && !clave.isBlank();
        if (claveNueva && clave.length() < 6) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres.");
        }
        if (usuario.getIdUsuario() == null) {
            if (!claveNueva) {
                throw new IllegalArgumentException("La contraseña es obligatoria para un usuario nuevo.");
            }
            usuario.setPassword(passwordEncoder.encode(clave));
        } else {
            Usuario actual = usuarioRepository.findById(usuario.getIdUsuario())
                    .orElseThrow(() -> new IllegalArgumentException("El usuario no existe."));
            usuario.setPassword(claveNueva ? passwordEncoder.encode(clave) : actual.getPassword());
        }
        Rol rol = rolRepository.findById(idRol)
                .orElseThrow(() -> new IllegalArgumentException("El rol no existe."));
        var roles = new ArrayList<Rol>();
        roles.add(rol);
        // El administrador también puede atender los pedidos del día
        if ("ADMIN".equals(rol.getRol())) {
            rolRepository.findByRol("ENCARGADO").ifPresent(roles::add);
        }
        usuario.setRoles(roles);
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void delete(Integer idUsuario, String usernameActual) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("El usuario con ID " + idUsuario + " no existe."));
        if (usuario.getUsername().equals(usernameActual)) {
            throw new IllegalStateException("No puede eliminar su propio usuario.");
        }
        try {
            usuarioRepository.delete(usuario);
            usuarioRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("No se puede eliminar el usuario. Tiene datos asociados.", e);
        }
    }
}
