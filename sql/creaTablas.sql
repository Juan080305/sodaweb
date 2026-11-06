/*
  Script de creacion de base de datos para SodaWeb
  Este script crea el esquema, tablas, usuario y
  carga datos de ejemplo del menu de la soda.
*/
-- Seccion de administracion (ejecutar una vez en un entorno de desarrollo)
drop database if exists sodaweb;
drop user if exists usuario_sodaweb;

-- Creacion del esquema
CREATE database sodaweb
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

-- Creacion del usuario de la aplicacion
create user 'usuario_sodaweb'@'%' identified by 'Soda_Clave.';

-- Asignacion de permisos
-- Se otorgan permisos especificos en lugar de todos los permisos
grant select, insert, update, delete on sodaweb.* to 'usuario_sodaweb'@'%';
flush privileges;

use sodaweb;

-- --- Seccion de Creacion de Tablas ---

-- Tabla de categorias del menu (HU-09, HU-11)
create table categoria (
  id_categoria INT NOT NULL AUTO_INCREMENT,
  descripcion VARCHAR(50) NOT NULL,
  ruta_imagen varchar(1024),
  activo boolean NOT NULL DEFAULT TRUE,
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id_categoria),
  unique (descripcion))
  ENGINE = InnoDB;

-- Tabla de productos del menu (HU-07, HU-08, HU-10, HU-12)
create table producto (
  id_producto INT NOT NULL AUTO_INCREMENT,
  id_categoria INT NOT NULL,
  descripcion VARCHAR(50) NOT NULL,
  detalle text,
  precio decimal(12,2) NOT NULL CHECK (precio >= 0),
  existencias int unsigned NOT NULL CHECK (existencias >= 0),
  stock_minimo int unsigned NOT NULL DEFAULT 5,
  tipo_alerta ENUM('Critica', 'Media', 'Baja') NOT NULL DEFAULT 'Media',
  ruta_imagen varchar(1024),
  activo boolean NOT NULL DEFAULT TRUE,
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id_producto),
  unique (descripcion),
  index ndx_categoria (id_categoria),
  foreign key fk_producto_categoria (id_categoria) references categoria(id_categoria))
  ENGINE = InnoDB;

-- Tabla de pedidos para retiro (HU-04, HU-05, HU-06)
create table pedido (
  id_pedido INT NOT NULL AUTO_INCREMENT,
  fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  nombre_cliente VARCHAR(60) NOT NULL,
  telefono VARCHAR(25) NOT NULL,
  observaciones VARCHAR(255),
  metodo_pago ENUM('Efectivo', 'SINPE', 'Tarjeta') NOT NULL,
  estado ENUM('Recibido', 'Preparacion', 'Listo', 'Entregado') NOT NULL DEFAULT 'Recibido',
  total DECIMAL(12,2) NOT NULL CHECK (total > 0),
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id_pedido),
  index ndx_estado (estado))
  ENGINE = InnoDB;

-- Tabla del detalle de cada pedido
create table detalle_pedido (
  id_detalle INT NOT NULL AUTO_INCREMENT,
  id_pedido INT NOT NULL,
  id_producto INT NOT NULL,
  cantidad INT UNSIGNED NOT NULL CHECK (cantidad > 0),
  precio_unitario DECIMAL(12,2) NOT NULL CHECK (precio_unitario >= 0),
  subtotal DECIMAL(12,2) NOT NULL CHECK (subtotal >= 0),
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id_detalle),
  index ndx_pedido (id_pedido),
  index ndx_producto (id_producto),
  UNIQUE (id_pedido, id_producto),
  foreign key fk_detalle_pedido (id_pedido) references pedido(id_pedido),
  foreign key fk_detalle_producto (id_producto) references producto(id_producto))
  ENGINE = InnoDB;

-- Tabla de proveedores (HU-13), independiente en el MVP
create table proveedor (
  id_proveedor INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(60) NOT NULL,
  contacto VARCHAR(60) NOT NULL,
  telefono VARCHAR(25) NOT NULL,
  correo VARCHAR(75),
  productos VARCHAR(255),
  activo boolean NOT NULL DEFAULT TRUE,
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id_proveedor),
  unique (nombre))
  ENGINE = InnoDB;

-- Tabla de usuarios del personal (HU-14)
CREATE TABLE usuario (
  id_usuario INT NOT NULL AUTO_INCREMENT,
  username varchar(30) NOT NULL UNIQUE,
  password varchar(512) NOT NULL,
  nombre VARCHAR(60) NOT NULL,
  activo boolean NOT NULL DEFAULT TRUE,
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id_usuario),
  index ndx_username (username))
  ENGINE = InnoDB;

-- Tabla de roles (HU-14)
create table rol (
  id_rol INT NOT NULL AUTO_INCREMENT,
  rol varchar(25) NOT NULL unique,
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  primary key (id_rol))
  ENGINE = InnoDB;

-- Tabla de relacion entre usuarios y roles
create table usuario_rol (
  id_usuario int not null,
  id_rol INT NOT NULL,
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id_usuario, id_rol),
  foreign key fk_usuarioRol_usuario (id_usuario) references usuario(id_usuario),
  foreign key fk_usuarioRol_rol (id_rol) references rol(id_rol))
  ENGINE = InnoDB;

