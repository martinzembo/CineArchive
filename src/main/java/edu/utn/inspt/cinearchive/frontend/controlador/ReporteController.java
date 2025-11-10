package edu.utn.inspt.cinearchive.frontend.controlador;

import edu.utn.inspt.cinearchive.backend.modelo.Reporte;
import edu.utn.inspt.cinearchive.backend.servicio.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controlador para gestionar reportes y analytics
 */
@Controller
@RequestMapping("/reportes")
public class ReporteController {

    private final ReporteService reporteService;

    @Autowired
    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    // ==================== ENDPOINTS REST (JSON) ====================

    /**
     * Obtener todos los reportes
     */
    @GetMapping("/api")
    @ResponseBody
    public ResponseEntity<List<Reporte>> listarReportes() {
        try {
            List<Reporte> reportes = reporteService.obtenerTodos();
            return ResponseEntity.ok(reportes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Obtener reporte por ID
     */
    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Reporte> obtenerReporte(@PathVariable Integer id) {
        try {
            Optional<Reporte> reporte = reporteService.obtenerPorId(id);
            return reporte.map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Crear nuevo reporte
     */
    @PostMapping("/api")
    @ResponseBody
    public ResponseEntity<Reporte> crearReporte(@Valid @RequestBody Reporte reporte) {
        try {
            Reporte reporteGuardado = reporteService.guardar(reporte);
            return ResponseEntity.status(HttpStatus.CREATED).body(reporteGuardado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Eliminar reporte
     */
    @DeleteMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Void> eliminarReporte(@PathVariable Integer id) {
        try {
            reporteService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Obtener reportes por analista
     */
    @GetMapping("/api/analista/{analistaId}")
    @ResponseBody
    public ResponseEntity<List<Reporte>> obtenerReportesPorAnalista(@PathVariable Integer analistaId) {
        try {
            List<Reporte> reportes = reporteService.obtenerPorAnalista(analistaId);
            return ResponseEntity.ok(reportes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Obtener reportes por tipo
     */
    @GetMapping("/api/tipo/{tipo}")
    @ResponseBody
    public ResponseEntity<List<Reporte>> obtenerReportesPorTipo(@PathVariable String tipo) {
        try {
            Reporte.TipoReporte tipoReporte = Reporte.TipoReporte.valueOf(tipo.toUpperCase());
            List<Reporte> reportes = reporteService.obtenerPorTipo(tipoReporte);
            return ResponseEntity.ok(reportes);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ==================== GENERACIÓN DE REPORTES ====================

    /**
     * Generar reporte de contenidos más alquilados
     */
    @PostMapping("/api/generar/mas-alquilados")
    @ResponseBody
    public ResponseEntity<Reporte> generarReporteContenidosMasAlquilados(
            @RequestParam Integer analistaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestParam(defaultValue = "20") int limite) {
        try {
            Reporte reporte = reporteService.generarReporteContenidosMasAlquilados(
                analistaId, fechaInicio, fechaFin, limite);
            return ResponseEntity.ok(reporte);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Generar reporte de análisis demográfico
     */
    @PostMapping("/api/generar/demografico")
    @ResponseBody
    public ResponseEntity<Reporte> generarReporteAnalisisDemografico(
            @RequestParam Integer analistaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        try {
            Reporte reporte = reporteService.generarReporteAnalisisDemografico(
                analistaId, fechaInicio, fechaFin);
            return ResponseEntity.ok(reporte);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Generar reporte de rendimiento de géneros
     */
    @PostMapping("/api/generar/generos")
    @ResponseBody
    public ResponseEntity<Reporte> generarReporteRendimientoGeneros(
            @RequestParam Integer analistaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        try {
            Reporte reporte = reporteService.generarReporteRendimientoGeneros(
                analistaId, fechaInicio, fechaFin);
            return ResponseEntity.ok(reporte);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Generar reporte de tendencias temporales
     */
    @PostMapping("/api/generar/tendencias")
    @ResponseBody
    public ResponseEntity<Reporte> generarReporteTendenciasTemporales(
            @RequestParam Integer analistaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        try {
            Reporte reporte = reporteService.generarReporteTendenciasTemporales(
                analistaId, fechaInicio, fechaFin);
            return ResponseEntity.ok(reporte);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Generar reporte de comportamiento de usuarios
     */
    @PostMapping("/api/generar/comportamiento")
    @ResponseBody
    public ResponseEntity<Reporte> generarReporteComportamientoUsuarios(
            @RequestParam Integer analistaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        try {
            Reporte reporte = reporteService.generarReporteComportamientoUsuarios(
                analistaId, fechaInicio, fechaFin);
            return ResponseEntity.ok(reporte);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ==================== ANALYTICS EN TIEMPO REAL ====================

    /**
     * Dashboard de estadísticas generales
     */
    @GetMapping("/api/dashboard")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> obtenerDashboard() {
        try {
            Map<String, Object> estadisticas = reporteService.obtenerEstadisticasGenerales();
            return ResponseEntity.ok(estadisticas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Top contenidos más alquilados
     */
    @GetMapping("/api/analytics/top-contenidos")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> obtenerTopContenidos(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestParam(defaultValue = "10") int limite) {
        try {
            List<Map<String, Object>> datos = reporteService.obtenerTopContenidos(
                fechaInicio, fechaFin, limite);
            return ResponseEntity.ok(datos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Categorías más populares
     */
    @GetMapping("/api/analytics/categorias-populares")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> obtenerCategoriasPopulares(
            @RequestParam(defaultValue = "10") int limite) {
        try {
            List<Map<String, Object>> datos = reporteService.obtenerCategoriasPopulares(limite);
            return ResponseEntity.ok(datos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Contenidos mejor calificados
     */
    @GetMapping("/api/analytics/mejor-calificados")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> obtenerContenidosMejorCalificados(
            @RequestParam(defaultValue = "10") int limite) {
        try {
            List<Map<String, Object>> datos = reporteService.obtenerContenidosMejorCalificados(limite);
            return ResponseEntity.ok(datos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Análisis demográfico en tiempo real
     */
    @GetMapping("/api/analytics/demografico")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> obtenerAnalisisDemografico(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        try {
            List<Map<String, Object>> datos = reporteService.obtenerAnalisisDemografico(
                fechaInicio, fechaFin);
            return ResponseEntity.ok(datos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Rendimiento de géneros
     */
    @GetMapping("/api/analytics/generos")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> obtenerRendimientoGeneros(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        try {
            List<Map<String, Object>> datos = reporteService.obtenerRendimientoGeneros(
                fechaInicio, fechaFin);
            return ResponseEntity.ok(datos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Tendencias temporales
     */
    @GetMapping("/api/analytics/tendencias")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> obtenerTendenciasTemporales(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        try {
            List<Map<String, Object>> datos = reporteService.obtenerTendenciasTemporales(
                fechaInicio, fechaFin);
            return ResponseEntity.ok(datos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Comportamiento de usuarios
     */
    @GetMapping("/api/analytics/usuarios")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> obtenerComportamientoUsuarios(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        try {
            List<Map<String, Object>> datos = reporteService.obtenerComportamientoUsuarios(
                fechaInicio, fechaFin);
            return ResponseEntity.ok(datos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ==================== VISTAS JSP ====================

    /**
     * Vista principal de reportes
     */
    @GetMapping
    public String mostrarReportes(Model model) {
        try {
            // Cargar datos para la vista
            List<Reporte> reportesRecientes = reporteService.obtenerTodos();
            Map<String, Object> estadisticas = reporteService.obtenerEstadisticasGenerales();

            model.addAttribute("reportes", reportesRecientes);
            model.addAttribute("estadisticas", estadisticas);

            return "analista-datos";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar los reportes");
            return "error";
        }
    }

    /**
     * Vista de dashboard de analytics
     */
    @GetMapping("/dashboard")
    public String mostrarDashboard(Model model) {
        try {
            Map<String, Object> estadisticas = reporteService.obtenerEstadisticasGenerales();
            model.addAttribute("estadisticas", estadisticas);

            return "dashboard-analytics";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar el dashboard");
            return "error";
        }
    }
}
