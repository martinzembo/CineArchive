package edu.utn.inspt.cinearchive.frontend.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Controlador para las vistas del Analista de Datos
 * Parte del Developer 3 - Manejo de frontend para reportes y analytics
 */
@Controller
@RequestMapping("/reportes")
public class ReportesViewController {

    /*
     * Mostrar el dashboard principal del analista de datos
     * NOTA: Este endpoint está comentado porque está duplicado con
     * ReporteController.mostrarDashboard()
     * El método activo es el de ReporteController que incluye
     * la lógica completa de carga de datos desde los servicios.
     */
    /*
    @GetMapping("/dashboard")
    public String mostrarDashboardAnalytics(Model model) {
        // Agregar metadatos para la vista
        model.addAttribute("pageTitle", "Centro de Análisis de Datos - CineArchive");
        model.addAttribute("pageDescription", "Dashboard integral para análisis de comportamiento, tendencias y métricas de negocio");
        model.addAttribute("currentSection", "reportes");

        // Configurar fechas por defecto (últimos 30 días)
        LocalDate fechaFin = LocalDate.now();
        LocalDate fechaInicio = fechaFin.minusDays(30);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        model.addAttribute("fechaInicio", fechaInicio.format(formatter));
        model.addAttribute("fechaFin", fechaFin.format(formatter));
        model.addAttribute("periodoSeleccionado", 30);

        // KPIs iniciales (serán cargados via JavaScript)
        model.addAttribute("totalVisualizaciones", 0);
        model.addAttribute("ingresosTotales", 0.0);
        model.addAttribute("usuariosActivos", 0);
        model.addAttribute("calificacionPromedio", 0.0);

        return "analista-datos";
    }
    */

    /**
     * Mostrar analytics con filtros específicos
     *
     * @param periodo período de análisis en días
     * @param fechaInicio fecha de inicio personalizada
     * @param fechaFin fecha de fin personalizada
     * @param tipoContenido tipo de contenido a analizar
     * @param model el modelo para pasar datos a la vista
     * @return el nombre de la vista JSP
     */
    @GetMapping("/analytics")
    public String mostrarAnalyticsConFiltros(
            @RequestParam(required = false, defaultValue = "30") int periodo,
            @RequestParam(required = false) String fechaInicio,
            @RequestParam(required = false) String fechaFin,
            @RequestParam(required = false) String tipoContenido,
            Model model) {

        model.addAttribute("pageTitle", "Analytics Avanzados - CineArchive");
        model.addAttribute("currentSection", "reportes");

        // Configurar filtros
        model.addAttribute("periodoSeleccionado", periodo);
        model.addAttribute("tipoContenidoSeleccionado", tipoContenido != null ? tipoContenido : "");

        // Si no se proporcionan fechas personalizadas, usar el período
        if (fechaInicio == null || fechaFin == null) {
            LocalDate fin = LocalDate.now();
            LocalDate inicio = fin.minusDays(periodo);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            model.addAttribute("fechaInicio", inicio.format(formatter));
            model.addAttribute("fechaFin", fin.format(formatter));
        } else {
            model.addAttribute("fechaInicio", fechaInicio);
            model.addAttribute("fechaFin", fechaFin);
        }

        return "analista-datos";
    }

    /**
     * Mostrar página de reportes personalizados
     *
     * @param model el modelo para pasar datos a la vista
     * @return el nombre de la vista JSP
     */
    @GetMapping("/personalizados")
    public String mostrarReportesPersonalizados(Model model) {
        model.addAttribute("pageTitle", "Reportes Personalizados - CineArchive");
        model.addAttribute("currentSection", "reportes");
        model.addAttribute("focusSection", "reportes");

        return "analista-datos";
    }

    /**
     * Mostrar análisis demográfico detallado
     *
     * @param model el modelo para pasar datos a la vista
     * @return el nombre de la vista JSP
     */
    @GetMapping("/demografico")
    public String mostrarAnalisisDemografico(Model model) {
        model.addAttribute("pageTitle", "Análisis Demográfico - CineArchive");
        model.addAttribute("currentSection", "reportes");
        model.addAttribute("focusChart", "demografico");

        return "analista-datos";
    }

    /**
     * Mostrar análisis de tendencias temporales
     *
     * @param model el modelo para pasar datos a la vista
     * @return el nombre de la vista JSP
     */
    @GetMapping("/tendencias")
    public String mostrarTendenciasTemporales(Model model) {
        model.addAttribute("pageTitle", "Tendencias Temporales - CineArchive");
        model.addAttribute("currentSection", "reportes");
        model.addAttribute("focusChart", "tendencias");

        return "analista-datos";
    }

    /**
     * Mostrar análisis de comportamiento de usuarios
     *
     * @param model el modelo para pasar datos a la vista
     * @return el nombre de la vista JSP
     */
    @GetMapping("/comportamiento")
    public String mostrarComportamientoUsuarios(Model model) {
        model.addAttribute("pageTitle", "Comportamiento de Usuarios - CineArchive");
        model.addAttribute("currentSection", "reportes");
        model.addAttribute("focusAnalytics", "comportamiento");

        return "analista-datos";
    }
}
