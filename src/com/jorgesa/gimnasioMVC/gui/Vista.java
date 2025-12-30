package com.jorgesa.gimnasioMVC.gui;

import com.github.lgooddatepicker.components.DatePicker;
import com.jorgesa.gimnasioMVC.gui.enums.Especialidad;
import com.jorgesa.gimnasioMVC.gui.enums.Membresia;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class Vista extends JFrame{
    private JPanel panel1;
    private JTabbedPane tabbedPane1;
    private final static String TITULO_FRAME="Aplicacion gimnasio";

    // Miembros
    private JPanel JPanelMiembros;
    private JTextField txtNombreMiembro;
    private JTextField txtApellidoMiembro;
    private DatePicker txtFechaNacimientoMiembro;
    private JTextField txtDniMiembro;
    private JTextField txtTelefonoMiembro;
    private JTextField txtCorreoMiembro;
    private JComboBox comboMembresiaMiembro;
    private JComboBox comboEntrenadorMiembro;
    private JTable tablaMiembro;

    // Entrenadores
    private JPanel JPanelEntrenadores;
    private JTextField txtNombreEntrenador;
    private JTextField txtApellidoEntrenador;
    private JTextField txtDniEntrenador;
    private JTextField txtCorreoEntrenador;
    private JTextField txtTelefonoEntrenador;
    private JTextField txtSueldoEntrenador;
    private JComboBox comboEspecialidadEntrenador;
    private JTable tablaEntrenador;

    // Clases
    private JPanel JPanelClases;
    private JSpinner horaInicioClase;
    private JTextField txtNombreClase;
    private JTextField txtDuracionClase;
    private JComboBox comboEntrenadorClase;
    private JTextField txtDescripcionClase;
    private JTable tablaClase;

    // Especialidades
    private JPanel JPanelEspecialidades;
    private JTable tablaEspecialidad;

    // Membresias
    private JPanel JPanelMembresias;
    private JTable tablaMembresia;

    // MiembrosClase
    private JPanel JPanelMiembrosClase;
    private JComboBox comboMiembroMiembrosClase;
    private JComboBox comboClaseMiembrosClase;
    private DatePicker fechaInscripcionMiembrosClase;
    private JCheckBox checkBoxNovatoMiembrosClase;
    private JTable tablaMiembrosClase;

    // Default table model
    DefaultTableModel dtmMiembros;
    DefaultTableModel dtmEntrenadores;
    DefaultTableModel dtmClases;
    DefaultTableModel dtmEspecialidades;
    DefaultTableModel dtmMembresias;
    DefaultTableModel dtmMiembrosClases;

    // Menubar
    JMenuItem itemOpciones;
    JMenuItem itemDesconectar;
    JMenuItem itemSalir;

    public Vista() {
        super(TITULO_FRAME);
        initFrame();
    }

    public void initFrame() {
        this.setContentPane(panel1);
        //al clickar en cerrar no hace nada
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.pack();
        //doy dimension
        this.setSize(new Dimension(this.getWidth()+100,this.getHeight()));
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        //llamo menu
        setMenu();
        //cargo enumerados
        setEnumComboBox();
        //cargo table models
        setTableModels();
    }

    private void setMenu() {
        JMenuBar mbBar = new JMenuBar();
        JMenu menu = new JMenu("Archivo");
        itemOpciones = new JMenuItem("Opciones");
        itemOpciones.setActionCommand("Opciones");
        itemDesconectar = new JMenuItem("Desconectar");
        itemDesconectar.setActionCommand("Desconectar");
        itemSalir=new JMenuItem("Salir");
        itemSalir.setActionCommand("Salir");
        menu.add(itemOpciones);
        menu.add(itemDesconectar);
        menu.add(itemSalir);
        mbBar.add(menu);
        mbBar.add(Box.createHorizontalGlue());
        this.setJMenuBar(mbBar);
    }

    private void setEnumComboBox() {
        //recorrer los enumerados y los cargo en el comboBox correspondiente
        // membresias
        for(Membresia membresia : Membresia.values()){
            comboMembresiaMiembro.addItem(membresia.getNombre());
        }
        comboMembresiaMiembro.setSelectedIndex(-1);

        // especialidades
        for(Especialidad especialidad : Especialidad.values()){
            comboEspecialidadEntrenador.addItem(especialidad.getNombre());
        }
        comboEspecialidadEntrenador.setSelectedIndex(-1);
    }

    private void setTableModels() {
        this.dtmMiembros = new DefaultTableModel();
        this.tablaMiembro.setModel(dtmMiembros);

        this.dtmEntrenadores = new DefaultTableModel();
        this.tablaEntrenador.setModel(dtmEntrenadores);

        this.dtmClases = new DefaultTableModel();
        this.tablaClase.setModel(dtmClases);

        this.dtmEspecialidades = new DefaultTableModel();
        this.tablaEspecialidad.setModel(dtmEspecialidades);

        this.dtmMembresias = new DefaultTableModel();
        this.tablaMembresia.setModel(dtmMembresias);

        this.dtmMiembrosClases = new DefaultTableModel();
        this.tablaMiembrosClase.setModel(dtmMiembrosClases);
    }
}

