package edu.utn.inspt.cinearchive.backend.modelo;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * Clase que representa un contenido multimedia (película o serie) en el sistema.
 * Contiene toda la información relevante para la gestión del catálogo y alquileres.
 */
public class Contenido implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum Tipo {
        PELICULA,
        SERIE
    }

    private Long id;

    @NotNull(message = "El título es obligatorio")
    @Size(min = 1, max = 255, message = "El título debe tener entre 1 y 255 caracteres")
    private String titulo;

    private String genero;

    @NotNull(message = "El año es obligatorio")
    @Min(value = 1900, message = "El año debe ser mayor a 1900")
    private Integer anio;

    @Size(max = 2000, message = "La descripción no puede exceder los 2000 caracteres")
    private String descripcion;

    private String imagenUrl;

    private String trailerUrl;

    @NotNull(message = "El tipo de contenido es obligatorio")
    private Tipo tipo;

    private Boolean disponibleParaAlquiler = true;

    @DecimalMin(value = "0.0", message = "El precio no puede ser negativo")
    private BigDecimal precioAlquiler;

    @Min(value = 0, message = "Las copias disponibles no pueden ser negativas")
    private Integer copiasDisponibles = 0;

    @Min(value = 0, message = "Las copias totales no pueden ser negativas")
    private Integer copiasTotales = 0;

    private LocalDate fechaVencimientoLicencia;

    private String idApiExterna;

    private Long gestorInventarioId;

    private Integer duracion;

    private String director;

    private Integer temporadas;

    private Integer capitulosTotales;

    private Boolean enEmision;

    private Set<ContenidoCategoria> categorias = new HashSet<>();

    // Constructores
    public Contenido() {
    }

    public Contenido(String titulo, Integer anio, Tipo tipo) {
        this.titulo = titulo;
        this.anio = anio;
        this.tipo = tipo;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public String getTrailerUrl() {
        return trailerUrl;
    }

    public void setTrailerUrl(String trailerUrl) {
        this.trailerUrl = trailerUrl;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public Boolean getDisponibleParaAlquiler() {
        return disponibleParaAlquiler;
    }

    public void setDisponibleParaAlquiler(Boolean disponibleParaAlquiler) {
        this.disponibleParaAlquiler = disponibleParaAlquiler;
    }

    public BigDecimal getPrecioAlquiler() {
        return precioAlquiler;
    }

    public void setPrecioAlquiler(BigDecimal precioAlquiler) {
        this.precioAlquiler = precioAlquiler;
    }

    public Integer getCopiasDisponibles() {
        return copiasDisponibles;
    }

    public void setCopiasDisponibles(Integer copiasDisponibles) {
        this.copiasDisponibles = copiasDisponibles;
    }

    public Integer getCopiasTotales() {
        return copiasTotales;
    }

    public void setCopiasTotales(Integer copiasTotales) {
        this.copiasTotales = copiasTotales;
    }

    public LocalDate getFechaVencimientoLicencia() {
        return fechaVencimientoLicencia;
    }

    public void setFechaVencimientoLicencia(LocalDate fechaVencimientoLicencia) {
        this.fechaVencimientoLicencia = fechaVencimientoLicencia;
    }

    public String getIdApiExterna() {
        return idApiExterna;
    }

    public void setIdApiExterna(String idApiExterna) {
        this.idApiExterna = idApiExterna;
    }

    public Long getGestorInventarioId() {
        return gestorInventarioId;
    }

    public void setGestorInventarioId(Long gestorInventarioId) {
        this.gestorInventarioId = gestorInventarioId;
    }

    public Integer getDuracion() {
        return duracion;
    }

    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public Integer getTemporadas() {
        return temporadas;
    }

    public void setTemporadas(Integer temporadas) {
        this.temporadas = temporadas;
    }

    public Integer getCapitulosTotales() {
        return capitulosTotales;
    }

    public void setCapitulosTotales(Integer capitulosTotales) {
        this.capitulosTotales = capitulosTotales;
    }

    public Boolean getEnEmision() {
        return enEmision;
    }

    public void setEnEmision(Boolean enEmision) {
        this.enEmision = enEmision;
    }

    /**
     * Agrega una categoría al contenido
     */
    public void agregarCategoria(Categoria categoria) {
        ContenidoCategoria contenidoCategoria = new ContenidoCategoria(this, categoria);
        categorias.add(contenidoCategoria);
    }

    /**
     * Remueve una categoría del contenido
     */
    public void removerCategoria(Categoria categoria) {
        categorias.removeIf(cc -> cc.getCategoria().equals(categoria));
    }

    /**
     * Verifica si hay copias disponibles para alquiler
     */
    public boolean tieneCopiasDisponibles() {
        return copiasDisponibles != null && copiasDisponibles > 0;
    }

    /**
     * Verifica si el contenido está disponible para alquiler
     */
    public boolean estaDisponibleParaAlquiler() {
        return disponibleParaAlquiler != null &&
               disponibleParaAlquiler &&
               tieneCopiasDisponibles() &&
               (fechaVencimientoLicencia == null ||
                fechaVencimientoLicencia.isAfter(LocalDate.now()));
    }

    /**
     * Reduce el número de copias disponibles en uno
     */
    public void reducirCopiasDisponibles() {
        if (copiasDisponibles > 0) {
            copiasDisponibles--;
        }
    }

    /**
     * Aumenta el número de copias disponibles en uno
     */
    public void aumentarCopiasDisponibles() {
        if (copiasDisponibles < copiasTotales) {
            copiasDisponibles++;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Contenido)) return false;
        Contenido contenido = (Contenido) o;
        return Objects.equals(id, contenido.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Contenido{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", tipo=" + tipo +
                ", anio=" + anio +
                ", disponibleParaAlquiler=" + disponibleParaAlquiler +
                ", copiasDisponibles=" + copiasDisponibles +
                '}';
    }

    /**
     * Valida el estado del contenido
     */
    public void validar() {
        if (copiasDisponibles > copiasTotales) {
            throw new IllegalStateException("Las copias disponibles no pueden superar a las totales");
        }
        if (tipo == Tipo.SERIE && temporadas != null && temporadas <= 0) {
            throw new IllegalStateException("Una serie debe tener al menos una temporada");
        }
        if (tipo == Tipo.PELICULA && duracion != null && duracion <= 0) {
            throw new IllegalStateException("Una película debe tener una duración positiva");
        }
    }
}
