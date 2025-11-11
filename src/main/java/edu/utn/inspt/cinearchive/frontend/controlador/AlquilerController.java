package edu.utn.inspt.cinearchive.frontend.controlador;

import edu.utn.inspt.cinearchive.backend.modelo.Usuario;
import edu.utn.inspt.cinearchive.backend.servicio.AlquilerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
public class AlquilerController {

    private static final Logger logger = Logger.getLogger(AlquilerController.class.getName());

    private final AlquilerService alquilerService;

    @Autowired
    public AlquilerController(AlquilerService alquilerService) {
        this.alquilerService = alquilerService;
    }

    @GetMapping("/mis-alquileres")
    public String misAlquileres(Model model, HttpSession session) {
        Long usuarioId = obtenerUsuarioId(session);
        model.addAttribute("alquileres", alquilerService.getByUsuarioConContenido(usuarioId));
        // Usuario logueado para el header
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuarioLogueado != null) {
            model.addAttribute("usuarioLogueado", usuarioLogueado);
        }
        return "mis-alquileres";
    }

    @PostMapping("/alquilar")
    public String alquilar(@RequestParam("contenidoId") Long contenidoId,
                           @RequestParam(value = "periodo", required = false, defaultValue = "3") Integer periodo,
                           @RequestParam(value = "metodoPago", required = false) String metodoPago,
                           HttpSession session,
                           RedirectAttributes ra) {
        Long usuarioId = obtenerUsuarioId(session);
        try {
            alquilerService.rent(usuarioId, contenidoId, periodo, metodoPago);
            ra.addFlashAttribute("msg", "Alquiler confirmado");
            return "redirect:/mis-alquileres";
        } catch (IllegalArgumentException | IllegalStateException ex) {
            logger.log(Level.WARNING, "Error en alquiler: {0}", ex.getMessage());
            ra.addFlashAttribute("error", ex.getMessage());
            return "redirect:/contenido/" + contenidoId;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error inesperado en alquiler", ex);
            ra.addFlashAttribute("error", "Error interno al procesar alquiler");
            return "redirect:/contenido/" + contenidoId;
        }
    }

    @PostMapping(value = "/alquiler/estado", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<java.util.Map<String,Object>> estadoAlquiler(@RequestBody java.util.Map<String,Object> body, HttpSession session) {
        Long usuarioId = obtenerUsuarioId(session);
        java.util.List<?> raw = (java.util.List<?>) body.getOrDefault("ids", java.util.Collections.emptyList());
        java.util.Set<Long> activos = new java.util.HashSet<>();
        try {
            java.util.List<edu.utn.inspt.cinearchive.backend.modelo.AlquilerDetalle> lista = alquilerService.getByUsuarioConContenido(usuarioId);
            java.time.LocalDateTime now = java.time.LocalDateTime.now();
            for (edu.utn.inspt.cinearchive.backend.modelo.AlquilerDetalle d : lista) {
                if (d.getContenidoId() != null && d.getFechaFin() != null && d.getFechaFin().isAfter(now) && d.getEstado() == edu.utn.inspt.cinearchive.backend.modelo.Alquiler.Estado.ACTIVO) {
                    activos.add(d.getContenidoId());
                }
            }
        } catch (Exception ignored) {}
        java.util.Map<String,Object> resp = new java.util.HashMap<>();
        resp.put("activos", activos.stream().map(String::valueOf).toArray());
        return ResponseEntity.ok(resp);
    }

    private Long obtenerUsuarioId(HttpSession session) {
        Object u = session != null ? session.getAttribute("usuario") : null;
        if (u instanceof Usuario) {
            return (long) ((Usuario) u).getId();
        }
        // Fallback temporal mientras no haya login real
        return 1L;
    }
}
