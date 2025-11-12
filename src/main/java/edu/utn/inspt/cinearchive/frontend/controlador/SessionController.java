package edu.utn.inspt.cinearchive.frontend.controlador;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * Controlador REST para gestionar operaciones de sesión vía API
 */
@RestController
@RequestMapping("/api/session")
public class SessionController {

    /**
     * Endpoint para invalidar la sesión del usuario actual
     * POST /api/session/invalidate
     *
     * @param session Sesión HTTP a invalidar
     * @return Respuesta JSON con el resultado de la operación
     */
    @PostMapping("/invalidate")
    public ResponseEntity<Map<String, Object>> invalidarSesion(HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Verificar si hay una sesión activa
            if (session != null && session.getAttribute("usuarioLogueado") != null) {
                String nombreUsuario = (String) session.getAttribute("usuarioNombre");

                // Invalidar la sesión
                session.invalidate();

                response.put("success", true);
                response.put("message", "Sesión invalidada correctamente");
                response.put("usuario", nombreUsuario);

                System.out.println("API: Sesión invalidada para usuario: " + nombreUsuario);

                return ResponseEntity.ok(response);
            } else {
                // No había sesión activa
                response.put("success", true);
                response.put("message", "No había sesión activa");

                return ResponseEntity.ok(response);
            }
        } catch (IllegalStateException e) {
            // La sesión ya fue invalidada
            response.put("success", true);
            response.put("message", "La sesión ya estaba invalidada");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Error inesperado
            response.put("success", false);
            response.put("message", "Error al invalidar la sesión: " + e.getMessage());

            System.err.println("ERROR al invalidar sesión via API: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Endpoint para verificar si hay una sesión activa
     * GET /api/session/check
     *
     * @param session Sesión HTTP a verificar
     * @return Respuesta JSON con información de la sesión
     */
    @GetMapping("/check")
    public ResponseEntity<Map<String, Object>> verificarSesion(HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        try {
            if (session != null && session.getAttribute("usuarioLogueado") != null) {
                response.put("active", true);
                response.put("usuarioNombre", session.getAttribute("usuarioNombre"));
                response.put("usuarioRol", session.getAttribute("usuarioRol"));
            } else {
                response.put("active", false);
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("active", false);
            response.put("error", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
}

