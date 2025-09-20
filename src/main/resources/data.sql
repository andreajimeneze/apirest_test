CREATE TABLE IF NOT EXISTS productos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(200) NOT NULL,
    stock INT NOT NULL,
    precio DOUBLE NOT NULL,
    status BOOLEAN NOT NULL DEFAULT TRUE
);

INSERT INTO productos (nombre, descripcion, stock, precio, status) 
VALUES ('Laptop Lenovo', 'Notebook Lenovo IdeaPad 3', 10, 550000, true);

INSERT INTO productos (nombre, descripcion, stock, precio, status) 
VALUES ('Mouse Logitech', 'Mouse inalámbrico Logitech M185', 50, 15000, true);

INSERT INTO productos (nombre, descripcion, stock, precio, status) 
VALUES ('Teclado Mecánico', 'Teclado mecánico Redragon Kumara', 20, 30000, true);

INSERT INTO productos (nombre, descripcion, stock, precio, status) 
VALUES ('Monitor Samsung', 'Monitor LED Samsung 24 pulgadas', 15, 120000, true);
