package edu.utn.inspt.cinearchive.backend.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Contenido implements Serializable {

    public enum Tipo {
        PELICULA,
        SERIE
    }

    private Long id;

    private String titulo;

    private String genero;

    private Integer anio;

    private String descripcion;

    private String imagenUrl;

    private String trailerUrl;

    private Tipo tipo;

    private Boolean disponibleParaAlquiler;

    private BigDecimal precioAlquiler;

    private Integer copiasDisponibles;

    private Integer copiasTotales;

    private LocalDate fechaVencimientoLicencia;

    private String idApiExterna;

    private Long gestorInventarioId;

    // Atributos específicos de Películas
    private Integer duracion;

    private String director;

    // Atributos específicos de Series
    private Integer temporadas;

    private Integer capitulosTotales;

    private Boolean enEmision;

    public Contenido() {
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Contenido)) return false;
        Contenido contenido = (Contenido) o;
        return Objects.equals(getId(), contenido.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Contenido{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", tipo=" + tipo +
                '}';
    }
}
