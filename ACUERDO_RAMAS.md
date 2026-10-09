# Acuerdo de trabajo por ramas

## Ramas
- `main`: versión estable y entregable. Solo recibe cambios desde `develop` al cerrar cada avance.
- `develop`: integración del equipo. Toda funcionalidad nueva se une aquí.
- `feature/<hu>-<descripcion>`: una rama por historia de usuario. Ejemplo: `feature/hu-03-carrito`.
- `fix/<descripcion>`: correcciones puntuales.

## Flujo
1. Actualizar `develop` (`git pull origin develop`).
2. Crear la rama: `git checkout -b feature/hu-xx-nombre`.
3. Hacer commits pequeños con mensajes claros: `HU-03: recalcula el total del carrito`.
4. Subir la rama y abrir un Pull Request hacia `develop`.
5. Otro integrante revisa y aprueba antes de unir.
6. Al cerrar cada avance, se une `develop` en `main` y se etiqueta la versión (`v0.1`, `v0.2`…).

## Reglas
- No se hace push directo a `main`.
- Cada Pull Request indica la HU que resuelve.
- Antes de unir, el proyecto debe compilar y ejecutarse sin errores.
