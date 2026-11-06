package com.sodaweb.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.*;

@Data
@Entity
@Table(name = "pedido")
public class Pedido implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido")
    private Integer idPedido;

    private LocalDateTime fecha;

    @Column(name = "nombre_cliente", nullable = false, length = 60)
    @NotBlank(message = "El nombre no puede estar vacío.")
    @Size(max = 60, message = "El nombre no puede tener más de 60 caracteres.")
    private String nombreCliente;

    @Column(nullable = false, length = 25)
    @NotBlank(message = "El teléfono no puede estar vacío.")
    @Pattern(regexp = "[0-9 +-]{8,25}", message = "El teléfono debe tener al menos 8 dígitos.")
    private String telefono;

    @Column(length = 255)
    @Size(max = 255, message = "Las observaciones no pueden tener más de 255 caracteres.")
    private String observaciones;

    // Efectivo, SINPE o Tarjeta: se paga al retirar, el sistema no procesa pagos en línea
    @Column(name = "metodo_pago", nullable = false)
    @NotBlank(message = "Debe seleccionar el método de pago.")
    private String metodoPago;

    // Recibido -> Preparacion -> Listo -> Entregado (HU-06)
    @Column(nullable = false)
    private String estado;

    @Column(precision = 12, scale = 2)
    private BigDecimal total;

    // Relación de uno a muchos con el detalle, sin "cascade" (cada detalle se guarda en el servicio)
    @OneToMany(mappedBy = "pedido")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<DetallePedido> detalles;

    // Código que se le muestra al cliente, por ejemplo SW-0007
    public String getCodigo() {
        return idPedido == null ? "" : String.format("SW-%04d", idPedido);
    }

    // HU-06: el estado solo avanza, nunca retrocede. Devuelve null si ya fue entregado.
    public String getSiguienteEstado() {
        if (estado == null) {
            return null;
        }
        return switch (estado) {
            case "Recibido" -> "Preparacion";
            case "Preparacion" -> "Listo";
            case "Listo" -> "Entregado";
            default -> null;
        };
    }
}
