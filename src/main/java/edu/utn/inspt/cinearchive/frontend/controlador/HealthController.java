package edu.utn.inspt.cinearchive.frontend.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controlador simple para verificar que Spring MVC está funcionando
 */
@Controller
public class HealthController {

    @GetMapping("/")
    @ResponseBody
    public String home() {
        return "<h1>CineArchive - Backend Funcionando</h1>" +
               "<p>Spring MVC esta activo</p>" +
               "<ul>" +
               "<li><a href='/cinearchive/test'>Suite de Tests</a></li>" +
               "<li><a href='/cinearchive/health'>Health Check</a></li>" +
               "<li><a href='/cinearchive/login'>Login</a></li>" +
               "</ul>";
    }

    @GetMapping("/health")
    @ResponseBody
    public String health() {
        return "OK - Spring MVC is running!";
    }
}

