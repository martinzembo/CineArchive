package edu.utn.inspt.cinearchive.backend.servicio;

import edu.utn.inspt.cinearchive.backend.modelo.Reporte;
import edu.utn.inspt.cinearchive.backend.repositorio.ReporteRepositoryImpl;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Servicio que maneja la lógica de negocio relacionada con los reportes y analytics
 */
@Service
@Transactional
public class ReporteServiceImpl implements ReporteService {

    private final ReporteRepositoryImpl reporteRepository;
    private final Gson gson;

    @Autowired
    public ReporteServiceImpl(ReporteRepositoryImpl reporteRepository) {
        this.reporteRepository = reporteRepository;
        this.gson = new Gson();
    }

    // ==================== CRUD BÁSICO ====================

    @Override
    @Transactional(readOnly = true)
    public List<Reporte> obtenerTodos() {
        return reporteRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Reporte> obtenerPorId(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID del reporte debe ser válido");
        }
        return reporteRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reporte> obtenerPorAnalista(Integer analistaId) {
        if (analistaId == null || analistaId <= 0) {
            throw new IllegalArgumentException("El ID del analista debe ser válido");
        }
        return reporteRepository.findByAnalistaId(analistaId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reporte> obtenerPorTipo(Reporte.TipoReporte tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de reporte es obligatorio");
        }
        return reporteRepository.findByTipoReporte(tipo);
    }

    @Override
    public Reporte guardar(Reporte reporte) {
        validarReporte(reporte);
        return reporteRepository.save(reporte);
    }

    @Override
    public void eliminar(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID del reporte debe ser válido");
        }
        if (!reporteRepository.existsById(id)) {
            throw new IllegalArgumentException("No existe un reporte con el ID: " + id);
        }
        reporteRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorId(Integer id) {
        if (id == null || id <= 0) {
            return false;
        }
        return reporteRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reporte> obtenerPorPeriodo(LocalDate fechaInicio, LocalDate fechaFin) {
        validarPeriodo(fechaInicio, fechaFin);
        return reporteRepository.findByFechaGeneracionBetween(fechaInicio, fechaFin);
    }

    // ==================== GENERACIÓN DE REPORTES ====================

    @Override
    public Reporte generarReporteContenidosMasAlquilados(Integer analistaId, LocalDate fechaInicio, LocalDate fechaFin, int limite) {
        validarParametrosReporte(analistaId, fechaInicio, fechaFin);
        if (limite <= 0 || limite > 100) {
            throw new IllegalArgumentException("El límite debe estar entre 1 y 100");
        }

        List<Map<String, Object>> datos = reporteRepository.obtenerContenidosMasAlquilados(fechaInicio, fechaFin, limite);

        JsonObject parametros = new JsonObject();
        parametros.addProperty("fechaInicio", fechaInicio.toString());
        parametros.addProperty("fechaFin", fechaFin.toString());
        parametros.addProperty("limite", limite);

        Reporte reporte = new Reporte();
        reporte.setAnalistaId(analistaId);
        reporte.setTitulo("Contenidos Más Alquilados");
        reporte.setDescripcion("Reporte de los " + limite + " contenidos más alquilados del " +
                              fechaInicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " al " +
                              fechaFin.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        reporte.setTipoReporte(Reporte.TipoReporte.MAS_ALQUILADOS);
        reporte.setParametros(parametros.toString());
        reporte.setResultados(gson.toJson(datos));
        reporte.setPeriodoInicio(fechaInicio);
        reporte.setPeriodoFin(fechaFin);

        return guardar(reporte);
    }

    @Override
    public Reporte generarReporteAnalisisDemografico(Integer analistaId, LocalDate fechaInicio, LocalDate fechaFin) {
        validarParametrosReporte(analistaId, fechaInicio, fechaFin);

        List<Map<String, Object>> datos = reporteRepository.obtenerAnalisisDemografico(fechaInicio, fechaFin);

        JsonObject parametros = new JsonObject();
        parametros.addProperty("fechaInicio", fechaInicio.toString());
        parametros.addProperty("fechaFin", fechaFin.toString());

        Reporte reporte = new Reporte();
        reporte.setAnalistaId(analistaId);
        reporte.setTitulo("Análisis Demográfico");
        reporte.setDescripcion("Análisis demográfico de usuarios por género y edad del " +
                              fechaInicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " al " +
                              fechaFin.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        reporte.setTipoReporte(Reporte.TipoReporte.ANALISIS_DEMOGRAFICO);
        reporte.setParametros(parametros.toString());
        reporte.setResultados(gson.toJson(datos));
        reporte.setPeriodoInicio(fechaInicio);
        reporte.setPeriodoFin(fechaFin);

        return guardar(reporte);
    }

    @Override
    public Reporte generarReporteRendimientoGeneros(Integer analistaId, LocalDate fechaInicio, LocalDate fechaFin) {
        validarParametrosReporte(analistaId, fechaInicio, fechaFin);

        List<Map<String, Object>> datos = reporteRepository.obtenerRendimientoGeneros(fechaInicio, fechaFin);

        JsonObject parametros = new JsonObject();
        parametros.addProperty("fechaInicio", fechaInicio.toString());
        parametros.addProperty("fechaFin", fechaFin.toString());

        Reporte reporte = new Reporte();
        reporte.setAnalistaId(analistaId);
        reporte.setTitulo("Rendimiento de Géneros");
        reporte.setDescripcion("Análisis de rendimiento por géneros cinematográficos del " +
                              fechaInicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " al " +
                              fechaFin.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        reporte.setTipoReporte(Reporte.TipoReporte.RENDIMIENTO_GENEROS);
        reporte.setParametros(parametros.toString());
        reporte.setResultados(gson.toJson(datos));
        reporte.setPeriodoInicio(fechaInicio);
        reporte.setPeriodoFin(fechaFin);

        return guardar(reporte);
    }

    @Override
    public Reporte generarReporteTendenciasTemporales(Integer analistaId, LocalDate fechaInicio, LocalDate fechaFin) {
        validarParametrosReporte(analistaId, fechaInicio, fechaFin);

        List<Map<String, Object>> datos = reporteRepository.obtenerTendenciasTemporales(fechaInicio, fechaFin);

        JsonObject parametros = new JsonObject();
        parametros.addProperty("fechaInicio", fechaInicio.toString());
        parametros.addProperty("fechaFin", fechaFin.toString());

        Reporte reporte = new Reporte();
        reporte.setAnalistaId(analistaId);
        reporte.setTitulo("Tendencias Temporales");
        reporte.setDescripcion("Análisis de tendencias de alquileres por período del " +
                              fechaInicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " al " +
                              fechaFin.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        reporte.setTipoReporte(Reporte.TipoReporte.TENDENCIAS_TEMPORALES);
        reporte.setParametros(parametros.toString());
        reporte.setResultados(gson.toJson(datos));
        reporte.setPeriodoInicio(fechaInicio);
        reporte.setPeriodoFin(fechaFin);

        return guardar(reporte);
    }

    @Override
    public Reporte generarReporteComportamientoUsuarios(Integer analistaId, LocalDate fechaInicio, LocalDate fechaFin) {
        validarParametrosReporte(analistaId, fechaInicio, fechaFin);

        List<Map<String, Object>> datos = reporteRepository.obtenerComportamientoUsuarios(fechaInicio, fechaFin);

        JsonObject parametros = new JsonObject();
        parametros.addProperty("fechaInicio", fechaInicio.toString());
        parametros.addProperty("fechaFin", fechaFin.toString());

        Reporte reporte = new Reporte();
        reporte.setAnalistaId(analistaId);
        reporte.setTitulo("Comportamiento de Usuarios");
        reporte.setDescripcion("Análisis de patrones de comportamiento de usuarios del " +
                              fechaInicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " al " +
                              fechaFin.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        reporte.setTipoReporte(Reporte.TipoReporte.COMPORTAMIENTO_USUARIOS);
        reporte.setParametros(parametros.toString());
        reporte.setResultados(gson.toJson(datos));
        reporte.setPeriodoInicio(fechaInicio);
        reporte.setPeriodoFin(fechaFin);

        return guardar(reporte);
    }

    // ==================== ANALYTICS Y CONSULTAS ====================

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> obtenerEstadisticasGenerales() {
        return reporteRepository.obtenerEstadisticasGenerales();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> obtenerTopContenidos(LocalDate fechaInicio, LocalDate fechaFin, int limite) {
        validarPeriodo(fechaInicio, fechaFin);
        if (limite <= 0 || limite > 100) {
            throw new IllegalArgumentException("El límite debe estar entre 1 y 100");
        }
        return reporteRepository.obtenerContenidosMasAlquilados(fechaInicio, fechaFin, limite);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> obtenerCategoriasPopulares(int limite) {
        if (limite <= 0 || limite > 50) {
            throw new IllegalArgumentException("El límite debe estar entre 1 y 50");
        }
        return reporteRepository.obtenerCategoriasPopulares(limite);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> obtenerContenidosMejorCalificados(int limite) {
        if (limite <= 0 || limite > 100) {
            throw new IllegalArgumentException("El límite debe estar entre 1 y 100");
        }
        return reporteRepository.obtenerContenidosMejorCalificados(limite);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> obtenerAnalisisDemografico(LocalDate fechaInicio, LocalDate fechaFin) {
        validarPeriodo(fechaInicio, fechaFin);
        return reporteRepository.obtenerAnalisisDemografico(fechaInicio, fechaFin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> obtenerRendimientoGeneros(LocalDate fechaInicio, LocalDate fechaFin) {
        validarPeriodo(fechaInicio, fechaFin);
        return reporteRepository.obtenerRendimientoGeneros(fechaInicio, fechaFin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> obtenerTendenciasTemporales(LocalDate fechaInicio, LocalDate fechaFin) {
        validarPeriodo(fechaInicio, fechaFin);
        return reporteRepository.obtenerTendenciasTemporales(fechaInicio, fechaFin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> obtenerComportamientoUsuarios(LocalDate fechaInicio, LocalDate fechaFin) {
        validarPeriodo(fechaInicio, fechaFin);
        return reporteRepository.obtenerComportamientoUsuarios(fechaInicio, fechaFin);
    }

    // ==================== MÉTODOS DE VALIDACIÓN ====================

    private void validarReporte(Reporte reporte) {
        if (reporte == null) {
            throw new IllegalArgumentException("El reporte es obligatorio");
        }
        if (reporte.getAnalistaId() <= 0) {
            throw new IllegalArgumentException("El ID del analista debe ser válido");
        }
        if (reporte.getTitulo() == null || reporte.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("El título del reporte es obligatorio");
        }
        if (reporte.getTipoReporte() == null) {
            throw new IllegalArgumentException("El tipo de reporte es obligatorio");
        }
        if (reporte.getPeriodoInicio() != null && reporte.getPeriodoFin() != null) {
            validarPeriodo(reporte.getPeriodoInicio(), reporte.getPeriodoFin());
        }
    }

    private void validarParametrosReporte(Integer analistaId, LocalDate fechaInicio, LocalDate fechaFin) {
        if (analistaId == null || analistaId <= 0) {
            throw new IllegalArgumentException("El ID del analista debe ser válido");
        }
        validarPeriodo(fechaInicio, fechaFin);
    }

    private void validarPeriodo(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("Las fechas de inicio y fin son obligatorias");
        }
        if (fechaInicio.isAfter(fechaFin)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }
        if (fechaFin.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de fin no puede ser futura");
        }
        // Validar que el período no sea mayor a 2 años
        if (fechaInicio.plusYears(2).isBefore(fechaFin)) {
            throw new IllegalArgumentException("El período del reporte no puede ser mayor a 2 años");
        }
    }
}
