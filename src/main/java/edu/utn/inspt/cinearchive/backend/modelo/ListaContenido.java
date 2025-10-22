package edu.utn.inspt.cinearchive.backend.modelo;

import java.io.Serializable;
import java.time.LocalDate;

public class ListaContenido implements Serializable {

    private int listaId;
    private int contenidoId;
    private int orden;
    private LocalDate fechaAgregado;

    public ListaContenido() {
    }

    public ListaContenido(int listaId, int contenidoId, int orden, LocalDate fechaAgregado) {
        this.listaId = listaId;
        this.contenidoId = contenidoId;
        this.orden = orden;
        this.fechaAgregado = fechaAgregado;
    }

    public int getListaId() {
        return listaId;
    }

    public void setListaId(int listaId) {
        this.listaId = listaId;
    }

    public int getContenidoId() {
        return contenidoId;
    }

    public void setContenidoId(int contenidoId) {
        this.contenidoId = contenidoId;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public LocalDate getFechaAgregado() {
        return fechaAgregado;
    }

    public void setFechaAgregado(LocalDate fechaAgregado) {
        this.fechaAgregado = fechaAgregado;
    }
}

