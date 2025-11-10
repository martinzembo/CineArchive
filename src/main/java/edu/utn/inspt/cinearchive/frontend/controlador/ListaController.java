package edu.utn.inspt.cinearchive.frontend.controlador;

import edu.utn.inspt.cinearchive.backend.modelo.Lista;
import edu.utn.inspt.cinearchive.backend.modelo.Contenido;
import edu.utn.inspt.cinearchive.backend.modelo.Usuario;
import edu.utn.inspt.cinearchive.backend.servicio.ListaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
public class ListaController {

    private static final Logger logger = Logger.getLogger(ListaController.class.getName());

    private final ListaService listaService;

    @Autowired
    public ListaController(ListaService listaService) {
        this.listaService = listaService;
    }

    @GetMapping("/mi-lista")
    public String miLista(Model model, HttpSession session) {
        Long usuarioId = obtenerUsuarioId(session);
        Lista lista = obtenerOCrearLista(usuarioId, "mi-lista", "Favoritos del usuario");
        List<Contenido> contenidos = lista != null ? listaService.getContenidoByLista(lista.getId()) : java.util.Collections.emptyList();
        model.addAttribute("contenidos", contenidos);
        return "mi-lista";
    }

    @GetMapping("/para-ver")
    public String paraVer(Model model, HttpSession session) {
        Long usuarioId = obtenerUsuarioId(session);
        Lista lista = obtenerOCrearLista(usuarioId, "para-ver", "Contenido pendiente por ver");
        List<Contenido> contenidos = lista != null ? listaService.getContenidoByLista(lista.getId()) : java.util.Collections.emptyList();
        model.addAttribute("contenidos", contenidos);
        return "para-ver";
    }

    @PostMapping(value = "/lista/add", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Map<String,Object>> addContenido(@RequestParam("contenidoId") Long contenidoId, @RequestParam("lista") String listaNombre, HttpSession session) {
        Map<String,Object> resp = new HashMap<>();
        try {
            Long usuarioId = obtenerUsuarioId(session);
            Lista lista = obtenerOCrearLista(usuarioId, listaNombre, "Generada automaticamente");
            if (lista != null) {
                listaService.addContenido(lista.getId(), contenidoId);
                resp.put("status","OK");
                return ResponseEntity.ok(resp);
            } else {
                resp.put("status","ERROR");
                resp.put("message","No se pudo crear o encontrar la lista");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error en addContenido", ex);
            resp.put("status","ERROR");
            resp.put("message", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
        }
    }

    @PostMapping(value = "/lista/remove", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Map<String,Object>> removeContenido(@RequestParam("contenidoId") Long contenidoId, @RequestParam("lista") String listaNombre, HttpSession session) {
        Map<String,Object> resp = new HashMap<>();
        try {
            Long usuarioId = obtenerUsuarioId(session);
            Lista lista = listaService.getByUsuario(usuarioId).stream().filter(l -> l.getNombre().equalsIgnoreCase(listaNombre)).findFirst().orElse(null);
            if (lista != null) {
                listaService.removeContenido(lista.getId(), contenidoId);
                resp.put("status","OK");
                return ResponseEntity.ok(resp);
            } else {
                resp.put("status","ERROR");
                resp.put("message","Lista no encontrada");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp);
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error en removeContenido", ex);
            resp.put("status","ERROR");
            resp.put("message", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
        }
    }

    @PostMapping(value = "/lista/estado", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Map<String,Object>> estadoListas(@RequestBody Map<String,Object> body, HttpSession session) {
        try {
            Long usuarioId = obtenerUsuarioId(session);
            java.util.List<?> ids = (java.util.List<?>) body.getOrDefault("ids", java.util.Collections.emptyList());
            java.util.List<Long> idsLong = new java.util.ArrayList<>();
            for (Object o : ids) {
                try { idsLong.add(Long.valueOf(String.valueOf(o))); } catch (Exception ignored) {}
            }
            java.util.List<Contenido> miLista = listaService.getContenidoByLista(obtenerOCrearLista(usuarioId, "mi-lista", "").getId());
            java.util.List<Contenido> paraVer = listaService.getContenidoByLista(obtenerOCrearLista(usuarioId, "para-ver", "").getId());
            java.util.Set<Long> miSet = new java.util.HashSet<>();
            for (Contenido c : miLista) { miSet.add(c.getId()); }
            java.util.Set<Long> pvSet = new java.util.HashSet<>();
            for (Contenido c : paraVer) { pvSet.add(c.getId()); }
            java.util.Map<String,Object> resp = new java.util.HashMap<>();
            // Solo reportar intersección con ids pedidos para ahorrar payload
            resp.put("miLista", idsLong.stream().filter(miSet::contains).map(String::valueOf).toArray());
            resp.put("paraVer", idsLong.stream().filter(pvSet::contains).map(String::valueOf).toArray());
            return ResponseEntity.ok(resp);
        } catch (Exception ex) {
            java.util.Map<String,Object> err = new java.util.HashMap<>();
            err.put("status","ERROR");
            err.put("message", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
        }
    }

    private Lista obtenerOCrearLista(Long usuarioId, String nombre, String descripcion) {
        Lista lista = listaService.getByUsuario(usuarioId).stream()
                .filter(l -> l.getNombre() != null && l.getNombre().equalsIgnoreCase(nombre))
                .findFirst().orElse(null);
        if (lista == null) {
            Lista nueva = new Lista();
            nueva.setUsuarioId(usuarioId);
            nueva.setNombre(nombre);
            nueva.setDescripcion(descripcion);
            nueva.setPublica(false);
            listaService.create(nueva);
            lista = listaService.getByUsuario(usuarioId).stream()
                    .filter(l -> l.getNombre() != null && l.getNombre().equalsIgnoreCase(nombre))
                    .findFirst().orElse(null);
        }
        return lista;
    }

    private Long obtenerUsuarioId(HttpSession session) {
        Object u = session != null ? session.getAttribute("usuario") : null;
        if (u instanceof Usuario) {
            return (long) ((Usuario) u).getId();
        }
        return 1L;
    }
}
