package edu.utn.inspt.cinearchive.modelo;

import java.io.Serializable;
import java.time.LocalDate;

public class Contenido implements Serializable {

    public enum Tipo {
        PELICULA,
        SERIE
    }

    private int id;
    private String titulo;
    private String genero;
    private int anio;
    private String descripcion;
    private String imagenUrl;
    private String trailerUrl;
    private Tipo tipo;
    private Boolean disponibleParaAlquiler;
    private float precioAlquiler;
    private int copiasDisponibles;
    private int copiasTotales;
    private LocalDate fechaVencimientoLicencia;
    private String idApiExterna;
    private int gestorInventarioId;

    // Atributos específicos de Películas
    private int duracion;
    private String director;

    // Atributos específicos de Series
    private int temporadas;
    private int capitulosTotales;
    private Boolean enEmision;

    public Contenido() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
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

    public float getPrecioAlquiler() {
        return precioAlquiler;
    }

    public void setPrecioAlquiler(float precioAlquiler) {
        this.precioAlquiler = precioAlquiler;
    }

    public int getCopiasDisponibles() {
        return copiasDisponibles;
    }

    public void setCopiasDisponibles(int copiasDisponibles) {
        this.copiasDisponibles = copiasDisponibles;
    }

    public int getCopiasTotales() {
        return copiasTotales;
    }

    public void setCopiasTotales(int copiasTotales) {
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

    public int getGestorInventarioId() {
        return gestorInventarioId;
    }

    public void setGestorInventarioId(int gestorInventarioId) {
        this.gestorInventarioId = gestorInventarioId;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public int getTemporadas() {
        return temporadas;
    }

    public void setTemporadas(int temporadas) {
        this.temporadas = temporadas;
    }

    public int getCapitulosTotales() {
        return capitulosTotales;
    }

    public void setCapitulosTotales(int capitulosTotales) {
        this.capitulosTotales = capitulosTotales;
    }

    public Boolean getEnEmision() {
        return enEmision;
    }

    public void setEnEmision(Boolean enEmision) {
        this.enEmision = enEmision;
    }
}

