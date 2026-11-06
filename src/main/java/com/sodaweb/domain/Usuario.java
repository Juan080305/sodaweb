package com.sodaweb.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.List;
import lombok.*;

@Data
@Entity
@Table(name = "usuario")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(unique = true, nullable = false, length = 30)
    @NotBlank(message = "El usuario no puede estar vacío.")
    @Pattern(regexp = "[a-zA-Z0-9._-]{3,30}", message = "El usuario debe tener de 3 a 30 letras o números, sin espacios.")
    private String username;

    // Se guarda cifrada con BCrypt; nunca se muestra en las vistas
    @Column(nullable = false, length = 512)
    @ToString.Exclude
    private String password;

    @Column(nullable = false, length = 60)
    @NotBlank(message = "El nombre no puede estar vacío.")
    @Size(max = 60, message = "El nombre no puede tener más de 60 caracteres.")
    private String nombre;

    @Column(nullable = false)
    private boolean activo = true;

    // Relación de muchos a muchos con Rol por medio de la tabla usuario_rol
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "usuario_rol",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_rol"))
    private List<Rol> roles;

    // Rol que se muestra en el formulario: ADMIN si lo tiene, si no el primero
    public Rol getRolPrincipal() {
        if (roles == null || roles.isEmpty()) {
            return null;
        }
        for (Rol r : roles) {
            if ("ADMIN".equals(r.getRol())) {
                return r;
            }
        }
        return roles.get(0);
    }
}
