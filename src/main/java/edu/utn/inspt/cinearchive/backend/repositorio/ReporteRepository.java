package edu.utn.inspt.cinearchive.backend.repositorio;

import edu.utn.inspt.cinearchive.backend.modelo.Reporte;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ReporteRepository {
    // Métodos CRUD básicos
    List<Reporte> findAll();
    Optional<Reporte> findById(Integer id);
    List<Reporte> findByAnalistaId(Integer analistaId);
    List<Reporte> findByTipoReporte(Reporte.TipoReporte tipo);
    List<Reporte> findByFechaGeneracionBetween(LocalDate fechaInicio, LocalDate fechaFin);
    Reporte save(Reporte reporte);
    void deleteById(Integer id);
    boolean existsById(Integer id);

    // Métodos específicos para analytics y reportes
    List<Map<String, Object>> obtenerContenidosMasAlquilados(LocalDate fechaInicio, LocalDate fechaFin, int limite);
    List<Map<String, Object>> obtenerAnalisisDemografico(LocalDate fechaInicio, LocalDate fechaFin);
    List<Map<String, Object>> obtenerRendimientoGeneros(LocalDate fechaInicio, LocalDate fechaFin);
    List<Map<String, Object>> obtenerTendenciasTemporales(LocalDate fechaInicio, LocalDate fechaFin);
    List<Map<String, Object>> obtenerComportamientoUsuarios(LocalDate fechaInicio, LocalDate fechaFin);
    Map<String, Object> obtenerEstadisticasGenerales();
    List<Map<String, Object>> obtenerCategoriasPopulares(int limite);
    List<Map<String, Object>> obtenerContenidosMejorCalificados(int limite);
}
