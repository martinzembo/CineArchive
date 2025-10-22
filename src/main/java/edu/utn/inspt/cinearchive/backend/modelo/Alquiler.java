package edu.utn.inspt.cinearchive.backend.modelo;

import java.io.Serializable;
import java.time.LocalDate;

public class Alquiler implements Serializable {

    public enum Estado {
        ACTIVO,
        FINALIZADO,
        CANCELADO
    }

    private int id;
    private int usuarioId;
    private int contenidoId;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private int periodoAlquiler;
    private float precio;
    private Estado estado;
    private Boolean visto;
    private LocalDate fechaVista;

    public Alquiler() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public int getContenidoId() {
        return contenidoId;
    }

    public void setContenidoId(int contenidoId) {
        this.contenidoId = contenidoId;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public int getPeriodoAlquiler() {
        return periodoAlquiler;
    }

    public void setPeriodoAlquiler(int periodoAlquiler) {
        this.periodoAlquiler = periodoAlquiler;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Boolean getVisto() {
        return visto;
    }

    public void setVisto(Boolean visto) {
        this.visto = visto;
    }

    public LocalDate getFechaVista() {
        return fechaVista;
    }

    public void setFechaVista(LocalDate fechaVista) {
        this.fechaVista = fechaVista;
    }
}

