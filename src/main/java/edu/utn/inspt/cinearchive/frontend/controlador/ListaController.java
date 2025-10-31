package edu.utn.inspt.cinearchive.frontend.controlador;

import edu.utn.inspt.cinearchive.backend.modelo.Lista;
import edu.utn.inspt.cinearchive.backend.servicio.ListaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ListaController {

    private final ListaService listaService;

    @Autowired
    public ListaController(ListaService listaService) {
        this.listaService = listaService;
    }

    @GetMapping("/mi-lista")
    public String miLista(Model model) {
        model.addAttribute("listas", listaService.getByUsuario(1L));
        return "mi-lista";
    }
}

