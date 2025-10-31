package edu.utn.inspt.cinearchive.frontend.controlador;

import edu.utn.inspt.cinearchive.backend.modelo.Contenido;
import edu.utn.inspt.cinearchive.backend.servicio.ContenidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DetalleContenidoController {

    private final ContenidoService contenidoService;

    @Autowired
    public DetalleContenidoController(ContenidoService contenidoService) {
        this.contenidoService = contenidoService;
    }

    @GetMapping("/contenido/{id}")
    public String detalle(@PathVariable("id") Long id, Model model) {
        Contenido c = contenidoService.getById(id);
        model.addAttribute("contenido", c);
        return "detalle";
    }
}

