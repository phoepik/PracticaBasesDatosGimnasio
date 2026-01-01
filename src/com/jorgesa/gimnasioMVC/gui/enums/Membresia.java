package com.jorgesa.gimnasioMVC.gui.enums;

public enum Membresia {
    BASICA(1,"Basica", 29.99, "La membresía más básica, no incluye clases ni entrenador"),
    PREMIUM(2,"Premium", 39.99, "Una membresía para los que necesiten un entrenador"),
    ULTIMATE(3,"Ultimate",49.99, "La membresía más completa, incluye entrenador, y las clases que quieras");

    private int idMembresia;
    private String nombre;
    private double cuotaMensual;
    private String descripcion;

    Membresia(int idMembresia, String nombre, double precio, String descripcion) {
        this.idMembresia = idMembresia;
        this.nombre = nombre;
        this.cuotaMensual = precio;
        this.descripcion = descripcion;
    }

    public int getIdMembresia() {
        return idMembresia;
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