-- Tabla de rutas: que rol puede entrar a cada ruta
CREATE TABLE ruta (
  id_ruta INT AUTO_INCREMENT NOT NULL,
  ruta VARCHAR(255) NOT NULL,
  id_rol INT NULL,
  requiere_rol boolean NOT NULL DEFAULT TRUE,
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CHECK ((requiere_rol = TRUE AND id_rol IS NOT NULL) OR (requiere_rol = FALSE AND id_rol IS NULL)),
  PRIMARY KEY (id_ruta),
  UNIQUE (ruta),
  FOREIGN KEY (id_rol) REFERENCES rol(id_rol))
  ENGINE = InnoDB;

-- --- Seccion de Insercion de Datos ---

-- Insercion de categorias (las mismas del prototipo en Figma)
INSERT INTO categoria (descripcion, ruta_imagen, activo) VALUES
('Desayunos', NULL, true),
('Casados', NULL, true),
('Especiales', NULL, true),
('Bebidas', NULL, true),
('Postres', NULL, true);

-- Insercion de productos (las imagenes se registran como URL en la nube desde el mantenimiento)
INSERT INTO producto (id_categoria, descripcion, detalle, precio, existencias, stock_minimo, tipo_alerta, ruta_imagen, activo) VALUES
(1, 'Gallo pinto con huevo', 'Gallo pinto con huevo al gusto, natilla, platano maduro y tortilla.', 2800, 18, 5, 'Baja', NULL, true),
(1, 'Tortilla con queso', 'Tortilla palmeada hecha al momento con queso tierno.', 1800, 10, 4, 'Baja', NULL, true),
(2, 'Casado con pollo', 'Arroz, frijoles, ensalada, picadillo del dia, platano maduro y pollo en salsa.', 3800, 25, 8, 'Baja', NULL, true),
(2, 'Casado con bistec', 'Arroz, frijoles, ensalada, picadillo, maduro y bistec de res encebollado.', 4200, 12, 5, 'Baja', NULL, true),
(2, 'Casado con pescado', 'Arroz, frijoles, ensalada, picadillo, maduro y filete de pescado empanizado.', 4500, 0, 5, 'Critica', NULL, true),
(3, 'Olla de carne', 'Sopa de res con yuca, elote, chayote y zanahoria. Arroz aparte.', 4000, 9, 4, 'Baja', NULL, true),
(3, 'Chifrijo', 'Arroz, frijoles, chicharron, pico de gallo y tortilla tostada.', 3200, 15, 5, 'Baja', NULL, true),
(4, 'Fresco de cas', 'Fresco natural de cas preparado al momento.', 1000, 4, 10, 'Critica', NULL, true),
(4, 'Cafe chorreado', 'Cafe costarricense chorreado, negro o con leche.', 800, 30, 10, 'Baja', NULL, true),
(5, 'Arroz con leche', 'Arroz con leche casero con canela.', 1200, 6, 8, 'Media', NULL, true),
(5, 'Tres leches', 'Porcion de queque tres leches con merengue.', 1500, 0, 4, 'Media', NULL, true);

-- Insercion de proveedores
INSERT INTO proveedor (nombre, contacto, telefono, correo, productos, activo) VALUES
('Verduleria El Mercado', 'Carlos Rojas', '8811-2233', 'ventas@elmercado.cr', 'Verduras, platano, yuca, chayote', true),
('Lacteos La Montana', 'Marta Solis', '8722-4455', 'pedidos@lamontana.cr', 'Natilla, queso tierno, leche', true),
('Cafe Tarrazu Directo', 'Luis Mora', '8633-6677', NULL, 'Cafe molido', true);

-- Insercion de roles
insert into rol (rol) values ('ADMIN'), ('ENCARGADO');

-- Insercion de usuarios (claves cifradas con BCrypt)
-- admin / admin123      encargado / encargado123
INSERT INTO usuario (username, password, nombre, activo) VALUES
('admin', '$2a$10$1onwQpSfFfYUPLhiLbRgyeqYtnBDthA5iuPjJDuel9TosSaXRSAE6', 'Administrador SodaWeb', true),
('encargado', '$2a$10$/6jseP4iGZBeQ6pJZ152/ux9ZZGD8AHUhEb9PRFCY46Yel2VnMqO2', 'Encargado de turno', true);

-- Asignacion de roles a usuarios (el administrador tambien atiende pedidos)
insert into usuario_rol (id_usuario, id_rol) values
(1, 1), (1, 2), (2, 2);

-- Insercion de rutas con roles especificos
INSERT INTO ruta (ruta, id_rol) VALUES
('/producto/**', 1),
('/categoria/**', 1),
('/proveedor/**', 1),
('/usuario/**', 1),
('/pedido/listado', 2),
('/pedido/detalle/**', 2),
('/pedido/avanzar', 2);

-- Insercion de rutas que no requieren rol
INSERT INTO ruta (ruta, requiere_rol) VALUES
('/', false),
('/menu/**', false),
('/carrito/**', false),
('/pedido/confirmar', false),
('/pedido/recibido/**', false),
('/login', false),
('/acceso_denegado', false),
('/error', false),
('/js/**', false),
('/css/**', false),
('/webjars/**', false);
