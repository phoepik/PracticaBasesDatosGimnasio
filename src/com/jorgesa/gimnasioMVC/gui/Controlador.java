package com.jorgesa.gimnasioMVC.gui;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
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

    private void cargarTodo() {
        cargarEspecialidades();
        cargarMembresias();
        refrescarMiembros();
        refrescarEntrenadores();
        refrescarClases();
        refrescarMiembrosClase();
    }


    private void addActionListeners(ActionListener listener){
        // miembros
        vista.insertarMiembroButton.addActionListener(listener);
        vista.insertarMiembroButton.setActionCommand("insertarMiembro");
        vista.modificarMiembroButton.addActionListener(listener);
        vista.modificarMiembroButton.setActionCommand("modificarMiembro");
        vista.eliminarMiembroButton.addActionListener(listener);
        vista.eliminarMiembroButton.setActionCommand("eliminarMiembro");

        // entrenadores
        vista.modificarEntrenadorButton.addActionListener(listener);
        vista.modificarEntrenadorButton.setActionCommand("modificarEntrenador");
        vista.insertarEntrenadorButton.addActionListener(listener);
        vista.insertarEntrenadorButton.setActionCommand("insertarEntrenador");
        vista.eliminarEntrenadorButton.addActionListener(listener);
        vista.eliminarEntrenadorButton.setActionCommand("eliminarEntrenador");

        // clases
        vista.insertarClaseButton.addActionListener(listener);
        vista.insertarClaseButton.setActionCommand("insertarClase");
        vista.modificarClaseButton.addActionListener(listener);
        vista.modificarClaseButton.setActionCommand("modificarClase");
        vista.eliminarClaseButton.addActionListener(listener);
        vista.eliminarClaseButton.setActionCommand("eliminarClase");

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

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command) {
            case "insertarMiembroClase":
                modelo.insertarMiembrosClase(String.valueOf(vista.comboMiembroMiembrosClase.getSelectedItem()),
                        String.valueOf(vista.comboClaseMiembrosClase.getSelectedItem()),
                        vista.fechaInscripcionMiembrosClase.getDate(),
                        vista.checkBoxNovatoMiembrosClase.isSelected());
                refrescarMiembrosClase();
                break;
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {    // NO METER EL DE ESPECIALIDAD NI MEMBRESIA
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
                vista.txtDescripcionClase.setText(String.valueOf(vista.tablaClase.getValueAt(fila,4)));
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

    private void refrescarMiembrosClase() {
        try{
            vista.tablaMiembrosClase.setModel(construirTableModel(modelo.consultarMiembroClase(),vista.dtmMiembrosClases));
            System.out.println("- Tabla miembrosClase refrescada");
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

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

    // ventana
    private void addWindowListeners(WindowListener listener){
        vista.addWindowListener(listener);
    }

    @Override
    public void windowClosing(WindowEvent e) {
        System.exit(0);
        System.out.println("- Salida exitosa del programa");
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
