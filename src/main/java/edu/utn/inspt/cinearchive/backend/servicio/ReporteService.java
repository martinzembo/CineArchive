package edu.utn.inspt.cinearchive.backend.servicio;

import edu.utn.inspt.cinearchive.backend.modelo.Reporte;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Interfaz que define las operaciones disponibles para el servicio de Reportes
 */
public interface ReporteService {

    // ==================== CRUD BÁSICO ====================

    List<Reporte> obtenerTodos();

    Optional<Reporte> obtenerPorId(Integer id);

    List<Reporte> obtenerPorAnalista(Integer analistaId);

    List<Reporte> obtenerPorTipo(Reporte.TipoReporte tipo);

    Reporte guardar(Reporte reporte);

    void eliminar(Integer id);

    boolean existePorId(Integer id);

    List<Reporte> obtenerPorPeriodo(LocalDate fechaInicio, LocalDate fechaFin);

    // ==================== GENERACIÓN DE REPORTES ====================

    /**
     * Genera un reporte de contenidos más alquilados
     */
    Reporte generarReporteContenidosMasAlquilados(Integer analistaId, LocalDate fechaInicio, LocalDate fechaFin, int limite);

    /**
     * Genera un reporte de análisis demográfico
     */
    Reporte generarReporteAnalisisDemografico(Integer analistaId, LocalDate fechaInicio, LocalDate fechaFin);

    /**
     * Genera un reporte de rendimiento de géneros
     */
    Reporte generarReporteRendimientoGeneros(Integer analistaId, LocalDate fechaInicio, LocalDate fechaFin);

    /**
     * Genera un reporte de tendencias temporales
     */
    Reporte generarReporteTendenciasTemporales(Integer analistaId, LocalDate fechaInicio, LocalDate fechaFin);

    /**
     * Genera un reporte de comportamiento de usuarios
     */
    Reporte generarReporteComportamientoUsuarios(Integer analistaId, LocalDate fechaInicio, LocalDate fechaFin);

    // ==================== ANALYTICS Y CONSULTAS ====================

    /**
     * Obtiene estadísticas generales del dashboard
     */
    Map<String, Object> obtenerEstadisticasGenerales();

    /**
     * Obtiene los contenidos más alquilados
     */
    List<Map<String, Object>> obtenerTopContenidos(LocalDate fechaInicio, LocalDate fechaFin, int limite);

    /**
     * Obtiene las categorías más populares
     */
    List<Map<String, Object>> obtenerCategoriasPopulares(int limite);

    /**
     * Obtiene los contenidos mejor calificados
     */
    List<Map<String, Object>> obtenerContenidosMejorCalificados(int limite);

    /**
     * Obtiene análisis demográfico en tiempo real
     */
    List<Map<String, Object>> obtenerAnalisisDemografico(LocalDate fechaInicio, LocalDate fechaFin);

    /**
     * Obtiene rendimiento de géneros
     */
    List<Map<String, Object>> obtenerRendimientoGeneros(LocalDate fechaInicio, LocalDate fechaFin);

    /**
     * Obtiene tendencias temporales
     */
    List<Map<String, Object>> obtenerTendenciasTemporales(LocalDate fechaInicio, LocalDate fechaFin);

    /**
     * Obtiene comportamiento de usuarios
     */
    List<Map<String, Object>> obtenerComportamientoUsuarios(LocalDate fechaInicio, LocalDate fechaFin);
}
