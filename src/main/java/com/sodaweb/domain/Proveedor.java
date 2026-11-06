package com.sodaweb.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import lombok.*;

@Data
@Entity
@Table(name = "proveedor")
public class Proveedor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proveedor")
    private Integer idProveedor;

    @Column(unique = true, nullable = false, length = 60)
    @NotBlank(message = "El nombre no puede estar vacío.")
    @Size(max = 60, message = "El nombre no puede tener más de 60 caracteres.")
    private String nombre;

    @Column(nullable = false, length = 60)
    @NotBlank(message = "El contacto no puede estar vacío.")
    @Size(max = 60, message = "El contacto no puede tener más de 60 caracteres.")
    private String contacto;

    @Column(nullable = false, length = 25)
    @NotBlank(message = "El teléfono no puede estar vacío.")
    @Pattern(regexp = "[0-9 +-]{8,25}", message = "El teléfono debe tener al menos 8 dígitos.")
    private String telefono;

    @Column(length = 75)
    @Email(message = "El correo no tiene un formato válido.")
    @Size(max = 75)
    private String correo;

    // Productos que suministra a la soda, por ejemplo: "Natilla, queso tierno"
    @Column(length = 255)
    @Size(max = 255)
    private String productos;

    @Column(nullable = false)
    private boolean activo = true;
}
