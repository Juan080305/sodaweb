package com.sodaweb.service;

import com.sodaweb.domain.Item;
import com.sodaweb.domain.Producto;
import com.sodaweb.repository.ProductoRepository;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/* El carrito de cada cliente se guarda en su sesión (HU-03), así dos clientes
   que piden al mismo tiempo no comparten el mismo carrito. */
@Service
public class CarritoService {

    private static final String CARRITO = "carrito";

    private final ProductoRepository productoRepository;

    public CarritoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @SuppressWarnings("unchecked")
    public List<Item> getItems(HttpSession session) {
        var items = (List<Item>) session.getAttribute(CARRITO);
        if (items == null) {
            items = new ArrayList<>();
            session.setAttribute(CARRITO, items);
        }
        return items;
    }

    public BigDecimal getTotal(List<Item> items) {
        var total = BigDecimal.ZERO;
        for (Item item : items) {
            total = total.add(item.getSubtotal());
        }
        return total;
    }

    // Solo se agregan productos activos y con existencias (HU-03)
    @Transactional(readOnly = true)
    public void agregar(HttpSession session, Integer idProducto) {
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new IllegalArgumentException("El producto con ID " + idProducto + " no existe."));
        if (!producto.isActivo() || producto.isAgotado()) {
            throw new IllegalStateException("El producto " + producto.getDescripcion() + " no está disponible.");
        }
        var items = getItems(session);
        Item item = buscarItem(items, idProducto);
        if (item == null) {
            item = new Item();
            item.setIdProducto(producto.getIdProducto());
            item.setDescripcion(producto.getDescripcion());
            item.setPrecio(producto.getPrecio());
            item.setRutaImagen(producto.getRutaImagen());
            item.setCantidad(1);
            items.add(item);
        } else if (item.getCantidad() < producto.getExistencias()) {
            item.setCantidad(item.getCantidad() + 1);
        } else {
            throw new IllegalStateException("No hay más existencias de " + producto.getDescripcion() + ".");
        }
        item.setExistencias(producto.getExistencias());
    }

    // Ninguna cantidad puede ser menor que uno ni mayor que las existencias (HU-03)
    @Transactional(readOnly = true)
    public void actualizar(HttpSession session, Integer idProducto, int cantidad) {
        var items = getItems(session);
        Item item = buscarItem(items, idProducto);
        if (item == null) {
            throw new IllegalArgumentException("El producto con ID " + idProducto + " no está en el pedido.");
        }
        if (cantidad < 1) {
            throw new IllegalArgumentException("La cantidad no puede ser menor que uno.");
        }
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new IllegalArgumentException("El producto con ID " + idProducto + " no existe."));
        if (cantidad > producto.getExistencias()) {
            throw new IllegalStateException("Solo hay " + producto.getExistencias() + " unidades de " + producto.getDescripcion() + ".");
        }
        item.setCantidad(cantidad);
        item.setExistencias(producto.getExistencias());
    }

    public void eliminar(HttpSession session, Integer idProducto) {
        var items = getItems(session);
        items.removeIf(i -> i.getIdProducto().equals(idProducto));
    }

    public void vaciar(HttpSession session) {
        session.removeAttribute(CARRITO);
    }

    private Item buscarItem(List<Item> items, Integer idProducto) {
        for (Item item : items) {
            if (item.getIdProducto().equals(idProducto)) {
                return item;
            }
        }
        return null;
    }

    // Número de unidades del pedido, se muestra en el encabezado de todas las páginas
    public int getCantidad(HttpSession session) {
        int cantidad = 0;
        for (Item item : getItems(session)) {
            cantidad += item.getCantidad();
        }
        return cantidad;
    }
}
