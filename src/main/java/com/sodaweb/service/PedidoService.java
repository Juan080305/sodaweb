package com.sodaweb.service;

import com.sodaweb.domain.DetallePedido;
import com.sodaweb.domain.Item;
import com.sodaweb.domain.Pedido;
import com.sodaweb.domain.Producto;
import com.sodaweb.repository.DetallePedidoRepository;
import com.sodaweb.repository.PedidoRepository;
import com.sodaweb.repository.ProductoRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detallePedidoRepository;
    private final ProductoRepository productoRepository;

    public PedidoService(PedidoRepository pedidoRepository,
            DetallePedidoRepository detallePedidoRepository,
            ProductoRepository productoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.detallePedidoRepository = detallePedidoRepository;
        this.productoRepository = productoRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Pedido> getPedido(Integer idPedido) {
        return pedidoRepository.findById(idPedido);
    }

    // HU-05: pedidos del día, todos o solo los de un estado
    @Transactional(readOnly = true)
    public List<Pedido> getPedidosHoy(String estado) {
        LocalDateTime inicioDelDia = LocalDate.now().atStartOfDay();
        if (estado == null || estado.isBlank()) {
            return pedidoRepository.findByFechaAfterOrderByFechaDesc(inicioDelDia);
        }
        return pedidoRepository.findByEstadoAndFechaAfterOrderByFechaAsc(estado, inicioDelDia);
    }

    // HU-06: avanza el pedido al siguiente estado; no se permite retroceder
    @Transactional
    public Pedido avanzarEstado(Integer idPedido) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new IllegalArgumentException("El pedido con ID " + idPedido + " no existe."));
        String siguiente = pedido.getSiguienteEstado();
        if (siguiente == null) {
            throw new IllegalStateException("El pedido " + pedido.getCodigo() + " ya fue entregado.");
        }
        pedido.setEstado(siguiente);
        return pedidoRepository.save(pedido);
    }

    /* HU-04: registra el pedido y su detalle en una sola transacción.
       Si algún producto ya no tiene existencias, no se guarda nada (rollback). */
    @Transactional
    public Pedido registrar(Pedido pedido, List<Item> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("El pedido debe tener al menos un producto.");
        }
        // Los datos que no vienen del cliente se definen aquí
        pedido.setIdPedido(null);
        pedido.setFecha(LocalDateTime.now());
        pedido.setEstado("Recibido");
        pedido.setNombreCliente(pedido.getNombreCliente().trim());
        pedido.setTelefono(pedido.getTelefono().trim());
        if (pedido.getObservaciones() != null && pedido.getObservaciones().isBlank()) {
            pedido.setObservaciones(null);
        }

        var total = BigDecimal.ZERO;
        for (Item item : items) {
            Producto producto = productoRepository.findById(item.getIdProducto())
                    .orElseThrow(() -> new IllegalStateException("Un producto del pedido ya no existe."));
            if (!producto.isActivo() || producto.getExistencias() < item.getCantidad()) {
                throw new IllegalStateException("No hay suficientes existencias de " + producto.getDescripcion() + ".");
            }
            // Se usa el precio actual de la base de datos, no el que quedó en la sesión
            total = total.add(producto.getPrecio().multiply(BigDecimal.valueOf(item.getCantidad())));
        }
        pedido.setTotal(total);
        pedido = pedidoRepository.save(pedido);

        for (Item item : items) {
            Producto producto = productoRepository.findById(item.getIdProducto()).get();
            var detalle = new DetallePedido();
            detalle.setPedido(pedido);
            detalle.setProducto(producto);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(producto.getPrecio());
            detalle.setSubtotal(producto.getPrecio().multiply(BigDecimal.valueOf(item.getCantidad())));
            detallePedidoRepository.save(detalle);
            // Se rebajan las existencias para que el menú y las alertas de stock estén al día
            producto.setExistencias(producto.getExistencias() - item.getCantidad());
            productoRepository.save(producto);
        }
        return pedido;
    }
}
