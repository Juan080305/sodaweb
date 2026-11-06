package com.sodaweb.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import lombok.*;

@Data
@Entity
@Table(name = "rol")
public class Rol implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private Integer idRol;

    // ADMIN o ENCARGADO
    @Column(unique = true, nullable = false, length = 25)
    private String rol;
}
