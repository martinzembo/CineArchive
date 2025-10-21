package edu.utn.inspt.cinearchive.modelo;

import java.io.Serializable;

public class Categoria implements Serializable {

    public enum Tipo {
        GENERO,
        TAG,
        CLASIFICACION
    }

    private int id;
    private String nombre;
    private Tipo tipo;
    private String descripcion;

    public Categoria() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}

