package com.jorgesa.gimnasioMVC.gui;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;

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
