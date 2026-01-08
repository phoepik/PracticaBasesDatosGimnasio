package com.jorgesa.gimnasioMVC.gui;

import com.jorgesa.gimnasioMVC.util.Util;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.*;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Vector;

public class Controlador implements ActionListener, ItemListener, ListSelectionListener, WindowListener {

    private Modelo modelo;
    private Vista vista;

    public Controlador(Modelo modelo, Vista vista){
        this.modelo = modelo;
        this.vista = vista;
        modelo.conectar();
        addActionListeners(this);
        addWindowListeners(this);
        iniciarListas(this);
        cargarTodo();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command) {
            // MIEMBRO
            case "borrarEntrenadorMiembro":
                vista.comboEntrenadorMiembro.setSelectedItem(null);
                break;

            case "insertarMiembro":
                if(!hayCamposVaciosMiembro()){
                    insertarMiembro();
                    refrescarMiembros();
                    System.out.println("- MIEMBRO INSERTADO");
                }
            break;

            case "modificarMiembro":
                if(hayCeldaMiembroSeleccionada() && !hayCamposVaciosMiembro()){
                    modificarMiembro();
                    refrescarMiembros();
                    System.out.println("- MIEMBRO MODIFICADO");
                }
                break;

            case "eliminarMiembro": // PENDIENTE, actualizar miembroClase por si acaso
                    if(hayCeldaMiembroSeleccionada()){
                        int fila = vista.tablaMiembro.getSelectedRow();
                        int id = (int) vista.tablaMiembro.getValueAt(fila,0);
                        String nombre = (String) vista.tablaMiembro.getValueAt(fila,1);
                        if(Util.mostrarMensajePregunta("Eliminar miembro: "+id+ " - "+nombre)){
                            modelo.eliminarMiembro(id);
                            refrescarMiembros();
                            //refrescarMiembroClase();
                            System.out.println("- MIEMBRO ELIMINADO");
                        }
                    }
                break;

            // ENTRENADOR
            case "borrarEspecialidadEntrenador":
                vista.comboEspecialidadEntrenador.setSelectedItem(null);
                break;

            case "insertarEntrenador":
                if(!hayCamposVaciosEntrenador()){
                    insertarEntrenador();
                    refrescarEntrenadores();
                    System.out.println("- ENTRENADOR INSERTADO");
                }
                break;

            case "modificarEntrenador":
                if(hayCeldaEntrenadorSeleccionada() && !hayCamposVaciosEntrenador()){
                    modificarEntrenador();
                    refrescarEntrenadores();
                    System.out.println("- ENTRENADOR MODIFICADO");
                }
                break;

            case "eliminarEntrenador":
                if(hayCeldaEntrenadorSeleccionada()){
                    int fila = vista.tablaEntrenador.getSelectedRow();
                    int id = (int) vista.tablaEntrenador.getValueAt(fila,0);
                    String nombre = (String) vista.tablaEntrenador.getValueAt(fila,1);
                    if(Util.mostrarMensajePregunta("Eliminar entrenador: "+id+ " - "+nombre)){
                        modelo.eliminarEntrenador(id);
                        refrescarEntrenadores();
                        System.out.println("- ENTRENADOR ELIMINADO");
                        refrescarMiembros();    // por si acaso tenia algun miembro asociado al entrenador borrado
                        refrescarClases();    // por si acaso tenia alguna clase asociada al entrenador borrado
                    }
                }
                break;

            // CLASE
            case "borrarEntrenadorClase":
                vista.comboEntrenadorClase.setSelectedItem(null);
                break;
            case "insertarClase":
                if(!hayCamposVaciosClase()){
                    insertarClase();
                    refrescarClases();
                    System.out.println("- CLASE INSERTADA");
                }
                break;

            case "modificarClase":
                if(hayCeldaClaseSeleccionada() && !hayCamposVaciosClase()){
                    modificarClase();
                    refrescarClases();
                    System.out.println("- CLASE MODIFICADA");
                }
                break;

            case "eliminarClase":
                if(hayCeldaClaseSeleccionada()){
                    int fila = vista.tablaClase.getSelectedRow();
                    int id = (int) vista.tablaClase.getValueAt(fila,0);
                    String nombre = (String) vista.tablaClase.getValueAt(fila,1);
                    if(Util.mostrarMensajePregunta("Eliminar clase: "+id+ " - "+nombre)){
                        modelo.eliminarClase(id);
                        refrescarClases();
                        System.out.println("- CLASE ELIMINADA");
                    }
                }
                break;

            case "insertarMiembroClase":    // no acabado, pero funcional
                modelo.insertarMiembrosClase(String.valueOf(vista.comboMiembroMiembrosClase.getSelectedItem()),
                        String.valueOf(vista.comboClaseMiembrosClase.getSelectedItem()),
                        vista.fechaInscripcionMiembrosClase.getDate(),
                        vista.checkBoxNovatoMiembrosClase.isSelected());
                refrescarMiembrosClase();
                break;
        }
    }

    private void refrescarMiembrosClase() {
        try{
            vista.tablaMiembrosClase.setModel(construirTableModel(modelo.consultarMiembroClase(),vista.dtmMiembrosClases));
            System.out.println("- Tabla miembrosClase refrescada");
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    // IMPORTANTE: TENER EN CUENTA QUE EN VISTA TAL VEZ SE PUEDA QUITAR EL METODO Q LO CARGA, SINO QUITAR DESDE AQUI LO RELACIONADO AL COMBOBOX
    private void cargarMembresias() {
        try{
            vista.tablaMembresia.setModel(construirTableModel(modelo.consultarMembresia(), vista.dtmMembresias));
            vista.comboMembresiaMiembro.removeAllItems();
            for(int i = 0; i< vista.dtmMembresias.getRowCount();i++){
                vista.comboMembresiaMiembro.addItem(vista.dtmMembresias.getValueAt(i,0)+" - "+
                        vista.dtmMembresias.getValueAt(i,1));
            }
            vista.comboMembresiaMiembro.setSelectedIndex(-1);
            System.out.println("- Tabla membresias refrescada" +
                    " - Combo membresia(en miembros) refrescado");
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    // IMPORTANTE: TENER EN CUENTA QUE EN VISTA TAL VEZ SE PUEDA QUITAR EL METODO Q LO CARGA, SINO QUITAR DESDE AQUI LO RELACIONADO AL COMBOBOX
    private void cargarEspecialidades() {
        try{
            vista.tablaEspecialidad.setModel(construirTableModel(modelo.consultarEspecialidad(), vista.dtmEspecialidades));
            vista.comboEspecialidadEntrenador.removeAllItems();
            for(int i = 0; i< vista.dtmEspecialidades.getRowCount();i++){
                vista.comboEspecialidadEntrenador.addItem(vista.dtmEspecialidades.getValueAt(i,0)+" - "+
                        vista.dtmEspecialidades.getValueAt(i,1));
            }
            vista.comboEspecialidadEntrenador.setSelectedIndex(-1);
            System.out.println("- Tabla especialidades refrescada" +
                    " - Combo especialidad(en entrenadores) refrescado");
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    // MIEMBRO
    private void refrescarMiembros() {
        try{
            vista.tablaMiembro.setModel(construirTableModel(modelo.consultarMiembro(), vista.dtmMiembros));
            vista.comboMiembroMiembrosClase.removeAllItems();
            for(int i = 0; i< vista.dtmMiembros.getRowCount();i++){
                vista.comboMiembroMiembrosClase.addItem(vista.dtmMiembros.getValueAt(i,0)+" - "+
                        vista.dtmMiembros.getValueAt(i,2)+", " + vista.dtmMiembros.getValueAt(i, 1));
            }
            vista.comboMiembroMiembrosClase.setSelectedIndex(-1);
            System.out.println("- Tabla miembros refrescada" +
                    " - Combo miembro(en miembrosClase) refrescado");
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    private void insertarMiembro() {
        if(vista.comboEntrenadorMiembro.getSelectedItem()!=null){
            modelo.insertarMiembro(vista.txtNombreMiembro.getText(),
                    vista.txtApellidoMiembro.getText(),
                    vista.txtFechaNacimientoMiembro.getDate(),
                    vista.txtDniMiembro.getText(),
                    vista.txtTelefonoMiembro.getText(),
                    vista.txtCorreoMiembro.getText(),
                    String.valueOf(vista.comboMembresiaMiembro.getSelectedItem()),
                    String.valueOf(vista.comboEntrenadorMiembro.getSelectedItem()));
        } else {
            modelo.insertarMiembro(vista.txtNombreMiembro.getText(),
                    vista.txtApellidoMiembro.getText(),
                    vista.txtFechaNacimientoMiembro.getDate(),
                    vista.txtDniMiembro.getText(),
                    vista.txtTelefonoMiembro.getText(),
                    vista.txtCorreoMiembro.getText(),
                    String.valueOf(vista.comboMembresiaMiembro.getSelectedItem()));
        }
    }

    private boolean hayCamposVaciosMiembro() {
        ArrayList<String> camposVacios = new ArrayList<>();
        boolean hayCamposVacios = false;
        if (vista.txtNombreMiembro.getText().isEmpty()) {
            camposVacios.add("nombre");
            hayCamposVacios = true;
        }

        if (vista.txtApellidoMiembro.getText().isEmpty()) {
            camposVacios.add("apellido");
            hayCamposVacios = true;
        }

        if (vista.txtFechaNacimientoMiembro.getText().isEmpty()) {
            camposVacios.add("fecha_nacimiento");
            hayCamposVacios = true;
        }

        if (vista.txtDniMiembro.getText().isEmpty()) {
            camposVacios.add("dni");
            hayCamposVacios = true;
        }

        if (vista.txtTelefonoMiembro.getText().isEmpty()) {
            camposVacios.add("telefono");
            hayCamposVacios = true;
        }

        if (vista.txtCorreoMiembro.getText().isEmpty()) {
            camposVacios.add("correo");
            hayCamposVacios = true;
        }

        if (vista.comboMembresiaMiembro.getSelectedItem() == null) {
            camposVacios.add("membresia");
            hayCamposVacios = true;
        }

        if (hayCamposVacios){
            String mensaje = "Campos necesarios: \n"+ String.join("\n", camposVacios);
            Util.mostrarMensajeError(mensaje);
            return true;
        }
        return false;
    }

    private boolean hayCeldaMiembroSeleccionada() {
        int fila = vista.tablaMiembro.getSelectedRow();
        if(fila != -1){
            return true;
        }
        Util.mostrarMensajeError("No has seleccionado ningun miembro en la tabla");
        return false;
    }

    private void modificarMiembro() {
        int fila = vista.tablaMiembro.getSelectedRow();
        int idMiembro = (int) vista.tablaMiembro.getValueAt(fila, 0);

        if(vista.comboEntrenadorMiembro.getSelectedItem()!=null){
            modelo.modificarMiembro(idMiembro,vista.txtNombreMiembro.getText(),
                    vista.txtApellidoMiembro.getText(),
                    vista.txtFechaNacimientoMiembro.getDate(),
                    vista.txtDniMiembro.getText(),
                    vista.txtTelefonoMiembro.getText(),
                    vista.txtCorreoMiembro.getText(),
                    String.valueOf(vista.comboMembresiaMiembro.getSelectedItem()),
                    String.valueOf(vista.comboEntrenadorMiembro.getSelectedItem()));
        } else {
            modelo.modificarMiembro(idMiembro,vista.txtNombreMiembro.getText(),
                    vista.txtApellidoMiembro.getText(),
                    vista.txtFechaNacimientoMiembro.getDate(),
                    vista.txtDniMiembro.getText(),
                    vista.txtTelefonoMiembro.getText(),
                    vista.txtCorreoMiembro.getText(),
                    String.valueOf(vista.comboMembresiaMiembro.getSelectedItem()));
        }
    }

    // ENTRENADOR
    private void refrescarEntrenadores() {
        try{
            vista.tablaEntrenador.setModel(construirTableModel(modelo.consultarEntrenador(), vista.dtmEntrenadores));
            vista.comboEntrenadorMiembro.removeAllItems();
            vista.comboEntrenadorClase.removeAllItems();
            for(int i = 0; i< vista.dtmEntrenadores.getRowCount();i++){
                vista.comboEntrenadorMiembro.addItem(vista.dtmEntrenadores.getValueAt(i,0)+" - "+
                        vista.dtmEntrenadores.getValueAt(i,2)+", " + vista.dtmEntrenadores.getValueAt(i, 1));
                vista.comboEntrenadorClase.addItem(vista.dtmEntrenadores.getValueAt(i,0)+" - "+
                        vista.dtmEntrenadores.getValueAt(i,2)+", " + vista.dtmEntrenadores.getValueAt(i, 1));
            }
            vista.comboEntrenadorMiembro.setSelectedIndex(-1);
            vista.comboEntrenadorClase.setSelectedIndex(-1);
            System.out.println("- Tabla entrenadores refrescada" +
                    " - Combo entrenador(en miembros y clases) refrescado");
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    private boolean hayCamposVaciosEntrenador(){
        ArrayList<String> camposVacios = new ArrayList<>();
        boolean hayCamposVacios = false;
        if (vista.txtNombreEntrenador.getText().isEmpty()) {
            camposVacios.add("nombre");
            hayCamposVacios = true;
        }

        if (vista.txtApellidoEntrenador.getText().isEmpty()) {
            camposVacios.add("apellido");
            hayCamposVacios = true;
        }

        if (vista.txtDniEntrenador.getText().isEmpty()) {
            camposVacios.add("dni");
            hayCamposVacios = true;
        }

        if (vista.txtCorreoEntrenador.getText().isEmpty()) {
            camposVacios.add("correo");
            hayCamposVacios = true;
        }

        if (vista.txtTelefonoEntrenador.getText().isEmpty()) {
            camposVacios.add("telefono");
            hayCamposVacios = true;
        }

        if (vista.txtSueldoEntrenador.getText().isEmpty()) {
            camposVacios.add("sueldo");
            hayCamposVacios = true;
        }

        if (hayCamposVacios){
            String mensaje = "Campos necesarios: \n"+ String.join("\n", camposVacios);
            Util.mostrarMensajeError(mensaje);
            return true;
        }
        return false;
    }

    private void insertarEntrenador(){
        if(vista.comboEspecialidadEntrenador.getSelectedItem()!=null){
            modelo.insertarEntrenador(vista.txtNombreEntrenador.getText(),
                    vista.txtApellidoEntrenador.getText(),
                    vista.txtDniEntrenador.getText(),
                    vista.txtCorreoEntrenador.getText(),
                    vista.txtTelefonoEntrenador.getText(),
                    Double.valueOf(vista.txtSueldoEntrenador.getText()),    // no está controlado q meta un numero
                    String.valueOf(vista.comboEspecialidadEntrenador.getSelectedItem()));
        } else {
            modelo.insertarEntrenador(vista.txtNombreEntrenador.getText(),
                    vista.txtApellidoEntrenador.getText(),
                    vista.txtDniEntrenador.getText(),
                    vista.txtCorreoEntrenador.getText(),
                    vista.txtTelefonoEntrenador.getText(),
                    Double.valueOf(vista.txtSueldoEntrenador.getText()));    // no está controlado q meta un numero;
        }
    }

    private boolean hayCeldaEntrenadorSeleccionada(){
        int fila = vista.tablaEntrenador.getSelectedRow();
        if(fila!=-1){
            return true;
        }
        Util.mostrarMensajeError("No has seleccionado ningun entrenador en la tabla");
        return false;
    }

    private void modificarEntrenador(){
        int fila = vista.tablaEntrenador.getSelectedRow();
        int idEntrenador = (int) vista.tablaEntrenador.getValueAt(fila, 0);

        if(vista.comboEspecialidadEntrenador.getSelectedItem()!=null){
            modelo.modificarEntrenador(idEntrenador,
                    vista.txtNombreEntrenador.getText(),
                    vista.txtApellidoEntrenador.getText(),
                    vista.txtDniEntrenador.getText(),
                    vista.txtCorreoEntrenador.getText(),
                    vista.txtTelefonoEntrenador.getText(),
                    Double.valueOf(vista.txtSueldoEntrenador.getText()),    // no está controlado q meta un numero
                    String.valueOf(vista.comboEspecialidadEntrenador.getSelectedItem())
            );
        } else {
            modelo.modificarEntrenador(idEntrenador,
                    vista.txtNombreEntrenador.getText(),
                    vista.txtApellidoEntrenador.getText(),
                    vista.txtDniEntrenador.getText(),
                    vista.txtCorreoEntrenador.getText(),
                    vista.txtTelefonoEntrenador.getText(),
                    Double.valueOf(vista.txtSueldoEntrenador.getText())    // no está controlado q meta un numero
            );
        }
    }

    // CLASE
    private void refrescarClases() {
        try{
            vista.tablaClase.setModel(construirTableModel(modelo.consultarClase(), vista.dtmClases));
            vista.comboClaseMiembrosClase.removeAllItems();
            vista.comboClaseMiembrosClase.removeAllItems();
            for(int i = 0; i< vista.dtmClases.getRowCount();i++){
                vista.comboClaseMiembrosClase.addItem(vista.dtmClases.getValueAt(i,0)+" - "+
                        vista.dtmClases.getValueAt(i,1));
            }
            vista.comboClaseMiembrosClase.setSelectedIndex(-1);
            System.out.println("- Tabla clases refrescada" +
                    " - Combo clases(en miembros y clases) refrescado");
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    private boolean hayCamposVaciosClase(){
        ArrayList<String> camposVacios = new ArrayList<>();
        boolean hayCamposVacios = false;
        if (vista.txtNombreClase.getText().isEmpty()) {
            camposVacios.add("nombre");
            hayCamposVacios = true;
        }

        if (vista.txtHoraInicioClase.getText().isEmpty()) {
            camposVacios.add("hora");
            hayCamposVacios = true;
        }

        if (vista.txtDuracionClase.getText().isEmpty()) {
            camposVacios.add("duracion");
            hayCamposVacios = true;
        }

        if (hayCamposVacios){
            String mensaje = "Campos necesarios: \n"+ String.join("\n", camposVacios);
            Util.mostrarMensajeError(mensaje);
            return true;
        }
        return false;
    }

    private void insertarClase() {
        boolean sinEntrenador = vista.comboEntrenadorClase.getSelectedItem() == null;
        boolean sinDescripcion = vista.txtDescripcionClase.getText().isEmpty();

        if (sinEntrenador && sinDescripcion) {
            modelo.insertarClaseSinDescripcionNiEntrenador(
                    vista.txtNombreClase.getText(),
                    LocalTime.parse(vista.txtHoraInicioClase.getText()),
                    Integer.parseInt(vista.txtDuracionClase.getText())
            );
        } else if (sinEntrenador) {
            modelo.insertarClaseSinEntrenador(
                    vista.txtNombreClase.getText(),
                    LocalTime.parse(vista.txtHoraInicioClase.getText()),
                    Integer.parseInt(vista.txtDuracionClase.getText()),
                    vista.txtDescripcionClase.getText()
            );
        } else if (sinDescripcion) {
            modelo.insertarClaseSinDescripcion(
                    vista.txtNombreClase.getText(),
                    LocalTime.parse(vista.txtHoraInicioClase.getText()),
                    Integer.parseInt(vista.txtDuracionClase.getText()),
                    String.valueOf(vista.comboEntrenadorClase.getSelectedItem())
            );
        } else {
            modelo.insertarClase(
                    vista.txtNombreClase.getText(),
                    LocalTime.parse(vista.txtHoraInicioClase.getText()),
                    Integer.parseInt(vista.txtDuracionClase.getText()),
                    vista.txtDescripcionClase.getText(),
                    String.valueOf(vista.comboEntrenadorClase.getSelectedItem())
            );
        }
    }

    private boolean hayCeldaClaseSeleccionada(){
        int fila = vista.tablaClase.getSelectedRow();
        if(fila!=-1){
            return true;
        }
        Util.mostrarMensajeError("No has seleccionado ninguna clase en la tabla");
        return false;
    }

    private void modificarClase(){
        int fila = vista.tablaClase.getSelectedRow();
        int idClase = (int) vista.tablaClase.getValueAt(fila, 0);

        boolean sinEntrenador = vista.comboEntrenadorClase.getSelectedItem() == null;
        boolean sinDescripcion = vista.txtDescripcionClase.getText().isEmpty();

        if (sinEntrenador && sinDescripcion) {
            modelo.modificarClaseSinDescripcionNiEntrenador(idClase,
                    vista.txtNombreClase.getText(),
                    LocalTime.parse(vista.txtHoraInicioClase.getText()),
                    Integer.parseInt(vista.txtDuracionClase.getText()));
        } else if (sinEntrenador) {
            modelo.modificarClaseSinEntrenador(idClase,
                    vista.txtNombreClase.getText(),
                    LocalTime.parse(vista.txtHoraInicioClase.getText()),
                    Integer.parseInt(vista.txtDuracionClase.getText()),
                    vista.txtDescripcionClase.getText()
            );
        } else if (sinDescripcion) {
            modelo.modificarClaseSinDescripcion(idClase,
                    vista.txtNombreClase.getText(),
                    LocalTime.parse(vista.txtHoraInicioClase.getText()),
                    Integer.parseInt(vista.txtDuracionClase.getText()),
                    String.valueOf(vista.comboEntrenadorClase.getSelectedItem())
            );
        } else {
            modelo.modificarClase(idClase,
                    vista.txtNombreClase.getText(),
                    LocalTime.parse(vista.txtHoraInicioClase.getText()),
                    Integer.parseInt(vista.txtDuracionClase.getText()),
                    vista.txtDescripcionClase.getText(),
                    String.valueOf(vista.comboEntrenadorClase.getSelectedItem())
            );
        }
    }


    // VENTANA
    private void addWindowListeners(WindowListener listener){
        vista.addWindowListener(listener);
    }

    @Override
    public void windowClosing(WindowEvent e) {
        modelo.desconectar();
        System.exit(0);
        System.out.println("- Salida exitosa del programa");
    }

    // SIN PESTAÑA ESPECIFICA
    private TableModel construirTableModel(ResultSet rs, DefaultTableModel modelo) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();

        // nombres de las columnas
        Vector<String> nombresColumnas = new Vector<>();
        int numColumnas = metaData.getColumnCount();
        for (int columna = 1; columna <= numColumnas; columna++){
            nombresColumnas.add(metaData.getColumnLabel(columna));
        }

        // datos de la tabla
        Vector<Vector<Object>> data = new Vector<>();
        setDataVector(rs,numColumnas,data);

        modelo.setDataVector(data,nombresColumnas);
        return modelo;
    }

    private void setDataVector(ResultSet rs, int columnCount, Vector<Vector<Object>> data) throws SQLException {
        while (rs.next()) {
            Vector<Object> vector = new Vector<>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
        }
    }

    private void iniciarListas(ListSelectionListener listener) {
        // especialidad
        vista.tablaEspecialidad.setCellSelectionEnabled(true);
        vista.tablaEspecialidad.setDefaultEditor(Object.class,null);
        vista.tablaEspecialidad.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        vista.tablaEspecialidad.getSelectionModel().addListSelectionListener(listener);

        // membresia
        vista.tablaMembresia.setCellSelectionEnabled(true);
        vista.tablaMembresia.setDefaultEditor(Object.class,null);
        vista.tablaMembresia.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        vista.tablaMembresia.getSelectionModel().addListSelectionListener(listener);

        // miembro
        vista.tablaMiembro.setCellSelectionEnabled(true);
        vista.tablaMiembro.setDefaultEditor(Object.class,null);
        vista.tablaMiembro.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        vista.tablaMiembro.getSelectionModel().addListSelectionListener(listener);

        // entrenador
        vista.tablaEntrenador.setCellSelectionEnabled(true);
        vista.tablaEntrenador.setDefaultEditor(Object.class,null);
        vista.tablaEntrenador.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        vista.tablaEntrenador.getSelectionModel().addListSelectionListener(listener);

        // clase
        vista.tablaClase.setCellSelectionEnabled(true);
        vista.tablaClase.setDefaultEditor(Object.class,null);
        vista.tablaClase.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        vista.tablaClase.getSelectionModel().addListSelectionListener(listener);

        // miembro_clase
        vista.tablaMiembrosClase.setCellSelectionEnabled(true);
        vista.tablaMiembrosClase.setDefaultEditor(Object.class,null);
        vista.tablaMiembrosClase.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        vista.tablaMiembrosClase.getSelectionModel().addListSelectionListener(listener);

        System.out.println("- Listeners de tablas inicializados");
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if(!e.getValueIsAdjusting() && !((ListSelectionModel) e.getSource()).isSelectionEmpty()){
            if(e.getSource().equals(vista.tablaMiembro.getSelectionModel())){
                int fila = vista.tablaMiembro.getSelectedRow();
                vista.txtNombreMiembro.setText(String.valueOf(vista.tablaMiembro.getValueAt(fila, 1)));
                vista.txtApellidoMiembro.setText(String.valueOf(vista.tablaMiembro.getValueAt(fila, 2)));
                vista.txtFechaNacimientoMiembro.setDate(Date.valueOf(String.valueOf(vista.tablaMiembro.getValueAt(fila,3))).toLocalDate());
                vista.txtDniMiembro.setText(String.valueOf(vista.tablaMiembro.getValueAt(fila, 4)));
                vista.txtTelefonoMiembro.setText(String.valueOf(vista.tablaMiembro.getValueAt(fila, 5)));
                vista.txtCorreoMiembro.setText(String.valueOf(vista.tablaMiembro.getValueAt(fila, 6)));
                String idMembresia = String.valueOf(vista.tablaMiembro.getValueAt(fila, 7));
                buscarItemComboBox(vista.comboMembresiaMiembro, idMembresia);
                String idMiembro = String.valueOf(vista.tablaMiembro.getValueAt(fila,8));
                buscarItemComboBox(vista.comboEntrenadorMiembro, idMiembro);
            } else if (e.getSource().equals(vista.tablaEntrenador.getSelectionModel())){
                int fila = vista.tablaEntrenador.getSelectedRow();
                vista.txtNombreEntrenador.setText(String.valueOf(vista.tablaEntrenador.getValueAt(fila, 1)));
                vista.txtApellidoEntrenador.setText(String.valueOf(vista.tablaEntrenador.getValueAt(fila, 2)));
                vista.txtDniEntrenador.setText(String.valueOf(vista.tablaEntrenador.getValueAt(fila, 3)));
                vista.txtCorreoEntrenador.setText(String.valueOf(vista.tablaEntrenador.getValueAt(fila, 4)));
                vista.txtTelefonoEntrenador.setText(String.valueOf(vista.tablaEntrenador.getValueAt(fila, 5)));
                vista.txtSueldoEntrenador.setText(String.valueOf(vista.tablaEntrenador.getValueAt(fila, 6)));
                String idEspecialidad = String.valueOf(vista.tablaEntrenador.getValueAt(fila,7));
                buscarItemComboBox(vista.comboEspecialidadEntrenador, idEspecialidad);
            } else if (e.getSource().equals(vista.tablaClase.getSelectionModel())){
                int fila = vista.tablaClase.getSelectedRow();
                vista.txtNombreClase.setText(String.valueOf(vista.tablaClase.getValueAt(fila,1)));
                vista.txtHoraInicioClase.setText(String.valueOf(vista.tablaClase.getValueAt(fila,2)));
                vista.txtDuracionClase.setText(String.valueOf(vista.tablaClase.getValueAt(fila,3)));
                if(vista.tablaClase.getValueAt(fila,4)==null){
                    vista.txtDescripcionClase.setText("");
                } else{
                    vista.txtDescripcionClase.setText(String.valueOf(vista.tablaClase.getValueAt(fila,4)));
                }
                String idEntrenador = String.valueOf(vista.tablaClase.getValueAt(fila,5));
                buscarItemComboBox(vista.comboEntrenadorClase, idEntrenador);
            } else if (e.getSource().equals(vista.tablaMiembrosClase.getSelectionModel())){
                int fila = vista.tablaMiembrosClase.getSelectedRow();
                String idMiembro = String.valueOf(vista.tablaMiembrosClase.getValueAt(fila,0));
                buscarItemComboBox(vista.comboMiembroMiembrosClase, idMiembro);
                String idClase = String.valueOf(vista.tablaMiembrosClase.getValueAt(fila,1));
                buscarItemComboBox(vista.comboClaseMiembrosClase, idClase);
                vista.fechaInscripcionMiembrosClase.setDate(Date.valueOf(String.valueOf(vista.tablaMiembrosClase.getValueAt(fila,2))).toLocalDate());
                vista.checkBoxNovatoMiembrosClase.setSelected((Boolean) vista.tablaMiembrosClase.getValueAt(fila,3));
            }
        }
    }

    private void buscarItemComboBox(JComboBox combobox, String idABuscar){
        boolean encontrado = false;
        for(int i = 0; i < combobox.getItemCount();i++){
            if(((String) combobox.getItemAt(i)).startsWith(idABuscar + " - ")){
                encontrado = true;
                combobox.setSelectedIndex(i);
                break;
            }
        }
        if(!encontrado){
            combobox.setSelectedIndex(-1);
        }
    }

    private void addActionListeners(ActionListener listener){
        // miembros
        vista.insertarMiembroButton.addActionListener(listener);
        vista.insertarMiembroButton.setActionCommand("insertarMiembro");
        vista.modificarMiembroButton.addActionListener(listener);
        vista.modificarMiembroButton.setActionCommand("modificarMiembro");
        vista.eliminarMiembroButton.addActionListener(listener);
        vista.eliminarMiembroButton.setActionCommand("eliminarMiembro");
        vista.borrarEntrenadorMiembroButton.addActionListener(listener);
        vista.borrarEntrenadorMiembroButton.setActionCommand("borrarEntrenadorMiembro");


        // entrenadores
        vista.modificarEntrenadorButton.addActionListener(listener);
        vista.modificarEntrenadorButton.setActionCommand("modificarEntrenador");
        vista.insertarEntrenadorButton.addActionListener(listener);
        vista.insertarEntrenadorButton.setActionCommand("insertarEntrenador");
        vista.eliminarEntrenadorButton.addActionListener(listener);
        vista.eliminarEntrenadorButton.setActionCommand("eliminarEntrenador");
        vista.borrarEspecialidadEntrenadorButton.addActionListener(listener);
        vista.borrarEspecialidadEntrenadorButton.setActionCommand("borrarEspecialidadEntrenador");

        // clases
        vista.insertarClaseButton.addActionListener(listener);
        vista.insertarClaseButton.setActionCommand("insertarClase");
        vista.modificarClaseButton.addActionListener(listener);
        vista.modificarClaseButton.setActionCommand("modificarClase");
        vista.eliminarClaseButton.addActionListener(listener);
        vista.eliminarClaseButton.setActionCommand("eliminarClase");
        vista.borrarEntrenadorClaseButton.addActionListener(listener);
        vista.borrarEntrenadorClaseButton.setActionCommand("borrarEntrenadorClase");

        // especialidades
        vista.consultarEspecialidadButton.addActionListener(listener);
        vista.consultarEspecialidadButton.setActionCommand("consultarEspecialidad");

        // membresias
        vista.consultarMembresiaButton.addActionListener(listener);
        vista.consultarMembresiaButton.setActionCommand("consultarMembresia");

        // miembrosClase
        vista.insertarMiembroClaseButton.addActionListener(listener);
        vista.insertarMiembroClaseButton.setActionCommand("insertarMiembroClase");
        vista.modificarMiembroClaseButton.addActionListener(listener);
        vista.modificarMiembroClaseButton.setActionCommand("modificarMiembroClase");
        vista.eliminarMiembroClaseButton.addActionListener(listener);
        vista.eliminarMiembroClaseButton.setActionCommand("eliminarMiembroClase");
    }

    private void cargarTodo() {
        cargarEspecialidades();
        cargarMembresias();
        refrescarMiembros();
        refrescarEntrenadores();
        refrescarClases();
        refrescarMiembrosClase();
    }

    /*LISTENERS IMPLEMENTADOS NO UTILIZADOS*/

    private void addItemListeners(Controlador controlador) {
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    @Override
    public void itemStateChanged(ItemEvent e) {

    }
}
