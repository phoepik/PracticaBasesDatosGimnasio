CREATE DATABASE IF NOT EXISTS base_gimnasio;
--
USE base_gimnasio;
--
CREATE TABLE IF NOT EXISTS membresias (
    id_membresia INT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    cuota_mensual DECIMAL(8,2) NOT NULL,
    descripcion VARCHAR(200)
);
--
CREATE TABLE IF NOT EXISTS especialidades (
    id_especialidad INT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    paga_extra DECIMAL(8,2) NOT NULL,
    descripcion VARCHAR(200)
);
--
CREATE TABLE IF NOT EXISTS entrenadores (
    id_entrenador INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(150) NOT NULL,
    dni VARCHAR(150) UNIQUE NOT NULL,
    correo VARCHAR(150) NOT NULL,
    telefono VARCHAR(150) NOT NULL,
    sueldo DECIMAL NOT NULL,
    id_especialidad INT,
    FOREIGN KEY (id_especialidad) references especialidades(id_especialidad)
);
--
CREATE TABLE IF NOT EXISTS miembros (
    id_miembro INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(150) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    dni VARCHAR(50) UNIQUE NOT NULL,
    telefono VARCHAR(50),
    correo VARCHAR(150),
    id_membresia INT NOT NULL,
    id_entrenador INT,
    FOREIGN KEY (id_membresia) REFERENCES membresias(id_membresia),
    FOREIGN KEY (id_entrenador) REFERENCES entrenadores(id_entrenador) ON DELETE SET NULL
);
--
CREATE TABLE IF NOT EXISTS clases (
    id_clase INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    hora_inicio TIME NOT NULL,
    duracion_minutos INT NOT NULL,
    descripcion VARCHAR(200),
	id_entrenador INT,
    FOREIGN KEY (id_entrenador) REFERENCES entrenadores(id_entrenador)
);
--
CREATE TABLE IF NOT EXISTS miembro_clase (
    id_miembro INT NOT NULL,
    id_clase INT NOT NULL,
    fecha_inscripcion DATE NOT NULL,
    novato BOOLEAN NOT NULL,
    PRIMARY KEY (id_miembro, id_clase),
    FOREIGN KEY (id_miembro) REFERENCES miembros(id_miembro) ON DELETE CASCADE,
    FOREIGN KEY (id_clase) REFERENCES clases(id_clase) ON DELETE CASCADE
);

