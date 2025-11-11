package edu.utn.inspt.cinearchive.backend.modelo;

import java.io.Serializable;
import java.util.Objects;

public class ContenidoCategoria implements Serializable {

    private int contenidoId;
    private int categoriaId;

    public ContenidoCategoria() {
    }

    public ContenidoCategoria(int contenidoId, int categoriaId) {
        this.contenidoId = contenidoId;
        this.categoriaId = categoriaId;
    }

    public int getContenidoId() {
        return contenidoId;
    }

    public void setContenidoId(int contenidoId) {
        this.contenidoId = contenidoId;
    }

    public int getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(int categoriaId) {
        this.categoriaId = categoriaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ContenidoCategoria)) return false;
        ContenidoCategoria that = (ContenidoCategoria) o;
        return Objects.equals(getContenidoId(), that.getContenidoId()) && Objects.equals(getCategoriaId(), that.getCategoriaId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getContenidoId(), getCategoriaId());
    }

    @Override
    public String toString() {
        return "ContenidoCategoria{" +
                "contenidoId=" + contenidoId +
                ", categoriaId=" + categoriaId +
                '}';
    }
}
