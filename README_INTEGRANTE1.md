# SodaWeb — Integrante 1

Proyecto Maven Java 17 / Spring Boot 3.3.5. Contiene la estructura inicial y el CRUD de categorías (HU-09 y HU-11 según reparto del equipo).

## Requisitos
- JDK 17 o superior (compila para Java 17)
- Apache NetBeans con soporte Maven
- MySQL 8 en ejecución
- Maven (NetBeans puede usar su Maven integrado)

## Ejecución
1. Crear la base de datos `sodaweb` en MySQL o dejar que la URL la cree si el usuario tiene permisos.
2. Configurar `DB_USER`, `DB_PASSWORD`, `DB_URL` como variables de entorno si difieren de los valores locales por defecto. **No subir contraseñas a GitHub**.
3. En NetBeans: File > Open Project, seleccionar la carpeta que contiene `pom.xml`.
4. Ejecutar el proyecto y abrir `http://localhost:8080/admin/categorias`.
5. Crear, editar, desactivar y reactivar categorías. Revisar tabla `categorias` en MySQL.

## Contrato de integración
- Paquete raíz: `com.sodaweb`.
- Entidad compartida: `com.sodaweb.domain.Categoria`, tabla `categorias`, PK `id` Long, `nombre` único, `descripcion`, `activa`.
- Integrante 2: en `Producto`, usar `@ManyToOne` con `@JoinColumn(name="categoria_id", nullable=false)` apuntando a `Categoria`; no crear otra entidad Categoría.
- Para el menú público, usar `categoriaService.listarActivas()`.
- **Eliminación**: se usa baja lógica (`activa=false`) para preservar referencias de productos. No se elimina físicamente.
- Seguridad: `/admin/**` **no está protegida todavía**. Integrante 5 debe añadir Spring Security y restringir categorías al rol ADMIN. No desplegar públicamente sin esa integración.
- El proyecto no contiene Producto, Pedido, Usuario ni Rol: son responsabilidad de otros integrantes.

## GitHub (trabajar sobre el repositorio real)
No subir el ZIP como archivo al repositorio. Clonar `https://github.com/Juan080305/sodaweb`, crear tu rama según `ACUERDO_RAMAS.md` y copiar el **contenido** de esta carpeta a la raíz del clon, conservando los archivos README.md y ACUERDO_RAMAS.md existentes. Luego realizar commits y abrir pull request. Coordinar con el equipo antes de fusionar.

## Limitaciones
Se preparó el código pero no se ejecutó contra una instancia real de MySQL en este entorno. Las pruebas de ejecución y de integración quedan pendientes en el equipo local.
