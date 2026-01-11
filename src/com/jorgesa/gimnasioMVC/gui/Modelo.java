package com.jorgesa.gimnasioMVC.gui;

import com.jorgesa.gimnasioMVC.gui.enums.Especialidad;
import com.jorgesa.gimnasioMVC.gui.enums.Membresia;

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
            System.out.println("- Conectado a la base");
            cargarMembresias();
            cargarEspecialidades();
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

                cargarMembresias();
                cargarEspecialidades();
                System.out.println("- Base reconstruida");
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void cargarEspecialidades() {
        String sentenciaSql = "INSERT INTO especialidades(id_especialidad, nombre, paga_extra, descripcion) VALUES (?,?,?,?) ON DUPLICATE KEY UPDATE" +
                "    nombre = VALUES(nombre)," +
                "    paga_extra = VALUES(paga_extra)," +
                "    descripcion = VALUES(descripcion);";
        PreparedStatement sentencia = null;

        try {
            for(Especialidad especialidad : Especialidad.values()){
                sentencia=conexion.prepareStatement(sentenciaSql);
                sentencia.setInt(1,especialidad.getIdEspecialidad());
                sentencia.setString(2,especialidad.getNombre());
                sentencia.setDouble(3,especialidad.getPagaExtra());
                sentencia.setString(4,especialidad.getDescripcion());

                sentencia.executeUpdate();
            }
            System.out.println("- Especialidades insertadas y/o actualizadas a la base de datos");
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

    private void cargarMembresias() {
        String sentenciaSql = "INSERT INTO membresias(id_membresia, nombre, cuota_mensual, descripcion) VALUES (?,?,?,?) ON DUPLICATE KEY UPDATE" +
                "    nombre = VALUES(nombre)," +
                "    cuota_mensual = VALUES(cuota_mensual)," +
                "    descripcion = VALUES(descripcion);";
        PreparedStatement sentencia = null;

        try {
            for(Membresia membresia : Membresia.values()){
                sentencia=conexion.prepareStatement(sentenciaSql);
                sentencia.setInt(1,membresia.getIdMembresia());
                sentencia.setString(2,membresia.getNombre());
                sentencia.setDouble(3,membresia.getCuotaMensual());
                sentencia.setString(4,membresia.getDescripcion());

                sentencia.executeUpdate();
            }
            System.out.println("- Membresias insertadas y/o actualizadas a la base de datos");
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

    void insertarMiembro(String nombre, String apellido, LocalDate fechaNacimiento, String dni,String telefono, String correo, String membresia, String entrenador) {
        String sentenciaSql = "INSERT INTO miembros (nombre, apellido, fecha_nacimiento, dni, telefono, correo, id_membresia, id_entrenador) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement sentencia = null;

        int idMembresia = Integer.parseInt(membresia.split(" - ")[0]);
        int idEntrenador = Integer.parseInt(entrenador.split(" - ")[0]);

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

    void insertarMiembro(String nombre, String apellido, LocalDate fechaNacimiento, String dni,String telefono, String correo, String membresia) {
        String sentenciaSql = "INSERT INTO miembros (nombre, apellido, fecha_nacimiento, dni, telefono, correo, id_membresia) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement sentencia = null;

        int idMembresia = Integer.parseInt(membresia.split(" - ")[0]);

        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setString(1, nombre);
            sentencia.setString(2, apellido);
            sentencia.setDate(3, Date.valueOf(fechaNacimiento));
            sentencia.setString(4, dni);
            sentencia.setString(5,telefono);
            sentencia.setString(6, correo);
            sentencia.setInt(7,idMembresia);
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

    void insertarEntrenador(String nombre, String apellido, String dni,String correo,String telefono,Double sueldo, String especialidad) {
        String sentenciaSql = "INSERT INTO entrenadores (nombre, apellido, dni, correo, telefono, sueldo, id_especialidad) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement sentencia = null;

        int idEspecialidad = Integer.parseInt(especialidad.split(" - ")[0]);

        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setString(1, nombre);
            sentencia.setString(2, apellido);
            sentencia.setString(3,dni);
            sentencia.setString(4, correo);
            sentencia.setString(5,telefono);
            sentencia.setDouble(6, sueldo);
            sentencia.setInt(7,idEspecialidad);
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

    void insertarEntrenador(String nombre, String apellido, String dni,String correo,String telefono,Double sueldo) {
        String sentenciaSql = "INSERT INTO entrenadores (nombre, apellido, dni, correo, telefono, sueldo) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement sentencia = null;


        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setString(1, nombre);
            sentencia.setString(2, apellido);
            sentencia.setString(3,dni);
            sentencia.setString(4, correo);
            sentencia.setString(5,telefono);
            sentencia.setDouble(6, sueldo);
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

    void insertarClase(String nombre, LocalTime horaInicio, int duracionMinutos, String descripcion, String entrenador) {
        String sentenciaSql = "INSERT INTO clases (nombre, hora_inicio, duracion_minutos, descripcion, id_entrenador) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement sentencia = null;

        int idEntrenador = Integer.parseInt(entrenador.split(" - ")[0]);

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

    void insertarClaseSinEntrenador(String nombre, LocalTime horaInicio, int duracionMinutos, String descripcion) {
        String sentenciaSql = "INSERT INTO clases (nombre, hora_inicio, duracion_minutos, descripcion, id_entrenador) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement sentencia = null;

        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setString(1, nombre);
            sentencia.setTime(2, Time.valueOf(horaInicio));
            sentencia.setInt(3,duracionMinutos);
            sentencia.setString(4, descripcion);
            sentencia.setNull(5, Types.INTEGER);
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

    void insertarClaseSinDescripcion(String nombre, LocalTime horaInicio, int duracionMinutos, String entrenador) {
        String sentenciaSql = "INSERT INTO clases (nombre, hora_inicio, duracion_minutos,descripcion,  id_entrenador) VALUES (?, ?, ?,?, ?)";
        PreparedStatement sentencia = null;

        int idEntrenador = Integer.parseInt(entrenador.split(" - ")[0]);

        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setString(1, nombre);
            sentencia.setTime(2, Time.valueOf(horaInicio));
            sentencia.setInt(3,duracionMinutos);
            sentencia.setNull(4, Types.VARCHAR);
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

    void insertarClaseSinDescripcionNiEntrenador(String nombre, LocalTime horaInicio, int duracionMinutos) {
        String sentenciaSql = "INSERT INTO clases (nombre, hora_inicio, duracion_minutos,descripcion,  id_entrenador) VALUES (?, ?, ?,?, ?)";
        PreparedStatement sentencia = null;

        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setString(1, nombre);
            sentencia.setTime(2, Time.valueOf(horaInicio));
            sentencia.setInt(3,duracionMinutos);
            sentencia.setNull(4, Types.VARCHAR);
            sentencia.setNull(5,Types.INTEGER);
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


    void insertarMiembrosClase(String miembro, String clase, LocalDate fechaInscripcion, boolean esNovato) {
        String sentenciaSql = "INSERT INTO miembro_clase (id_miembro, id_clase, fecha_inscripcion, novato) VALUES (?, ?, ?, ?)";
        PreparedStatement sentencia = null;

        int idMiembro = Integer.parseInt(miembro.split(" - ")[0]);
        int idClase = Integer.parseInt(clase.split(" - ")[0]);

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

    void modificarMiembro(int idMiembro, String nombre, String apellido, LocalDate fechaNacimiento, String dni,String telefono, String correo, String membresia, String entrenador) {

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

    void modificarMiembro(int idMiembro, String nombre, String apellido, LocalDate fechaNacimiento, String dni,String telefono, String correo, String membresia) {

        String sentenciaSql = "UPDATE miembros SET nombre = ?, apellido = ?, fecha_nacimiento = ?, dni = ?, " +
                "telefono = ?, correo = ?, id_membresia= ?, id_entrenador= ? WHERE id_miembro = ?";
        PreparedStatement sentencia = null;

        int idMembresia = Integer.valueOf(membresia.split(" - ")[0]);

        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setString(1, nombre);
            sentencia.setString(2, apellido);
            sentencia.setDate(3, Date.valueOf(fechaNacimiento));
            sentencia.setString(4, dni);
            sentencia.setString(5, telefono);
            sentencia.setString(6, correo);
            sentencia.setInt(7, idMembresia);
            sentencia.setNull(8,Types.INTEGER);
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

    void modificarEntrenador(int idEntrenador, String nombre, String apellido, String dni,String correo,String telefono,Double sueldo, String especialidad) {

        String sentenciaSql = "UPDATE entrenadores SET nombre = ?, apellido = ?, dni = ?, correo = ?, " +
                "telefono = ?,sueldo = ?, id_especialidad = ? WHERE id_entrenador = ?";
        PreparedStatement sentencia = null;

        int idEspecialidad = Integer.valueOf(especialidad.split(" - ")[0]);

        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setString(1, nombre);
            sentencia.setString(2, apellido);
            sentencia.setString(3, dni);
            sentencia.setString(4,correo);
            sentencia.setString(5, telefono);
            sentencia.setDouble(6,sueldo);
            sentencia.setInt(7, idEspecialidad);
            sentencia.setInt(8, idEntrenador);
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

    void modificarEntrenador(int idEntrenador, String nombre, String apellido, String dni,String correo,String telefono,Double sueldo) {

        String sentenciaSql = "UPDATE entrenadores SET nombre = ?, apellido = ?, dni = ?, correo = ?, " +
                "telefono = ?,sueldo = ?, id_especialidad = ? WHERE id_entrenador = ?";
        PreparedStatement sentencia = null;

        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setString(1, nombre);
            sentencia.setString(2, apellido);
            sentencia.setString(3, dni);
            sentencia.setString(4,correo);
            sentencia.setString(5, telefono);
            sentencia.setDouble(6,sueldo);
            sentencia.setNull(7,Types.INTEGER);
            sentencia.setInt(8, idEntrenador);
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

    void modificarClase(int idClase, String nombre, LocalTime horaInicio, int duracionMinutos, String descripcion, String entrenador) {

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

    void modificarClaseSinEntrenador(int idClase, String nombre, LocalTime horaInicio, int duracionMinutos, String descripcion) {

        String sentenciaSql = "UPDATE clases SET nombre = ?, hora_inicio = ?, duracion_minutos = ?, descripcion = ?, " +
                " id_entrenador = ? WHERE id_clase = ?";
        PreparedStatement sentencia = null;

        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setString(1, nombre);
            sentencia.setTime(2, Time.valueOf(horaInicio));
            sentencia.setInt(3, duracionMinutos);
            sentencia.setString(4,descripcion);
            sentencia.setNull(5, Types.INTEGER);
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

    void modificarClaseSinDescripcion(int idClase, String nombre, LocalTime horaInicio, int duracionMinutos, String entrenador) {

        String sentenciaSql = "UPDATE clases SET nombre = ?, hora_inicio = ?, duracion_minutos = ?, descripcion = ?, " +
                " id_entrenador = ? WHERE id_clase = ?";
        PreparedStatement sentencia = null;

        int idEntrenador = Integer.valueOf(entrenador.split(" - ")[0]);

        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setString(1, nombre);
            sentencia.setTime(2, Time.valueOf(horaInicio));
            sentencia.setInt(3, duracionMinutos);
            sentencia.setNull(4,Types.VARCHAR);
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

    void modificarClaseSinDescripcionNiEntrenador(int idClase, String nombre, LocalTime horaInicio, int duracionMinutos) {

        String sentenciaSql = "UPDATE clases SET nombre = ?, hora_inicio = ?, duracion_minutos = ?, descripcion = ?, " +
                " id_entrenador = ? WHERE id_clase = ?";
        PreparedStatement sentencia = null;

        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setString(1, nombre);
            sentencia.setTime(2, Time.valueOf(horaInicio));
            sentencia.setInt(3, duracionMinutos);
            sentencia.setNull(4,Types.VARCHAR);
            sentencia.setNull(5, Types.INTEGER);
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

    void eliminarMiembroClase(String miembro, String clase) {
        String sentenciaSql = "DELETE FROM miembro_clase WHERE id_miembro = ? AND id_clase = ?";
        PreparedStatement sentencia = null;

        int idMiembro = Integer.valueOf(miembro.split(" - ")[0]);
        int idClase = Integer.valueOf(clase.split(" - ")[0]);

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

    ResultSet consultarMiembro(){
        String sentenciaSql = "SELECT id_miembro AS ID, " +
                "nombre AS Nombre, " +
                "apellido AS Apellido, "+
                "fecha_nacimiento AS FechaNacimiento, "+
                "dni AS DNI, " +
                "telefono AS Telefono, " +
                "correo AS Correo, " +
                "id_membresia AS Membresia, " +
                "id_entrenador AS Entrenador " +
                "FROM miembros";
        PreparedStatement sentencia;
        ResultSet resultado;
        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            resultado = sentencia.executeQuery();
            return resultado;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    ResultSet consultarEntrenador(){
        String sentenciaSql = "SELECT id_entrenador AS ID, " +
                "nombre AS Nombre, " +
                "apellido AS Apellido, "+
                "dni AS DNI, " +
                "correo AS Correo, " +
                "telefono AS Telefono, " +
                "sueldo AS Sueldo, " +
                "id_especialidad AS Especialidad " +
                "FROM entrenadores";
        PreparedStatement sentencia;
        ResultSet resultado;
        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            resultado = sentencia.executeQuery();
            return resultado;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    ResultSet consultarClase(){
        String sentenciaSql = "SELECT id_clase AS ID, " +
                "nombre AS Nombre, " +
                "hora_inicio AS HoraInicio, "+
                "duracion_minutos AS 'Duracion(min)', " +
                "descripcion AS Descripcion, " +
                "id_entrenador AS Entrenador " +
                "FROM clases";
        PreparedStatement sentencia;
        ResultSet resultado;
        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            resultado = sentencia.executeQuery();
            return resultado;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    ResultSet consultarMiembroClase(){
        String sentenciaSql = "SELECT id_miembro AS Miembro, " +
                "id_clase AS Clase, " +
                "fecha_inscripcion AS Inscripcion, "+
                "novato AS 'Es novato' " +
                "FROM miembro_clase";
        PreparedStatement sentencia;
        ResultSet resultado;
        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            resultado = sentencia.executeQuery();
            return resultado;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    ResultSet consultarEspecialidad() {
        String sentenciaSql = "SELECT id_especialidad AS ID, " +
                "nombre AS Nombre, " +
                "paga_extra AS PagaExtra, "+
                "descripcion AS Descripcion " +
                "FROM especialidades";
        PreparedStatement sentencia;
        ResultSet resultado;
        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            resultado = sentencia.executeQuery();
            return resultado;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    ResultSet consultarMembresia() {
        String sentenciaSql = "SELECT id_membresia AS ID, " +
                "nombre AS Nombre, " +
                "cuota_mensual AS CuotaMensual, "+
                "descripcion AS Descripcion " +
                "FROM membresias";
        PreparedStatement sentencia;
        ResultSet resultado;
        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            resultado = sentencia.executeQuery();
            return resultado;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    ResultSet consultarMiembroClasePorClase(){
        String sentenciaSql = "SELECT id_miembro AS Miembro, " +
                "id_clase AS Clase, " +
                "fecha_inscripcion AS Inscripcion, "+
                "novato AS 'Es novato' " +
                "FROM miembro_clase ORDER BY id_clase";
        PreparedStatement sentencia;
        ResultSet resultado;
        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            resultado = sentencia.executeQuery();
            return resultado;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean dniEntrenadorYaExiste(String dni) {
        String query = "SELECT existeDniEntrenador(?)";
        boolean exists = false;
        try {
            PreparedStatement stmt = conexion.prepareStatement(query);
            stmt.setString(1, dni);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            exists = rs.getBoolean(1);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }

    public boolean dniMiembroYaExiste(String dni) {
        String query = "SELECT existeDniMiembro(?)";
        boolean exists = false;
        try {
            PreparedStatement stmt = conexion.prepareStatement(query);
            stmt.setString(1, dni);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            exists = rs.getBoolean(1);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }

    public boolean nombreClaseYaExiste(String nombre) {
        String query = "SELECT existeNombreClase(?)";
        boolean exists = false;
        try {
            PreparedStatement stmt = conexion.prepareStatement(query);
            stmt.setString(1, nombre);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            exists = rs.getBoolean(1);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }

    public boolean miembroClaseYaExiste(String miembro, String clase) {

        int idMiembro = Integer.parseInt(miembro.split(" - ")[0]);
        int idClase = Integer.parseInt(clase.split(" - ")[0]);

        String query = "SELECT existeMiembroClase(?, ?)";
        boolean exists = false;
        try {
            PreparedStatement stmt = conexion.prepareStatement(query);
            stmt.setInt(1, idMiembro);
            stmt.setInt(2, idClase);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            exists = rs.getBoolean(1);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }

    public ResultSet buscarMiembroPorDni(String dni) {
        ResultSet rs = null;
        try {
            CallableStatement stmt = conexion.prepareCall("{CALL buscarMiembroPorDni(?)}");
            stmt.setString(1, dni);
            rs = stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }


}
