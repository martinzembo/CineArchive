package edu.utn.inspt.cinearchive.backend.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Alquiler implements Serializable {

    public enum Estado {
        ACTIVO,
        FINALIZADO,
        CANCELADO
    }

    private Long id;

    private Long usuarioId;

    private Long contenidoId;

    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;

    private Integer periodoAlquiler;

    private BigDecimal precio;

    private Estado estado;

    private Boolean visto;
    private LocalDateTime fechaVista;

    public Alquiler() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getContenidoId() {
        return contenidoId;
    }

    public void setContenidoId(Long contenidoId) {
        this.contenidoId = contenidoId;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Integer getPeriodoAlquiler() {
        return periodoAlquiler;
    }

    public void setPeriodoAlquiler(Integer periodoAlquiler) {
        this.periodoAlquiler = periodoAlquiler;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
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

    public LocalDateTime getFechaVista() {
        return fechaVista;
    }

    public void setFechaVista(LocalDateTime fechaVista) {
        this.fechaVista = fechaVista;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Alquiler)) return false;
        Alquiler alquiler = (Alquiler) o;
        return Objects.equals(getId(), alquiler.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Alquiler{" +
                "id=" + id +
                ", usuarioId=" + usuarioId +
                ", contenidoId=" + contenidoId +
                ", estado=" + estado +
                '}';
    }
}
