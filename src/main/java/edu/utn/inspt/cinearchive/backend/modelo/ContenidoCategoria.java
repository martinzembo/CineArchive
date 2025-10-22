package edu.utn.inspt.cinearchive.backend.modelo;

import java.io.Serializable;

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
}

