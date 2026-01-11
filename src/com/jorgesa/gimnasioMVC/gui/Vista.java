package com.jorgesa.gimnasioMVC.gui;

import com.github.lgooddatepicker.components.DatePicker;
import com.jorgesa.gimnasioMVC.gui.enums.Especialidad;
import com.jorgesa.gimnasioMVC.gui.enums.Membresia;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class Vista extends JFrame {
    private JPanel panel1;
    private JTabbedPane tabbedPane1;
    private final static String TITULO_FRAME = "Aplicacion gimnasio";

    // Miembros
    JPanel JPanelMiembros;
    JTextField txtNombreMiembro;
    JTextField txtApellidoMiembro;
    DatePicker txtFechaNacimientoMiembro;
    JTextField txtDniMiembro;
    JTextField txtTelefonoMiembro;
    JTextField txtCorreoMiembro;
    JComboBox comboMembresiaMiembro;
    JComboBox comboEntrenadorMiembro;
    JTable tablaMiembro;
    JButton insertarMiembroButton;
    JButton modificarMiembroButton;
    JButton eliminarMiembroButton;

    // Entrenadores
    JPanel JPanelEntrenadores;
    JTextField txtNombreEntrenador;
    JTextField txtApellidoEntrenador;
    JTextField txtDniEntrenador;
    JTextField txtCorreoEntrenador;
    JTextField txtTelefonoEntrenador;
    JTextField txtSueldoEntrenador;
    JComboBox comboEspecialidadEntrenador;
    JTable tablaEntrenador;
    JButton modificarEntrenadorButton;
    JButton insertarEntrenadorButton;
    JButton eliminarEntrenadorButton;

    // Clases
    JPanel JPanelClases;
    JTextField txtNombreClase;
    JTextField txtHoraInicioClase;
    JTextField txtDuracionClase;
    JComboBox comboEntrenadorClase;
    JTextField txtDescripcionClase;
    JTable tablaClase;
    JButton insertarClaseButton;
    JButton modificarClaseButton;
    JButton eliminarClaseButton;

    // Especialidades
    JPanel JPanelEspecialidades;
    JTable tablaEspecialidad;

    // Membresias
    JPanel JPanelMembresias;
    JTable tablaMembresia;

    // MiembrosClase
    JPanel JPanelMiembrosClase;
    JComboBox comboMiembroMiembrosClase;
    JComboBox comboClaseMiembrosClase;
    DatePicker fechaInscripcionMiembrosClase;
    JCheckBox checkBoxNovatoMiembrosClase;
    JTable tablaMiembrosClase;
    JButton insertarMiembroClaseButton;
    JButton modificarMiembroClaseButton;
    JButton eliminarMiembroClaseButton;
    JButton borrarEntrenadorMiembroButton;
    JButton borrarEspecialidadEntrenadorButton;
    JButton borrarEntrenadorClaseButton;
    JRadioButton miembroRadioButton;
    JRadioButton claseRadioButton;
    JTextField txtDniFiltrarMiembro;
    JButton filtrarMiembroButton;


    // Default table model
    DefaultTableModel dtmMiembros;
    DefaultTableModel dtmEntrenadores;
    DefaultTableModel dtmClases;
    DefaultTableModel dtmEspecialidades;
    DefaultTableModel dtmMembresias;
    DefaultTableModel dtmMiembrosClases;

    // Menubar
    JMenuItem itemOpciones;
    JMenuItem itemSalir;

    //cuadro dialogo
    OptionDialog optionDialog;
    JDialog adminPasswordDialog;
    JButton btnValidate;
    JPasswordField adminPassword;

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
        this.setSize(new Dimension(this.getWidth() + 200, this.getHeight()));
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        // creo cuadro de dialogo
        optionDialog=new OptionDialog(this);
        //llamo menu
        setMenu();
        //llamo cuadro dialogo admin
        setAdminDialog();
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
        itemSalir = new JMenuItem("Salir");
        itemSalir.setActionCommand("Salir");
        menu.add(itemOpciones);
        menu.add(itemSalir);
        mbBar.add(menu);
        mbBar.add(Box.createHorizontalGlue());
        this.setJMenuBar(mbBar);
    }

    private void setEnumComboBox() {
        //recorrer los enumerados y los cargo en el comboBox correspondiente
        // membresias
        for (Membresia membresia : Membresia.values()) {
            comboMembresiaMiembro.addItem(membresia.getIdMembresia()+" - "+membresia.getNombre());
        }
        comboMembresiaMiembro.setSelectedIndex(-1);

        // especialidades
        for (Especialidad especialidad : Especialidad.values()) {
            comboEspecialidadEntrenador.addItem(especialidad.getIdEspecialidad() + " - "+especialidad.getNombre());
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

    private void setAdminDialog() {
        btnValidate = new JButton("Validar");
        btnValidate.setActionCommand("abrirOpciones");
        adminPassword = new JPasswordField();
        //dimension al cuadro de texto
        adminPassword.setPreferredSize(new Dimension(100,26));
        Object[] options=new Object[] {adminPassword,btnValidate};
        JOptionPane jop = new JOptionPane("Introduce la contraseña",JOptionPane.WARNING_MESSAGE,
                JOptionPane.YES_NO_OPTION,null,options);
        adminPasswordDialog = new JDialog(this,"Opciones",true);
        adminPasswordDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        adminPasswordDialog.setContentPane(jop);
        adminPasswordDialog.pack();
        adminPasswordDialog.setLocationRelativeTo(this);
    }
}

