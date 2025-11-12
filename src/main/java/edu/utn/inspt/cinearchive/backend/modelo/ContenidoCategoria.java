package edu.utn.inspt.cinearchive.backend.modelo;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * Clase que representa la relación muchos a muchos entre Contenido y Categoria.
 * Esta es una tabla de unión con atributos adicionales que permite asociar contenidos con categorías.
 */
public class ContenidoCategoria implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long contenidoId;

    private Long categoriaId;

    @NotNull(message = "El contenido es obligatorio")
    private Contenido contenido;

    @NotNull(message = "La categoría es obligatoria")
    private Categoria categoria;

    public ContenidoCategoria() {
    }

    public ContenidoCategoria(Contenido contenido, Categoria categoria) {
        this.contenido = contenido;
        this.categoria = categoria;
        this.contenidoId = contenido.getId();
        this.categoriaId = categoria.getId();
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

    public Contenido getContenido() {
        return contenido;
    }

    public void setContenido(Contenido contenido) {
        this.contenido = contenido;
        if (contenido != null) {
            this.contenidoId = contenido.getId();
        }
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
        if (categoria != null) {
            this.categoriaId = categoria.getId();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ContenidoCategoria)) return false;
        ContenidoCategoria that = (ContenidoCategoria) o;
        return Objects.equals(contenidoId, that.contenidoId) &&
               Objects.equals(categoriaId, that.categoriaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contenidoId, categoriaId);
    }

    @Override
    public String toString() {
        return "ContenidoCategoria{" +
                "contenidoId=" + contenidoId +
                ", categoriaId=" + categoriaId +
                '}';
    }

    /**
     * Valida que los IDs coincidan con los objetos relacionados
     */
    public void validarIds() {
        if (contenido != null && !Objects.equals(contenidoId, contenido.getId())) {
            throw new IllegalStateException("El ID del contenido no coincide con la relación");
        }
        if (categoria != null && !Objects.equals(categoriaId, categoria.getId())) {
            throw new IllegalStateException("El ID de la categoría no coincide con la relación");
        }
    }
}
