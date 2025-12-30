package com.jorgesa.gimnasioMVC.gui.enums;

public enum Membresia {
    BASICA("Basica", 29.99, "La membresía más básica, no incluye clases ni entrenador"),
    PREMIUM("Premium", 39.99, "Una membresía para los que necesiten un entrenador"),
    ULTIMATE("Ultimate",49.99, "La membresía más completa, incluye entrenador, y las clases que quieras");

    private String nombre;
    private double cuotaMensual;
    private String descripcion;

    Membresia(String nombre, double precio, String descripcion) {
        this.nombre = nombre;
        this.cuotaMensual = precio;
        this.descripcion = descripcion;
    }

    public String getNombre(){
        return nombre;
    }

    public double getCuotaMensual() {
        return cuotaMensual;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
