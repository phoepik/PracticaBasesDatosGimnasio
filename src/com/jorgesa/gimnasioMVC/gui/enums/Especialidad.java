package com.jorgesa.gimnasioMVC.gui.enums;

public enum Especialidad {
    BOXEO("Boxeo", 40.00, "Entrenamiento de boxeo con saco y técnicas de combate"),
    FUTBOL("Futbol", 10.00, "Entrenamiento de fútbol"),
    TENNIS("Tennis", 15.00, "Clases de tenis"),
    SPINNING("Spinning", 12.00, "Sesiones de bicicleta estática con música"),
    YOGA("Yoga", 8.00, "Clases de yoga para flexibilidad, respiración y relajación");

    private final String nombre;
    private final double pagaExtra;
    private final String descripcion;

    Especialidad(String nombre, double pagaExtra, String descripcion) {
        this.nombre = nombre;
        this.pagaExtra = pagaExtra;
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPagaExtra() {
        return pagaExtra;
    }

    public String getDescripcion() {
        return descripcion;
    }
}

