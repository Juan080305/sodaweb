# SodaWeb

Sistema web de menú y gestión de pedidos para retiro en una soda local.

**Curso:** Aplicaciones Web y Patrones · Universidad Fidélitas · Grupo 10
**Profesor:** Wilberth Molina Pérez
**Integrantes:** Juan José Tijerino · Julián Miranda · Miranda M. · José Sancho

## Descripción
Aplicación web responsiva para que los clientes consulten el menú por categorías, armen su pedido y lo confirmen para retirarlo en el local, eligiendo el método de pago al retirar (efectivo, SINPE Móvil o tarjeta). El encargado gestiona los pedidos y sus estados; el administrador gestiona productos, categorías, alertas de stock, proveedores y usuarios con roles.

## Tecnologías previstas
- Java 17 + Spring Boot (Spring MVC, Spring Data JPA, Spring Security)
- Thymeleaf + Bootstrap 5
- MySQL
- Maven

## Roles
| Rol | Acceso |
|---|---|
| Cliente visitante | Menú, detalle, carrito y confirmación de pedido |
| Encargado | Lista y detalle de pedidos, cambio de estado |
| Administrador | Productos, categorías, alertas de stock, proveedores y usuarios |

## Backlog (Avance 1)
HU-01 a HU-14 con criterios de aceptación y prioridad: ver `docs/SodaWeb_Avance1_Integrado_APA7.docx`.

## Prototipo
Prototipo navegable en Figma generado con `prototipo/SodaWeb_Prototipo_Scripter.js` (Figma → Plugins → Scripter → Run).

## Entidades principales
Usuario, Rol, Categoría, Producto, Pedido, DetallePedido, Proveedor.

## Trabajo por ramas
Ver [ACUERDO_RAMAS.md](ACUERDO_RAMAS.md).
