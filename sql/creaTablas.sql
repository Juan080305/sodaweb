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

-- --- Seccion de Insercion de Datos ---

-- Insercion de categorias
INSERT INTO categoria (descripcion, ruta_imagen, activo) VALUES
('Desayunos', NULL, true),
('Casados', NULL, true),
('Bocadillos', NULL, true),
('Bebidas naturales', NULL, true),
('Reposteria', NULL, true);

-- Insercion de productos
INSERT INTO producto (id_categoria, descripcion, detalle, precio, existencias, stock_minimo, tipo_alerta, ruta_imagen, activo) VALUES
(1, 'Gallo pinto con huevo', 'Gallo pinto de la casa con huevo frito o picado, natilla y tortilla palmeada.', 2800, 25, 5, 'Critica', NULL, true),
(1, 'Pinto completo', 'Gallo pinto, huevo, queso frito, platano maduro, natilla y tortilla.', 3900, 20, 5, 'Critica', NULL, true),
(1, 'Tortilla con queso', 'Tortilla palmeada hecha al momento con queso tierno.', 1800, 3, 5, 'Media', NULL, true),
(2, 'Casado con pollo', 'Arroz, frijoles, ensalada, platano maduro, picadillo del dia y pollo en salsa.', 3800, 15, 4, 'Critica', NULL, true),
(2, 'Casado con bistec', 'Arroz, frijoles, ensalada, platano maduro, picadillo del dia y bistec encebollado.', 4200, 12, 4, 'Critica', NULL, true),
(2, 'Casado vegetariano', 'Arroz, frijoles, ensalada, platano maduro, picadillo y huevo frito.', 3300, 0, 3, 'Media', NULL, true),
(3, 'Empanada de queso', 'Empanada de maiz rellena de queso, recien frita.', 1200, 18, 6, 'Media', NULL, true),
(3, 'Arreglado de pollo', 'Pan suave con pollo deshilachado, repollo y salsa de la casa.', 1500, 10, 4, 'Baja', NULL, true),
(4, 'Fresco de cas', 'Fresco natural de cas en agua, vaso de 500 ml.', 1000, 30, 8, 'Baja', NULL, true),
(4, 'Batido de fresa en leche', 'Batido de fresa natural en leche, vaso de 500 ml.', 1600, 4, 6, 'Media', NULL, true),
(4, 'Cafe chorreado', 'Cafe costarricense chorreado al momento.', 900, 40, 10, 'Baja', NULL, true),
(5, 'Tres leches', 'Porcion de queque tres leches de la casa.', 1700, 8, 3, 'Baja', NULL, true),
(5, 'Arroz con leche', 'Arroz con leche con canela, porcion individual.', 1300, 0, 3, 'Baja', NULL, false);
