package com.sodaweb.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import lombok.*;

@Data
@Entity
@Table(name = "categoria")
public class Categoria implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Integer idCategoria;

    @Column(unique = true, nullable = false, length = 50)
    @NotBlank(message = "La descripción no puede estar vacía.")
    @Size(max = 50, message = "La descripción no puede tener más de 50 caracteres.")
    private String descripcion;

    @Column(name = "ruta_imagen", length = 1024)
    @Size(max = 1024)
    private String rutaImagen;

    @Column(nullable = false)
    private boolean activo = true;

    // Relación de uno a muchos con la clase Producto
    // Sin "cascade" ni "orphanRemoval" para evitar la propagación de operaciones.
    // Se excluye de toString/hashCode para evitar un ciclo infinito Categoria <-> Producto
    @OneToMany(mappedBy = "categoria")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Producto> productos;
}
