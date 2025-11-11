package edu.utn.inspt.cinearchive.backend.modelo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class ListaContenido implements Serializable {

    private Long listaId;
    private Long contenidoId;

    private Integer orden;

    private LocalDateTime fechaAgregado;

    public ListaContenido() {
    }

    public ListaContenido(Long listaId, Long contenidoId, Integer orden, LocalDateTime fechaAgregado) {
        this.listaId = listaId;
        this.contenidoId = contenidoId;
        this.orden = orden;
        this.fechaAgregado = fechaAgregado;
    }

    public Long getListaId() {
        return listaId;
    }

    public void setListaId(Long listaId) {
        this.listaId = listaId;
    }

    public Long getContenidoId() {
        return contenidoId;
    }

    public void setContenidoId(Long contenidoId) {
        this.contenidoId = contenidoId;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public LocalDateTime getFechaAgregado() {
        return fechaAgregado;
    }

    public void setFechaAgregado(LocalDateTime fechaAgregado) {
        this.fechaAgregado = fechaAgregado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ListaContenido)) return false;
        ListaContenido that = (ListaContenido) o;
        return Objects.equals(getListaId(), that.getListaId()) && Objects.equals(getContenidoId(), that.getContenidoId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getListaId(), getContenidoId());
    }

    @Override
    public String toString() {
        return "ListaContenido{" +
                "listaId=" + listaId +
                ", contenidoId=" + contenidoId +
                ", orden=" + orden +
                '}';
    }
}
