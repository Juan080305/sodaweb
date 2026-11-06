package com.sodaweb.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import lombok.*;

@Data
@Entity
@Table(name = "ruta")
public class Ruta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ruta")
    private Integer idRuta;

    // Patrón de la ruta, por ejemplo /producto/**
    private String ruta;

    // Rol que puede entrar a la ruta (null si la ruta es pública)
    @ManyToOne
    @JoinColumn(name = "id_rol")
    private Rol rol;

    @Column(name = "requiere_rol")
    private boolean requiereRol;
}
