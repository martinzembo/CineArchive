package edu.utn.inspt.cinearchive.backend.modelo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class Lista implements Serializable {

    private Long id;

    private Long usuarioId;

    private String nombre;

    private String descripcion;

    private Boolean publica;

    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;

    public Lista() {
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getPublica() {
        return publica;
    }

    public void setPublica(Boolean publica) {
        this.publica = publica;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(LocalDateTime fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Lista)) return false;
        Lista lista = (Lista) o;
        return Objects.equals(getId(), lista.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Lista{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
