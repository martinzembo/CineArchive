package edu.utn.inspt.cinearchive.frontend.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controlador simple para verificar que Spring MVC está funcionando
 */
@Controller
public class HealthController {

    @GetMapping("/health")
    @ResponseBody
    public String health() {
        return "OK - Spring MVC is running!";
    }
}
