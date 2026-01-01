package com.jorgesa.gimnasioMVC.gui;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

public class Controlador implements ActionListener, ItemListener, ListSelectionListener, WindowListener {

    private Modelo modelo;
    private Vista vista;
    boolean refrescar;

    public Controlador(Modelo modelo, Vista vista){
        this.modelo = modelo;
        this.vista = vista;
        modelo.conectar();
        addActionListeners(this);
        addWindowListeners(this);
        refrescarTodo();
    }

    private void refrescarTodo() {
        refrescarEspecialidades();  // no es necesario ya q especialidades nunca cambia
        refrescarMembresias();  // no es necesario ya q membresias nunca cambia
    }

    private void refrescarMembresias() {
        try{
            vista.tablaMembresia.setModel(construirTableModelMembresias(modelo.consultarMembresia()));
            vista.comboMembresiaMiembro.removeAllItems();
            for(int i = 0; i< vista.dtmMembresias.getRowCount();i++){
                vista.comboMembresiaMiembro.addItem(vista.dtmMembresias.getValueAt(i,0)+" - "+
                        vista.dtmMembresias.getValueAt(i,1));
            }
            vista.comboMembresiaMiembro.setSelectedIndex(-1);
            System.out.println("- Tabla membresias refrescada" +
                    " - Combo membresias refrescado");
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    private TableModel construirTableModelMembresias(ResultSet rs) throws SQLException {
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

        vista.dtmMembresias.setDataVector(data,nombresColumnas);
        return vista.dtmMembresias;
    }

    private void refrescarEspecialidades() {
        try{
            vista.tablaEspecialidad.setModel(construirTableModelEspecialidades(modelo.consultarEspecialidad()));
            vista.comboEspecialidadEntrenador.removeAllItems();
            for(int i = 0; i< vista.dtmEspecialidades.getRowCount();i++){
                vista.comboEspecialidadEntrenador.addItem(vista.dtmEspecialidades.getValueAt(i,0)+" - "+
                vista.dtmEspecialidades.getValueAt(i,1));
            }
            vista.comboEspecialidadEntrenador.setSelectedIndex(-1);
            System.out.println("- Tabla especialidades refrescada" +
                    " - Combo especialidades refrescado");
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    private TableModel construirTableModelEspecialidades(ResultSet rs) throws SQLException {
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

        vista.dtmEspecialidades.setDataVector(data,nombresColumnas);
        return vista.dtmEspecialidades;
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
        vista.insertarMiembroClaseButton.setActionCommand("insertarMiembro");
        vista.modificarMiembroClaseButton.addActionListener(listener);
        vista.modificarMiembroClaseButton.setActionCommand("modificarMiembro");
        vista.eliminarMiembroClaseButton.addActionListener(listener);
        vista.eliminarMiembroClaseButton.setActionCommand("eliminarMiembro");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command){

        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {

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
    public void valueChanged(ListSelectionEvent e) {

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
}
