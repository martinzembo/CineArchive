package edu.utn.inspt.cinearchive.frontend.controlador;

import edu.utn.inspt.cinearchive.backend.servicio.ListaService;
import edu.utn.inspt.cinearchive.backend.modelo.Lista;
import edu.utn.inspt.cinearchive.backend.modelo.Contenido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ParaVerController {

    private final ListaService listaService;

    @Autowired
    public ParaVerController(ListaService listaService) {
        this.listaService = listaService;
    }

    @GetMapping("/para-ver")
    public String paraVer(Model model) {
        Long usuarioId = 1L; // TODO reemplazar por usuario en sesión
        // Buscar lista con nombre 'para-ver'
        Lista lista = listaService.getByUsuario(usuarioId).stream()
                .filter(l -> l.getNombre() != null && l.getNombre().equalsIgnoreCase("para-ver"))
                .findFirst()
                .orElse(null);
        if (lista == null) {
            Lista nueva = new Lista();
            nueva.setUsuarioId(usuarioId);
            nueva.setNombre("para-ver");
            nueva.setDescripcion("Lista de contenido para ver más tarde");
            nueva.setPublica(false);
            listaService.create(nueva);
            lista = listaService.getByUsuario(usuarioId).stream()
                    .filter(l -> l.getNombre() != null && l.getNombre().equalsIgnoreCase("para-ver"))
                    .findFirst().orElse(null);
        }
        List<Contenido> contenidos = lista != null ? listaService.getContenidoByLista(lista.getId()) : java.util.Collections.emptyList();
        model.addAttribute("contenidos", contenidos);
        return "para-ver";
    }
}

