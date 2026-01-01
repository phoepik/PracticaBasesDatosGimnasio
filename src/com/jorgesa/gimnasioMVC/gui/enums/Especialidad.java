package com.jorgesa.gimnasioMVC.gui.enums;

public enum Especialidad {
    BOXEO(1, "Boxeo", 40.00, "Entrenamiento de boxeo con saco y técnicas de combate"),
    FUTBOL(2, "Futbol", 10.00, "Entrenamiento de fútbol"),
    TENNIS(3, "Tennis", 15.00, "Clases de tenis"),
    SPINNING(4, "Spinning", 12.00, "Sesiones de bicicleta estática con música"),
    YOGA(5, "Yoga", 8.00, "Clases de yoga para flexibilidad, respiración y relajación"),
    PILATES(6, "Pilates", 33.00, "Ejercicios de fortalecimiento y tonificación del core"),
    CROSSFIT(7, "Crossfit", 20.00, "Entrenamientos funcionales de alta intensidad"),
    NATACION(8, "Natación", 18.00, "Clases de natación"),
    BAILE(9, "Baile", 12.00, "Clases de baile y coordinación");

    private int idEspecialidad;
    private String nombre;
    private double pagaExtra;
    private String descripcion;

    Especialidad(int idEspecialidad, String nombre, double pagaExtra, String descripcion) {
        this.idEspecialidad = idEspecialidad;
        this.nombre = nombre;
        this.pagaExtra = pagaExtra;
        this.descripcion = descripcion;
    }

    public int getIdEspecialidad() {
        return idEspecialidad;
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

