package com.sodaweb.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.*;

/* Un Item es un producto dentro del carrito del cliente (HU-03).
   No es una entidad: vive en la sesión hasta que el cliente confirma el pedido. */
@Data
public class Item implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idProducto;
    private String descripcion;
    private BigDecimal precio;
    private Integer existencias;
    private String rutaImagen;
    private int cantidad;

    // El subtotal se recalcula cada vez que cambia la cantidad
    public BigDecimal getSubtotal() {
        return precio.multiply(BigDecimal.valueOf(cantidad));
    }
}
