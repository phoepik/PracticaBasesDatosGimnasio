package com.jorgesa.gimnasioMVC;

import com.jorgesa.gimnasioMVC.gui.Controlador;
import com.jorgesa.gimnasioMVC.gui.Modelo;
import com.jorgesa.gimnasioMVC.gui.Vista;

public class Principal {
    public static void main(String[] args) {
        Vista vista = new Vista();
        Modelo modelo = new Modelo();
        Controlador controlador = new Controlador(modelo,vista);
    }
}
