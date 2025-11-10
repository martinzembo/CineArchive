package edu.utn.inspt.cinearchive.frontend.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HealthController {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public HealthController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping(value = "/api/health/db", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String dbHealth() {
        Integer count = 0;
        try {
            count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM contenido", Integer.class);
        } catch (Exception ex) {
            // Si la tabla no existe aún o no hay conexión
            return "{\"status\":\"ERROR\",\"message\":\"" + ex.getMessage().replace("\"", "'") + "\"}";
        }
        String status = (count != null && count > 0) ? "OK" : "EMPTY";
        return "{\"contenidoCount\":" + (count == null ? 0 : count) + ",\"status\":\"" + status + "\"}";
    }
}
