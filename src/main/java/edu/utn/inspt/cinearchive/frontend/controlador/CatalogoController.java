package edu.utn.inspt.cinearchive.frontend.controlador;

import edu.utn.inspt.cinearchive.backend.modelo.Contenido;
import edu.utn.inspt.cinearchive.backend.servicio.ContenidoService;
import edu.utn.inspt.cinearchive.backend.servicio.AlquilerService;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class CatalogoController {

    private final ContenidoService contenidoService;
    private final AlquilerService alquilerService;

    @Autowired
    public CatalogoController(ContenidoService contenidoService, AlquilerService alquilerService) {
        this.contenidoService = contenidoService;
        this.alquilerService = alquilerService;
    }

    @GetMapping({"/catalogo"})
    public String catalogo(@RequestParam(value = "q", required = false) String q,
                           @RequestParam(value = "genero", required = false) String genero,
                           @RequestParam(value = "tipo", required = false) String tipo,
                           @RequestParam(value = "orden", required = false) String orden,
                           @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                           Model model, HttpSession session) {
        // Normalizar orden soportado
        if (orden != null && !(orden.equals("fecha") || orden.equals("nombre"))) {
            orden = "nombre";
        }
        if (page < 1) page = 1;
        final int size = 50; // tamaño fijo solicitado
        long total = contenidoService.searchCount(q, genero, tipo);
        List<Contenido> lista = contenidoService.searchPagedLight(q, genero, tipo, orden, page, size);
        long totalPages = (long) Math.ceil(total / (double) size);
        model.addAttribute("contenidos", lista);
        model.addAttribute("query", q);
        model.addAttribute("genero", genero);
        model.addAttribute("tipo", tipo);
        model.addAttribute("orden", orden);
        model.addAttribute("page", page);
        model.addAttribute("size", size); // aún disponible para JSP si lo muestra
        model.addAttribute("total", total);
        model.addAttribute("totalPages", totalPages);
        // IDs alquilados activos para el usuario (para marcar estado en tarjetas)
        Long usuarioId = obtenerUsuarioId(session);
        java.util.Set<Long> alquilados = new java.util.HashSet<>();
        try {
            java.util.List<edu.utn.inspt.cinearchive.backend.modelo.AlquilerDetalle> ads = alquilerService.getByUsuarioConContenido(usuarioId);
            java.time.LocalDateTime now = java.time.LocalDateTime.now();
            for (edu.utn.inspt.cinearchive.backend.modelo.AlquilerDetalle d : ads) {
                if (d.getContenidoId() != null && d.getFechaFin() != null && d.getFechaFin().isAfter(now) && d.getEstado() == edu.utn.inspt.cinearchive.backend.modelo.Alquiler.Estado.ACTIVO) {
                    alquilados.add(d.getContenidoId());
                }
            }
        } catch (Exception ignore) {}
        java.util.Map<Long, Boolean> alquiladoMap = new java.util.HashMap<>();
        for (Long idC : alquilados) alquiladoMap.put(idC, Boolean.TRUE);
        model.addAttribute("alquiladoMap", alquiladoMap);

        // Secciones destacadas para portada de catálogo (diseño dev2)
        try {
            java.util.List<Contenido> novedades = contenidoService.searchPagedLight(null, null, null, "fecha", 1, 12);
            model.addAttribute("novedades", novedades);
        } catch (Exception ignore) {}
        try {
            // Sin campo de popularidad, usamos nombre como orden estable (placeholder)
            java.util.List<Contenido> populares = contenidoService.searchPagedLight(null, null, null, "nombre", 1, 12);
            model.addAttribute("populares", populares);
        } catch (Exception ignore) {}
        try {
            java.util.List<Contenido> accionList = contenidoService.searchPagedLight(null, "accion", null, "nombre", 1, 12);
            model.addAttribute("accionList", accionList);
        } catch (Exception ignore) {}
        try {
            java.util.List<Contenido> seriesRecomendadas = contenidoService.searchPagedLight(null, null, "SERIE", "fecha", 1, 12);
            model.addAttribute("seriesRecomendadas", seriesRecomendadas);
        } catch (Exception ignore) {}

        return "catalogo";
    }

    private Long obtenerUsuarioId(HttpSession session) {
        Object u = session != null ? session.getAttribute("usuario") : null;
        if (u instanceof edu.utn.inspt.cinearchive.backend.modelo.Usuario) {
            return (long) ((edu.utn.inspt.cinearchive.backend.modelo.Usuario) u).getId();
        }
        return 1L;
    }
}
