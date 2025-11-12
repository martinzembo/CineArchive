package edu.utn.inspt.cinearchive.frontend.controlador;

import edu.utn.inspt.cinearchive.backend.modelo.Usuario;
import edu.utn.inspt.cinearchive.backend.servicio.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

/**
 * Controlador para el panel principal del Administrador
 * Endpoint principal para el rol ADMINISTRADOR
 */
@Controller
@RequestMapping("/admin")
public class AdminPanelController {

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Mostrar el panel principal del administrador
     * Endpoint principal para el rol ADMINISTRADOR
     *
     * @param model el modelo para pasar datos a la vista
     * @param session la sesión HTTP para verificar permisos
     * @return el nombre de la vista JSP
     */
    @GetMapping("/panel")
    public String mostrarPanelAdmin(Model model, HttpSession session) {
        // Verificar que el usuario logueado es administrador
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuarioLogueado == null || usuarioLogueado.getRol() != Usuario.Rol.ADMINISTRADOR) {
            return "redirect:/acceso-denegado";
        }

        // Agregar metadatos para la vista
        model.addAttribute("pageTitle", "Panel de Administración - CineArchive");
        model.addAttribute("pageDescription", "Panel de control para administración de usuarios y sistema");
        model.addAttribute("currentSection", "admin");

        // Redirigir al listado de usuarios que es la funcionalidad principal del admin
        return "redirect:/admin/usuarios";
    }
}

