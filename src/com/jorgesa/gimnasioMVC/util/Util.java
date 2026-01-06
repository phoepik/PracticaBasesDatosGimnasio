package com.jorgesa.gimnasioMVC.util;

import javax.swing.*;

public class Util {
    //mensaje de error
    public static void mostrarMensajeError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    //mensaje de aviso
    public static void mostrarMensajeAlerta(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Aviso", JOptionPane.WARNING_MESSAGE);
    }

    //mensaje de info
    public static void mostrarMensajeInformativo(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean  mostrarMensajePregunta(String mensaje) {
        int opcion = JOptionPane.showConfirmDialog(
                null, mensaje, "Confirmar", JOptionPane.YES_NO_OPTION);
        if (opcion==JOptionPane.YES_OPTION){
            return true;
        }
        return false;
    }
}
