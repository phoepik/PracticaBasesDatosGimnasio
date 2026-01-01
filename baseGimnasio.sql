DROP DATABASE IF EXISTS base_gimnasio;
-- CREACION DE BASE DE DATOS Y PONERLA EN USO
CREATE DATABASE IF NOT EXISTS base_gimnasio;
--
USE base_gimnasio;
-- CREACION TABLA MEMBRESIAS
CREATE TABLE IF NOT EXISTS membresias (
    id_membresia INT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    cuota_mensual DECIMAL(8,2) NOT NULL,
    descripcion VARCHAR(200) -- Puede tener o no tener descripcion
);
-- se tendra q hacer con los valores q le meta de un enum en java (borrar luego la consulta)
INSERT INTO membresias (nombre, cuota_mensual,descripcion) -- sin desc porque lo queria a null para empezar
VALUES ("Normal", 29.99, "La membresía más básica, no incluye clases ni entrenador"),
("Premium", 39.99, "Una membresía para los que necesiten un entrenador"),
("Ultimate", 49.99, "La membresía más completa, incluye entrenador, y las clases que quieras.");
-- CREACION TABLA ESPECIALIDADES
CREATE TABLE IF NOT EXISTS especialidades (
    id_especialidad INT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    paga_extra DECIMAL(8,2) NOT NULL,
    descripcion VARCHAR(200) -- Puede tener o no tener descripcion
);
-- se tendra q hacer con los valores q le meta de un enum en java (borrar luego la consulta)
INSERT INTO especialidades (nombre, paga_extra) -- sin desc porque lo queria a null para empezar
VALUES ("Boxeo", 40.00),
("Futbol", 10.00),
("Tennis", 15.00);
-- CREACION TABLA ENTRENADORES
CREATE TABLE IF NOT EXISTS entrenadores (
    id_entrenador INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(150) NOT NULL,
    dni VARCHAR(150) UNIQUE NOT NULL,
    correo VARCHAR(150) NOT NULL,
    telefono VARCHAR(150) NOT NULL,
    sueldo DECIMAL NOT NULL,
    id_especialidad INT, -- Puede tener o no especialidad
    FOREIGN KEY (id_especialidad) references especialidades(id_especialidad)
);

-- CREACION TABLA MIEMBROS
CREATE TABLE IF NOT EXISTS miembros (
    id_miembro INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(150) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    dni VARCHAR(50) UNIQUE NOT NULL,
    telefono VARCHAR(50),
    correo VARCHAR(150),
    id_membresia INT NOT NULL,
    id_entrenador INT, -- Puede tener o no entrenador
    FOREIGN KEY (id_membresia) REFERENCES membresias(id_membresia),
    FOREIGN KEY (id_entrenador) REFERENCES entrenadores(id_entrenador)
);

-- CREACION TABLA CLASES
CREATE TABLE IF NOT EXISTS clases (
    id_clase INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    hora_inicio TIME NOT NULL,
    duracion_minutos INT NOT NULL,
    descripcion VARCHAR(200),-- opcional
	id_entrenador INT,	-- por si quiero crear la clase y no hay entrenador aun asignado
    FOREIGN KEY (id_entrenador) REFERENCES entrenadores(id_entrenador)
);

INSERT INTO clases (nombre, hora_inicio, duracion_minutos)
VALUES ("Yoga", "18:00:00", 60),
("Boxeo", "19:00:00", 60),
("Crossfit", "20:00:00", 60),
("Bici", "21:00:00", 60);

-- CREACION TABLA MIEMBRO_CLASE, para q un miembro pueda estar en varias clases
CREATE TABLE IF NOT EXISTS miembro_clase (
    id_miembro INT NOT NULL,
    id_clase INT NOT NULL,
    fecha_inscripcion DATE NOT NULL,
    novato BOOLEAN NOT NULL,
    PRIMARY KEY (id_miembro, id_clase),
    FOREIGN KEY (id_miembro) REFERENCES miembros(id_miembro) ON DELETE CASCADE, -- que si elimino el miembro se elimina el registro de este
    FOREIGN KEY (id_clase) REFERENCES clases(id_clase) ON DELETE CASCADE
);

