package edu.utn.inspt.cinearchive.frontend.controlador;

import edu.utn.inspt.cinearchive.backend.modelo.Alquiler;
import edu.utn.inspt.cinearchive.backend.servicio.AlquilerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AlquilerController {

    private final AlquilerService alquilerService;

    @Autowired
    public AlquilerController(AlquilerService alquilerService) {
        this.alquilerService = alquilerService;
    }

    @GetMapping("/mis-alquileres")
    public String misAlquileres(Model model) {
        // placeholder: en el futuro filtrar por usuario logueado
        model.addAttribute("alquileres", alquilerService.getByUsuario(1L));
        return "mis-alquileres";
    }

    @PostMapping("/alquilar")
    public String alquilar(@RequestParam("contenidoId") Long contenidoId, @RequestParam("periodo") Integer periodo) {
        // placeholder: crear un Alquiler y persistir
        Alquiler a = new Alquiler();
        a.setContenidoId(contenidoId);
        a.setUsuarioId(1L); // temporal
        a.setPeriodoAlquiler(periodo);
        alquilerService.create(a);
        return "redirect:/mis-alquileres";
    }
}

