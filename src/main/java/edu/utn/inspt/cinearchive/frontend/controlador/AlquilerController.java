package edu.utn.inspt.cinearchive.frontend.controlador;

import edu.utn.inspt.cinearchive.backend.servicio.AlquilerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AlquilerController {

    private final AlquilerService alquilerService;

    @Autowired
    public AlquilerController(AlquilerService alquilerService) {
        this.alquilerService = alquilerService;
    }

    @GetMapping("/mis-alquileres")
    public String misAlquileres(Model model) {
        model.addAttribute("alquileres", alquilerService.getByUsuarioConContenido(1L));
        return "mis-alquileres";
    }

    @PostMapping("/alquilar")
    public String alquilar(@RequestParam("contenidoId") Long contenidoId,
                           @RequestParam("periodo") Integer periodo,
                           RedirectAttributes ra) {
        try {
            alquilerService.rent(1L, contenidoId, periodo);
            ra.addFlashAttribute("msg", "Alquiler confirmado");
            return "redirect:/mis-alquileres";
        } catch (IllegalArgumentException | IllegalStateException ex) {
            ra.addFlashAttribute("error", ex.getMessage());
            return "redirect:/contenido/" + contenidoId;
        }
    }
}
