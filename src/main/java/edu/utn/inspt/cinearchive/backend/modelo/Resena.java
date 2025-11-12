package edu.utn.inspt.cinearchive.backend.modelo;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Clase que representa una reseña de un contenido realizada por un usuario.
 * Incluye calificación, título, texto y fechas de creación/modificación.
 */
public class Resena implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull(message = "El usuario es obligatorio")
    private Usuario usuario;

    @NotNull(message = "El contenido es obligatorio")
    private Contenido contenido;

    @DecimalMin(value = "0.0", message = "La calificación no puede ser menor a 0")
    @DecimalMax(value = "5.0", message = "La calificación no puede ser mayor a 5")
    @NotNull(message = "La calificación es obligatoria")
    private Double calificacion;

    @NotNull(message = "El título es obligatorio")
    @Size(min = 3, max = 100, message = "El título debe tener entre 3 y 100 caracteres")
    private String titulo;

    @NotNull(message = "El texto es obligatorio")
    @Size(max = 2000, message = "El texto no puede exceder los 2000 caracteres")
    private String texto;

    private LocalDate fechaCreacion;

    private LocalDate fechaModificacion;

    public Resena() {
    }

    public Resena(Usuario usuario, Contenido contenido, Double calificacion, String titulo, String texto) {
        this.usuario = usuario;
        this.contenido = contenido;
        this.calificacion = calificacion;
        this.titulo = titulo;
        this.texto = texto;
        this.fechaCreacion = LocalDate.now();
    }

    /**
     * Establece la fecha de creación
     */
    public void onCreate() {
        fechaCreacion = LocalDate.now();
    }

    /**
     * Establece la fecha de modificación
     */
    public void onUpdate() {
        fechaModificacion = LocalDate.now();
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Contenido getContenido() {
        return contenido;
    }

    public void setContenido(Contenido contenido) {
        this.contenido = contenido;
    }

    public Double getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Double calificacion) {
        this.calificacion = calificacion;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDate getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(LocalDate fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Resena)) return false;
        Resena resena = (Resena) o;
        return id != null && id.equals(resena.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Resena{" +
                "id=" + id +
                ", usuarioId=" + (usuario != null ? usuario.getId() : null) +
                ", contenidoId=" + (contenido != null ? contenido.getId() : null) +
                ", calificacion=" + calificacion +
                ", titulo='" + titulo + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }
}
