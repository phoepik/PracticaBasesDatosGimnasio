package com.jorgesa.gimnasioMVC.gui.enums;

public enum Especialidad {
    BOXEO(1, "Boxeo", 40.00, "Boxeo con saco"),
    FUTBOL(2, "Futbol", 10.00, "Entrenamiento fútbol"),
    TENNIS(3, "Tennis", 15.00, "Clases de tenis"),
    SPINNING(4, "Spinning", 12.00, "Bicicleta estática"),
    YOGA(5, "Yoga", 8.00, "Yoga y relajación"),
    PILATES(6, "Pilates", 33.00, "Fortalecimiento core"),
    CROSSFIT(7, "Crossfit", 20.00, "Alta intensidad"),
    NATACION(8, "Natación", 18.00, "Clases natación"),
    BAILE(9, "Baile", 12.00, "Baile y coordinación");

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

