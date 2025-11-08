package edu.utn.inspt.cinearchive.backend.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * Entidad que representa la relación muchos a muchos entre Contenido y Categoria.
 * Esta es una tabla de unión con atributos adicionales que permite asociar contenidos con categorías.
 */
@Entity
@Table(name = "contenido_categoria")
@IdClass(ContenidoCategoriaId.class)
public class ContenidoCategoria implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "contenido_id", nullable = false)
    private Long contenidoId;

    @Id
    @Column(name = "categoria_id", nullable = false)
    private Long categoriaId;

    @NotNull(message = "El contenido es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contenido_id", insertable = false, updatable = false)
    private Contenido contenido;

    @NotNull(message = "La categoría es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", insertable = false, updatable = false)
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

    @PrePersist
    @PreUpdate
    protected void validarIds() {
        if (contenido != null && !Objects.equals(contenidoId, contenido.getId())) {
            throw new IllegalStateException("El ID del contenido no coincide con la relación");
        }
        if (categoria != null && !Objects.equals(categoriaId, categoria.getId())) {
            throw new IllegalStateException("El ID de la categoría no coincide con la relación");
        }
    }
}
