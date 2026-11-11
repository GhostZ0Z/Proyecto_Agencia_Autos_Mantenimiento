CREATE DATABASE IF NOT EXISTS agencia_autos
  CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE agencia_autos;

-- Para poder volver a correr el script desde cero (borra todo y recrea)
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS servicio_refacciones, refacciones, servicios, vehiculos,
                     propietarios, anios, modelos, marcas, usuarios;
SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE usuarios (
  id            INT AUTO_INCREMENT PRIMARY KEY,
  usuario       VARCHAR(50)  NOT NULL UNIQUE,
  password_hash CHAR(64)     NOT NULL,
  nombre        VARCHAR(100) NOT NULL
) ENGINE=InnoDB;

CREATE TABLE marcas (
  id     INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(60) NOT NULL UNIQUE
) ENGINE=InnoDB;

CREATE TABLE modelos (
  id       INT AUTO_INCREMENT PRIMARY KEY,
  marca_id INT NOT NULL,
  nombre   VARCHAR(60) NOT NULL,
  UNIQUE KEY uq_modelo_marca (marca_id, nombre),
  FOREIGN KEY (marca_id) REFERENCES marcas(id)
) ENGINE=InnoDB;

CREATE TABLE anios (
  id   INT AUTO_INCREMENT PRIMARY KEY,
  anio INT NOT NULL UNIQUE
) ENGINE=InnoDB;

CREATE TABLE propietarios (
  id       INT AUTO_INCREMENT PRIMARY KEY,
  nombre   VARCHAR(100) NOT NULL,
  telefono VARCHAR(20),
  INDEX idx_propietario_nombre (nombre)      -- acelera la búsqueda por nombre
) ENGINE=InnoDB;

CREATE TABLE vehiculos (
  id             INT AUTO_INCREMENT PRIMARY KEY,
  propietario_id INT NOT NULL,
  modelo_id      INT NOT NULL,
  anio_id        INT NOT NULL,
  placas         VARCHAR(15),
  FOREIGN KEY (propietario_id) REFERENCES propietarios(id),
  FOREIGN KEY (modelo_id)      REFERENCES modelos(id),
  FOREIGN KEY (anio_id)        REFERENCES anios(id)
) ENGINE=InnoDB;

CREATE TABLE servicios (
  id               INT AUTO_INCREMENT PRIMARY KEY,
  folio            VARCHAR(20) NOT NULL UNIQUE,
  vehiculo_id      INT NOT NULL,
  usuario_id       INT NOT NULL,
  persona_entrega  VARCHAR(100),
  tipo             ENUM('Preventivo','Correctivo') NOT NULL,
  descripcion      TEXT,
  estatus          ENUM('En espera','En proceso','Finalizado') NOT NULL DEFAULT 'En espera',
  fecha_ingreso    DATE NOT NULL,
  proximo_servicio DATE NULL,
  FOREIGN KEY (vehiculo_id) REFERENCES vehiculos(id),
  FOREIGN KEY (usuario_id)  REFERENCES usuarios(id)
) ENGINE=InnoDB;

CREATE TABLE refacciones (
  id     INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL UNIQUE,
  precio DECIMAL(10,2) NOT NULL DEFAULT 0
) ENGINE=InnoDB;

CREATE TABLE servicio_refacciones (
  servicio_id  INT NOT NULL,
  refaccion_id INT NOT NULL,
  cantidad     INT NOT NULL DEFAULT 1,
  PRIMARY KEY (servicio_id, refaccion_id),
  FOREIGN KEY (servicio_id)  REFERENCES servicios(id) ON DELETE CASCADE,
  FOREIGN KEY (refaccion_id) REFERENCES refacciones(id)
) ENGINE=InnoDB;

-- Administrador de prueba  usuario: admin | contraseña: admin123
INSERT INTO usuarios (usuario, password_hash, nombre)
VALUES ('admin', SHA2('admin123', 256), 'Administrador');

INSERT INTO marcas (nombre) VALUES
  ('Nissan'), ('Toyota'), ('Chevrolet'), ('Ford'), ('Volkswagen');

INSERT INTO modelos (marca_id, nombre)
  SELECT id, 'Versa'   FROM marcas WHERE nombre = 'Nissan';
INSERT INTO modelos (marca_id, nombre)
  SELECT id, 'Sentra'  FROM marcas WHERE nombre = 'Nissan';
INSERT INTO modelos (marca_id, nombre)
  SELECT id, 'Corolla' FROM marcas WHERE nombre = 'Toyota';
INSERT INTO modelos (marca_id, nombre)
  SELECT id, 'Aveo'    FROM marcas WHERE nombre = 'Chevrolet';
INSERT INTO modelos (marca_id, nombre)
  SELECT id, 'Figo'    FROM marcas WHERE nombre = 'Ford';
INSERT INTO modelos (marca_id, nombre)
  SELECT id, 'Jetta'   FROM marcas WHERE nombre = 'Volkswagen';

INSERT INTO anios (anio) VALUES
  (2015),(2016),(2017),(2018),(2019),(2020),(2021),(2022),(2023),(2024),(2025),(2026);

INSERT INTO refacciones (nombre, precio) VALUES
  ('Aceite de motor 5W-30', 350.00),
  ('Filtro de aceite', 120.00),
  ('Filtro de aire', 180.00),
  ('Balatas delanteras', 650.00),
  ('Bujías (juego)', 480.00),
  ('Batería', 1900.00);

