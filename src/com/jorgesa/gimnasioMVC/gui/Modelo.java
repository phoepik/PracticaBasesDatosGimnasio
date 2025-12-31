package com.jorgesa.gimnasioMVC.gui;

import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Properties;

public class Modelo {
    private String ip;
    private String user;
    private String password;
    private String adminPassword;

    public Modelo() {
        getPropValues();
    }

    public String getIp() {
        return ip;
    }
    public String getUser() {
        return user;
    }
    public String getPassword() {
        return password;
    }
    public String getAdminPassword() {
        return adminPassword;
    }

    private Connection conexion;

    void conectar() {
        try {
            conexion = DriverManager.getConnection(
                    "jdbc:mysql://"+ip+":3306/base_gimnasio",user, password);
            System.out.println("- Conectado a la base correctamente");
        } catch (SQLException sqle) {
            try {
                System.out.println("- Error inesperado, volviendo a construir la base");
                conexion = DriverManager.getConnection(
                        "jdbc:mysql://"+ip+":3306/",user, password);

                PreparedStatement statement = null;

                String code = leerFichero();
                String[] query = code.split("--");
                for (String aQuery : query) {
                    statement = conexion.prepareStatement(aQuery);
                    statement.executeUpdate();
                }
                assert statement != null;
                statement.close();
                System.out.println("- Base reconstruida exitosamente");
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String leerFichero() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("baseGimnasioJava.sql")) ;
        String linea;
        StringBuilder stringBuilder = new StringBuilder();
        while ((linea = reader.readLine()) != null) {
            stringBuilder.append(linea);
            stringBuilder.append(" ");
        }

        return stringBuilder.toString();
    }

    void desconectar() {
        try {
            conexion.close();
            conexion = null;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    private void getPropValues() {
        InputStream inputStream = null;
        try {
            Properties prop = new Properties();
            String propFileName = "config.properties";

            inputStream = new FileInputStream(propFileName);

            prop.load(inputStream);
            ip = prop.getProperty("ip");
            user = prop.getProperty("user");
            password = prop.getProperty("pass");
            adminPassword = prop.getProperty("admin");

        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            try {
                if (inputStream != null) inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void setPropValues(String ip, String user, String pass, String adminPass) {
        try {
            Properties prop = new Properties();
            prop.setProperty("ip", ip);
            prop.setProperty("user", user);
            prop.setProperty("pass", pass);
            prop.setProperty("admin", adminPass);
            OutputStream out = new FileOutputStream("config.properties");
            prop.store(out, null);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        this.ip = ip;
        this.user = user;
        this.password = pass;
        this.adminPassword = adminPass;
    }

    void insertarMiembro(String nombre, String apellido, LocalDate fechaNacimiento, String dni,String telefono, String correo, int idMembresia, int idEntrenador) {
        String sentenciaSql = "INSERT INTO miembros (nombre, apellido, fecha_nacimiento, dni, telefono, correo, id_membresia, id_entrenador) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement sentencia = null;

        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setString(1, nombre);
            sentencia.setString(2, apellido);
            sentencia.setDate(3, Date.valueOf(fechaNacimiento));
            sentencia.setString(4, dni);
            sentencia.setString(5,telefono);
            sentencia.setString(6, correo);
            sentencia.setInt(7,idMembresia);
            sentencia.setInt(8,idEntrenador);
            sentencia.executeUpdate();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            if (sentencia != null)
                try {
                    sentencia.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
        }
    }

    void insertarEntrenador(String nombre, String apellido, String dni,String correo,String telefono, int idEspecialidad) {
        String sentenciaSql = "INSERT INTO entrenadores (nombre, apellido, dni, correo, telefono, id_especialidad) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement sentencia = null;

        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setString(1, nombre);
            sentencia.setString(2, apellido);
            sentencia.setString(3,dni);
            sentencia.setString(4, correo);
            sentencia.setString(5,telefono);
            sentencia.setInt(6, idEspecialidad);
            sentencia.executeUpdate();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            if (sentencia != null)
                try {
                    sentencia.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
        }
    }

    void insertarClase(String nombre, LocalTime horaInicio, int duracionMinutos, String descripcion, int idEntrenador) {
        String sentenciaSql = "INSERT INTO clases (nombre, hora_inicio, duracion_minutos, descripcion, id_entrenador) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement sentencia = null;

        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setString(1, nombre);
            sentencia.setTime(2, Time.valueOf(horaInicio));
            sentencia.setInt(3,duracionMinutos);
            sentencia.setString(4, descripcion);
            sentencia.setInt(5,idEntrenador);
            sentencia.executeUpdate();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            if (sentencia != null)
                try {
                    sentencia.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
        }
    }

    void insertarMiembrosClase(int idMiembro, int idClase, LocalDate fechaInscripcion, boolean esNovato) {
        String sentenciaSql = "INSERT INTO miembro_clase (id_miembro, id_clase, fecha_inscripcion, novato) VALUES (?, ?, ?, ?)";
        PreparedStatement sentencia = null;

        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setInt(1, idMiembro);
            sentencia.setInt(2, idClase);
            sentencia.setDate(3, Date.valueOf(fechaInscripcion));
            sentencia.setBoolean(4, esNovato);
            sentencia.executeUpdate();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            if (sentencia != null)
                try {
                    sentencia.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
        }
    }

    void modificarMiembro(String nombre, String apellido, LocalDate fechaNacimiento, String dni,String telefono, String correo, String membresia, String entrenador, int idMiembro) {

        String sentenciaSql = "UPDATE miembros SET nombre = ?, apellido = ?, fecha_nacimiento = ?, dni = ?, " +
                "telefono = ?, correo = ?, id_membresia= ?, id_entrenador= ? WHERE id_miembro = ?";
        PreparedStatement sentencia = null;

        int idMembresia = Integer.valueOf(membresia.split(" - ")[0]);
        int idEntrenador = Integer.valueOf(entrenador.split(" - ")[0]);

        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setString(1, nombre);
            sentencia.setString(2, apellido);
            sentencia.setDate(3, Date.valueOf(fechaNacimiento));
            sentencia.setString(4, dni);
            sentencia.setString(5, telefono);
            sentencia.setString(6, correo);
            sentencia.setInt(7, idMembresia);
            sentencia.setInt(8, idEntrenador);
            sentencia.setInt(9, idMiembro);
            sentencia.executeUpdate();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            if (sentencia != null)
                try {
                    sentencia.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
        }
    }

    void modificarEntrenador(String nombre, String apellido, String dni,String correo,String telefono, String especialidad, int idEntrenador) {

        String sentenciaSql = "UPDATE entrenadores SET nombre = ?, apellido = ?, dni = ?, correo = ?, " +
                "telefono = ?, id_especialidad = ? WHERE id_entrenador = ?";
        PreparedStatement sentencia = null;

        int idEspecialidad = Integer.valueOf(especialidad.split(" - ")[0]);

        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setString(1, nombre);
            sentencia.setString(2, apellido);
            sentencia.setString(3, dni);
            sentencia.setString(4,correo);
            sentencia.setString(5, telefono);
            sentencia.setInt(6, idEspecialidad);
            sentencia.setInt(7, idEntrenador);
            sentencia.executeUpdate();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            if (sentencia != null)
                try {
                    sentencia.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
        }
    }

    void modificarClase(String nombre, LocalTime horaInicio, int duracionMinutos, String descripcion, String entrenador, int idClase) {

        String sentenciaSql = "UPDATE clases SET nombre = ?, hora_inicio = ?, duracion_minutos = ?, descripcion = ?, " +
                " id_entrenador = ? WHERE id_clase = ?";
        PreparedStatement sentencia = null;

        int idEntrenador = Integer.valueOf(entrenador.split(" - ")[0]);

        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setString(1, nombre);
            sentencia.setTime(2, Time.valueOf(horaInicio));
            sentencia.setInt(3, duracionMinutos);
            sentencia.setString(4,descripcion);
            sentencia.setInt(5, idEntrenador);
            sentencia.setInt(6, idClase);
            sentencia.executeUpdate();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            if (sentencia != null)
                try {
                    sentencia.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
        }
    }

    void modificarMiembroClase(String miembro, String clase, LocalDate fechaInscripcion, boolean esNovato){
        String sentenciaSql ="UPDATE miembro_clase SET fecha_inscripcion = ?, novato = ? " +
                "WHERE id_miembro = ? AND id_clase = ?";
        PreparedStatement sentencia = null;

        int idMiembro = Integer.valueOf(miembro.split(" - ")[0]);
        int idClase = Integer.valueOf(clase.split(" - ")[0]);

        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setDate(1, Date.valueOf(fechaInscripcion));
            sentencia.setBoolean(2, esNovato);
            sentencia.setInt(3, idMiembro);
            sentencia.setInt(4,idClase);
            sentencia.executeUpdate();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            if (sentencia != null)
                try {
                    sentencia.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
        }
    }

    void eliminarMiembro(int idMiembro) {
        String sentenciaSql = "DELETE FROM miembros WHERE id_miembro = ?";
        PreparedStatement sentencia = null;

        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setInt(1, idMiembro);
            sentencia.executeUpdate();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            if (sentencia != null)
                try {
                    sentencia.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
        }
    }

    void eliminarEntrenador(int idEntrenador) {
        String sentenciaSql = "DELETE FROM entrenadores WHERE id_entrenador = ?";
        PreparedStatement sentencia = null;

        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setInt(1, idEntrenador);
            sentencia.executeUpdate();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            if (sentencia != null)
                try {
                    sentencia.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
        }
    }

    void eliminarClase(int idClase) {
        String sentenciaSql = "DELETE FROM clases WHERE id_clase = ?";
        PreparedStatement sentencia = null;

        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setInt(1, idClase);
            sentencia.executeUpdate();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            if (sentencia != null)
                try {
                    sentencia.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
        }
    }

    void eliminarMiembroClase(int idMiembro, int idClase) {
        String sentenciaSql = "DELETE FROM miembro_clase WHERE id_miembro = ? AND id_clase = ?";
        PreparedStatement sentencia = null;

        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setInt(1, idMiembro);
            sentencia.setInt(2, idClase);
            sentencia.executeUpdate();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            if (sentencia != null)
                try {
                    sentencia.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
        }
    }
}
