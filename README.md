# SodaWeb

Sistema web de menú y gestión de pedidos para retiro en una soda local.

**Curso:** Aplicaciones Web y Patrones · Universidad Fidélitas · Grupo 10
**Profesor:** Wilberth Molina Pérez
**Integrantes:** Juan José Tijerino · Julián Miranda · Miranda M. · José Sancho

## Descripción
Aplicación web responsiva para que los clientes consulten el menú por categorías, armen su pedido y lo confirmen para retirarlo en el local, eligiendo el método de pago al retirar (efectivo, SINPE Móvil o tarjeta). El encargado gestiona los pedidos y sus estados; el administrador gestiona productos, categorías y alertas de stock.

## Tecnologías
- Java 21 + Spring Boot 3.5 (Spring MVC, Spring Data JPA / Hibernate, Bean Validation)
- Thymeleaf con fragmentos reutilizables e internacionalización (español / inglés)
- Bootstrap 5, Font Awesome y jQuery por medio de webjars
- MySQL 8
- Lombok
- Maven (NetBeans)

## Requisitos de ejecución
1. **JDK 21** y **MySQL 8** instalados.
2. En MySQL Workbench ejecutar `sql/creaTablas.sql`. El script crea la base `sodaweb`, el usuario `usuario_sodaweb` (clave `Soda_Clave.`), las tablas y datos de prueba del menú.
3. Abrir el proyecto en NetBeans (*File → Open Project*), hacer **Clean and Build** y ejecutar `SodaWebApplication`.
4. Abrir `http://localhost` (puerto 80, igual que en clase). Si el puerto está ocupado, cambiar `server.port` en `application.properties`.
5. Las imágenes se registran como URL en la nube (no hay imágenes locales). Si un producto no tiene imagen, se muestra un ícono.

## Estructura (MVC)
```
com.sodaweb
├── ProjectConfig          → plantillas, idioma (LocaleResolver) y messageSource
├── domain                 → Categoria, Producto, Pedido, DetallePedido, Item (carrito)
├── repository             → interfaces JpaRepository con consultas derivadas y JPQL
├── service                → reglas de negocio con @Transactional
└── controller             → Index, Categoria, Producto, Carrito y Pedido
templates
├── general/fragmentos     → head, header, footer, mostrarToast, confirmarEliminar
├── menu, carrito, pedido, producto, categoria → fragmentos.html + vistas
```

## Rutas principales
| Ruta | Rol | Historia |
|---|---|---|
| `/` y `/menu/{idCategoria}` | Cliente | HU-01 |
| `/menu/producto/{idProducto}` | Cliente | HU-02 |
| `/carrito/listado` | Cliente | HU-03, HU-04 |
| `/pedido/recibido/{idPedido}` | Cliente | HU-04 |
| `/pedido/listado`, `/pedido/detalle/{idPedido}` | Encargado | HU-05, HU-06 |
| `/producto/listado` | Administrador | HU-07, HU-08, HU-10 |
| `/producto/alertas` | Administrador | HU-12 |
| `/categoria/listado` | Administrador | HU-09, HU-11 |

## Estado del avance 2
Historias implementadas: **12 de 14 (86 %)**, incluidas todas las de prioridad alta del flujo del cliente y del encargado.

| ID | Historia | Estado |
|---|---|---|
| HU-01 | Consultar el menú digital por categorías | ✅ Implementada |
| HU-02 | Ver el detalle de un producto (agotado no se puede agregar) | ✅ Implementada |
| HU-03 | Agregar productos al carrito y modificar cantidades | ✅ Implementada |
| HU-04 | Confirmar pedido para retiro con nombre, teléfono y método de pago | ✅ Implementada |
| HU-05 | Visualizar los pedidos nuevos y sus detalles | ✅ Implementada (falta restringir por rol, ver HU-14) |
| HU-06 | Actualizar el estado de los pedidos sin retroceder | ✅ Implementada |
| HU-07 | Registrar y actualizar productos, precios y disponibilidad | ✅ Implementada |
| HU-08 | Registrar productos (AF-01) | ✅ Implementada |
| HU-09 | Registrar categoría (AF-02) | ✅ Implementada |
| HU-10 | Modificar productos con búsqueda por nombre y categoría (AF-03) | ✅ Implementada |
| HU-11 | Modificar categoría (AF-04) | ✅ Implementada |
| HU-12 | Alerta de stock con mínimo y tipo de alerta (AF-05) | ✅ Implementada |
| HU-13 | Proveedores (AF-06) | ⏳ Pendiente |
| HU-14 | Roles de usuario e inicio de sesión (AF-07) | ⏳ Pendiente |

**Pendiente para el siguiente avance:** módulo de proveedores (HU-13) y Spring Security con roles Administrador y Encargado (HU-14), que protegerá `/pedido/listado`, `/producto/**` y `/categoria/**`.

## Trabajo por ramas
Ver [ACUERDO_RAMAS.md](ACUERDO_RAMAS.md). Cada historia se trabaja en una rama `feature/...` y se integra a `develop` por Pull Request revisado por otro integrante.
