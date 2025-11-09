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
    public String catalogo(@RequestParam(value = "q", required = false) String q,
                           @RequestParam(value = "genero", required = false) String genero,
                           @RequestParam(value = "tipo", required = false) String tipo,
                           @RequestParam(value = "orden", required = false) String orden,
                           Model model) {
        List<Contenido> lista = contenidoService.search(q, genero, tipo, orden);
        model.addAttribute("contenidos", lista);
        model.addAttribute("query", q);
        model.addAttribute("genero", genero);
        model.addAttribute("tipo", tipo);
        model.addAttribute("orden", orden);
        return "catalogo";
    }
}
