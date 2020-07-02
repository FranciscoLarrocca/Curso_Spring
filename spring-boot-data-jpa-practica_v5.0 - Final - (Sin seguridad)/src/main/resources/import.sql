
/* Tabla Clientes */

INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('Juan', 'Perez', 'jperez@mail.com', '2020-06-13', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('Carla', 'Gomez', 'cgomez@mail.com', '2020-06-13', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('Pedro', 'Perez', 'pperez@mail.com', '2020-06-13', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('Marcela', 'Mendez', 'mm@mail.com', '2020-06-13', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('Camila', 'Ramirez', 'cramirez@mail.com', '2020-06-13', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('Eugenia', 'Di Pace', 'edp@mail.com', '2020-06-13', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('Pablo', 'Brondino', 'pb@mail.com', '2020-06-13', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('Mirta', 'Smith', 'msmith@mail.com', '2020-06-13', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('Ivan', 'Hortz', 'hivan@mail.com', '2020-06-13', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('Estaban', 'Gudjonsen', 'eg@mail.com', '2020-06-13', '');

/* Tabla Productos */

INSERT INTO productos(nombre, precio, create_at) VALUES('Panasonic Pantalla LCD','6000', NOW());
INSERT INTO productos(nombre, precio, create_at) VALUES('Sony Camara Digital DSC-W320B','35000', NOW());
INSERT INTO productos(nombre, precio, create_at) VALUES('Apple Ipod Shuffle','80000', NOW());
INSERT INTO productos(nombre, precio, create_at) VALUES('Sony Notebook Z110','40000', NOW());
INSERT INTO productos(nombre, precio, create_at) VALUES('Asus Notebook','25000', NOW());
INSERT INTO productos(nombre, precio, create_at) VALUES('HP Spectre','90000', NOW());
INSERT INTO productos(nombre, precio, create_at) VALUES('Samsung A10S','15000', NOW());
INSERT INTO productos(nombre, precio, create_at) VALUES('Nokia 5.1','15500', NOW());
INSERT INTO productos(nombre, precio, create_at) VALUES('Bianchi Bicicleta MB','20000', NOW());
INSERT INTO productos(nombre, precio, create_at) VALUES('Puma mochila x','6000', NOW());

/* Creamos algunas facturas */

INSERT INTO facturas (descripcion, observacion, cliente_id, create_at) VALUES('Factura equipos de oficina', null, 1, NOW());
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES(1, 1, 1);
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES(2, 1, 4);
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES(1, 1, 5);
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES(1, 1, 7);

INSERT INTO facturas (descripcion, observacion, cliente_id, create_at) VALUES('Factura Bicicleta', 'Alguna nota importante!', 1, NOW());
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES(3, 2, 6);