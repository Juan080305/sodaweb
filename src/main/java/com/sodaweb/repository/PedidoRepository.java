package com.sodaweb.repository;

import com.sodaweb.domain.Pedido;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    //Consulta derivada con "After": pedidos realizados después de una fecha, los más recientes primero (HU-05)
    public List<Pedido> findByFechaAfterOrderByFechaDesc(LocalDateTime fecha);

    //Consulta derivada: pedidos de un estado realizados después de una fecha, en orden de llegada (HU-05)
    public List<Pedido> findByEstadoAndFechaAfterOrderByFechaAsc(String estado, LocalDateTime fecha);
}
