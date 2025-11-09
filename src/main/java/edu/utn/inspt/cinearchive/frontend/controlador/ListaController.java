package edu.utn.inspt.cinearchive.frontend.controlador;

import edu.utn.inspt.cinearchive.backend.modelo.Lista;
import edu.utn.inspt.cinearchive.backend.modelo.Contenido;
import edu.utn.inspt.cinearchive.backend.servicio.ListaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ListaController {

    private final ListaService listaService;

    @Autowired
    public ListaController(ListaService listaService) {
        this.listaService = listaService;
    }

    @GetMapping("/mi-lista")
    public String miLista(Model model) {
        Long usuarioId = 1L; // TODO: reemplazar con usuario de sesión
        // obtener o crear lista "mi-lista"
        Lista lista = listaService.getByUsuario(usuarioId).stream()
                .filter(l -> l.getNombre() != null && l.getNombre().equalsIgnoreCase("mi-lista"))
                .findFirst().orElse(null);
        if (lista == null) {
            Lista nueva = new Lista();
            nueva.setUsuarioId(usuarioId);
            nueva.setNombre("mi-lista");
            nueva.setDescripcion("Favoritos del usuario");
            nueva.setPublica(false);
            listaService.create(nueva);
            lista = listaService.getByUsuario(usuarioId).stream()
                    .filter(l -> l.getNombre() != null && l.getNombre().equalsIgnoreCase("mi-lista"))
                    .findFirst().orElse(null);
        }
        List<Contenido> contenidos = lista != null ? listaService.getContenidoByLista(lista.getId()) : java.util.Collections.emptyList();
        model.addAttribute("contenidos", contenidos);
        return "mi-lista";
    }

    @PostMapping(value = "/lista/add", produces = "application/json")
    @ResponseBody
    public String addContenido(@RequestParam("contenidoId") Long contenidoId, @RequestParam("lista") String listaNombre) {
        Long usuarioId = 1L; // TODO: sesión
        Lista lista = listaService.getByUsuario(usuarioId).stream().filter(l -> l.getNombre().equalsIgnoreCase(listaNombre)).findFirst().orElse(null);
        if (lista == null) {
            Lista nueva = new Lista();
            nueva.setUsuarioId(usuarioId);
            nueva.setNombre(listaNombre);
            nueva.setDescripcion("Generada automaticamente");
            nueva.setPublica(false);
            listaService.create(nueva);
            lista = listaService.getByUsuario(usuarioId).stream().filter(l -> l.getNombre().equalsIgnoreCase(listaNombre)).findFirst().orElse(null);
        }
        if (lista != null) {
            listaService.addContenido(lista.getId(), contenidoId);
        }
        return "{\"status\":\"OK\"}";
    }

    @PostMapping(value = "/lista/remove", produces = "application/json")
    @ResponseBody
    public String removeContenido(@RequestParam("contenidoId") Long contenidoId, @RequestParam("lista") String listaNombre) {
        Long usuarioId = 1L; // TODO: sesión
        Lista lista = listaService.getByUsuario(usuarioId).stream().filter(l -> l.getNombre().equalsIgnoreCase(listaNombre)).findFirst().orElse(null);
        if (lista != null) {
            listaService.removeContenido(lista.getId(), contenidoId);
        }
        return "{\"status\":\"OK\"}";
    }
}
