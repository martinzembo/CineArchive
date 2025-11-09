package edu.utn.inspt.cinearchive.frontend.controlador;

import edu.utn.inspt.cinearchive.backend.modelo.Lista;
import edu.utn.inspt.cinearchive.backend.servicio.ListaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ListaController {

    private final ListaService listaService;

    @Autowired
    public ListaController(ListaService listaService) {
        this.listaService = listaService;
    }

    @GetMapping("/mi-lista")
    public String miLista(Model model) {
        model.addAttribute("listas", listaService.getByUsuario(1L)); // TODO usuario sesión
        return "mi-lista";
    }

    @PostMapping(value = "/lista/add", produces = "application/json")
    @ResponseBody
    public String addContenido(@RequestParam("contenidoId") Long contenidoId, @RequestParam("lista") String listaNombre) {
        Lista lista = listaService.getByUsuario(1L).stream().filter(l -> l.getNombre().equalsIgnoreCase(listaNombre)).findFirst().orElse(null);
        if (lista == null) {
            Lista nueva = new Lista();
            nueva.setUsuarioId(1L);
            nueva.setNombre(listaNombre);
            nueva.setDescripcion("Generada automaticamente");
            nueva.setPublica(false);
            listaService.create(nueva);
            lista = listaService.getByUsuario(1L).stream().filter(l -> l.getNombre().equalsIgnoreCase(listaNombre)).findFirst().orElse(null);
        }
        if (lista != null) {
            listaService.addContenido(lista.getId(), contenidoId);
        }
        return "{\"status\":\"OK\"}";
    }

    @PostMapping(value = "/lista/remove", produces = "application/json")
    @ResponseBody
    public String removeContenido(@RequestParam("contenidoId") Long contenidoId, @RequestParam("lista") String listaNombre) {
        Lista lista = listaService.getByUsuario(1L).stream().filter(l -> l.getNombre().equalsIgnoreCase(listaNombre)).findFirst().orElse(null);
        if (lista != null) {
            listaService.removeContenido(lista.getId(), contenidoId);
        }
        return "{\"status\":\"OK\"}";
    }
}
