package edu.utn.inspt.cinearchive.backend.modelo;

import java.io.Serializable;
import java.time.LocalDate;

public class Reporte implements Serializable {

    public enum TipoReporte {
        MAS_ALQUILADOS,
        ANALISIS_DEMOGRAFICO,
        RENDIMIENTO_GENEROS,
        TENDENCIAS_TEMPORALES,
        COMPORTAMIENTO_USUARIOS
    }

    private int id;
    private int analistaId;
    private String titulo;
    private String descripcion;
    private TipoReporte tipoReporte;
    private String parametros;
    private String resultados;
    private LocalDate fechaGeneracion;
    private LocalDate periodoInicio;
    private LocalDate periodoFin;

    public Reporte() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAnalistaId() {
        return analistaId;
    }

    public void setAnalistaId(int analistaId) {
        this.analistaId = analistaId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public TipoReporte getTipoReporte() {
        return tipoReporte;
    }

    public void setTipoReporte(TipoReporte tipoReporte) {
        this.tipoReporte = tipoReporte;
    }

    public String getParametros() {
        return parametros;
    }

    public void setParametros(String parametros) {
        this.parametros = parametros;
    }

    public String getResultados() {
        return resultados;
    }

    public void setResultados(String resultados) {
        this.resultados = resultados;
    }

    public LocalDate getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(LocalDate fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    public LocalDate getPeriodoInicio() {
        return periodoInicio;
    }

    public void setPeriodoInicio(LocalDate periodoInicio) {
        this.periodoInicio = periodoInicio;
    }

    public LocalDate getPeriodoFin() {
        return periodoFin;
    }

    public void setPeriodoFin(LocalDate periodoFin) {
        this.periodoFin = periodoFin;
    }
}
