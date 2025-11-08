package edu.utn.inspt.cinearchive.backend.modelo;

import java.io.Serializable;
import java.util.Objects;

/**
 * Clase identificadora compuesta para la relación entre Contenido y Categoria.
 * Implementa Serializable para permitir la serialización JPA.
 */
public class ContenidoCategoriaId implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long contenidoId;
    private Long categoriaId;

    public ContenidoCategoriaId() {
    }

    public ContenidoCategoriaId(Long contenidoId, Long categoriaId) {
        this.contenidoId = contenidoId;
        this.categoriaId = categoriaId;
    }

    public Long getContenidoId() {
        return contenidoId;
    }

    public void setContenidoId(Long contenidoId) {
        this.contenidoId = contenidoId;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContenidoCategoriaId that = (ContenidoCategoriaId) o;
        return Objects.equals(contenidoId, that.contenidoId) &&
               Objects.equals(categoriaId, that.categoriaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contenidoId, categoriaId);
    }

    @Override
    public String toString() {
        return "ContenidoCategoriaId{" +
                "contenidoId=" + contenidoId +
                ", categoriaId=" + categoriaId +
                '}';
    }
}
