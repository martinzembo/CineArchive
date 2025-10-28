package edu.utn.inspt.cinearchive.frontend.controlador;

import edu.utn.inspt.cinearchive.backend.modelo.Contenido;
import edu.utn.inspt.cinearchive.backend.servicio.ContenidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class CatalogoController {

    private final ContenidoService contenidoService;

    @Autowired
    public CatalogoController(ContenidoService contenidoService) {
        this.contenidoService = contenidoService;
    }

    @GetMapping({"/catalogo","/"})
    public String catalogo(@RequestParam(value = "q", required = false) String q, Model model) {
        // Por ahora devolvemos todo; en el futuro aplicar filtros por 'q'
        List<Contenido> lista = contenidoService.getAll();
        model.addAttribute("contenidos", lista);
        model.addAttribute("query", q);
        return "catalogo";
    }
}

