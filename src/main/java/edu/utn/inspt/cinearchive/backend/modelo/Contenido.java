package edu.utn.inspt.cinearchive.backend.modelo;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "contenido")
public class Contenido implements Serializable {

    public enum Tipo {
        PELICULA,
        SERIE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "genero")
    private String genero;

    @Min(1880) @Max(2100)
    @Column(name = "anio")
    private Integer anio;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "imagen_url")
    private String imagenUrl;

    @Column(name = "trailer_url")
    private String trailerUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private Tipo tipo;

    @Column(name = "disponible_para_alquiler")
    private Boolean disponibleParaAlquiler;

    @Column(name = "precio_alquiler")
    private BigDecimal precioAlquiler;

    @Min(0)
    @Column(name = "copias_disponibles")
    private Integer copiasDisponibles;

    @Min(0)
    @Column(name = "copias_totales")
    private Integer copiasTotales;

    @Column(name = "fecha_vencimiento_licencia")
    private LocalDate fechaVencimientoLicencia;

    @Column(name = "id_api_externa")
    private String idApiExterna;

    @Column(name = "gestor_inventario_id")
    private Long gestorInventarioId;

    // Atributos específicos de Películas
    @Min(1)
    @Column(name = "duracion")
    private Integer duracion;

    @Column(name = "director")
    private String director;

    // Atributos específicos de Series
    @Column(name = "temporadas")
    private Integer temporadas;

    @Column(name = "capitulos_totales")
    private Integer capitulosTotales;

    @Column(name = "en_emision")
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
