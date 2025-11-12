package edu.utn.inspt.cinearchive.frontend.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controlador para las vistas del Gestor de Inventario
 * Parte del Developer 3 - Manejo de frontend para gestión de inventarios
 */
@Controller
@RequestMapping("/inventario")
public class InventarioViewController {

    /**
     * Mostrar la página principal del gestor de inventario
     *
     * @param model el modelo para pasar datos a la vista
     * @return el nombre de la vista JSP
     */
    @GetMapping("/dashboard")
    public String mostrarDashboardInventario(Model model) {
        // Agregar metadatos para la vista
        model.addAttribute("pageTitle", "Gestor de Inventario - CineArchive");
        model.addAttribute("pageDescription", "Panel de control completo para la gestión de contenidos, categorías y reseñas");
        model.addAttribute("currentSection", "inventario");

        // Datos iniciales para la vista (pueden ser cargados desde servicios)
        model.addAttribute("totalContenidos", 0);
        model.addAttribute("contenidosDisponibles", 0);
        model.addAttribute("totalCategorias", 0);
        model.addAttribute("totalResenas", 0);

        return "gestor-inventario";
    }

    /**
     * Mostrar formulario para agregar contenido
     *
     * @param model el modelo para pasar datos a la vista
     * @return el nombre de la vista JSP
     */
    @GetMapping("/contenido/nuevo")
    public String mostrarFormularioNuevoContenido(Model model) {
        model.addAttribute("pageTitle", "Agregar Contenido - CineArchive");
        model.addAttribute("action", "crear");

        return "gestor-inventario";
    }

    /*
     * Mostrar gestión de categorías
     * NOTA: Este endpoint está comentado porque está duplicado con
     * GestorInventarioController.gestionarCategorias()
     * El método activo es el de GestorInventarioController que incluye
     * la lógica de carga de datos desde los servicios.
     */
    /*
    @GetMapping("/categorias")
    public String mostrarGestionCategorias(Model model) {
        model.addAttribute("pageTitle", "Gestión de Categorías - CineArchive");
        model.addAttribute("activeTab", "categorias");

        return "gestor-inventario";
    }
    */

    /**
     * Mostrar gestión de reseñas
     *
     * @param model el modelo para pasar datos a la vista
     * @return el nombre de la vista JSP
     */
    @GetMapping("/resenas")
    public String mostrarGestionResenas(Model model) {
        model.addAttribute("pageTitle", "Gestión de Reseñas - CineArchive");
        model.addAttribute("activeTab", "resenas");

        return "gestor-inventario";
    }

    // Nota: El endpoint /estadisticas está manejado por GestorInventarioController
}
