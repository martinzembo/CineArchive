package edu.utn.inspt.cinearchive.frontend.controlador;

import edu.utn.inspt.cinearchive.backend.modelo.Resena;
import edu.utn.inspt.cinearchive.backend.servicio.ResenaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST que expone los endpoints para gestionar reseñas
 */
@RestController
@RequestMapping("/api/resenas")
public class ResenaController {

    private final ResenaService resenaService;

    @Autowired
    public ResenaController(ResenaService resenaService) {
        this.resenaService = resenaService;
    }

    @GetMapping
    public ResponseEntity<List<Resena>> listarResenas() {
        return ResponseEntity.ok(resenaService.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resena> obtenerResena(@PathVariable Long id) {
        return resenaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Resena>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(resenaService.obtenerPorUsuario(usuarioId));
    }

    @GetMapping("/contenido/{contenidoId}")
    public ResponseEntity<List<Resena>> listarPorContenido(@PathVariable Long contenidoId) {
        return ResponseEntity.ok(resenaService.obtenerPorContenido(contenidoId));
    }

    @GetMapping("/calificacion/{minima}")
    public ResponseEntity<List<Resena>> listarPorCalificacionMinima(@PathVariable Double minima) {
        return ResponseEntity.ok(resenaService.obtenerPorCalificacionMinima(minima));
    }

    @GetMapping("/contenido/{contenidoId}/promedio")
    public ResponseEntity<Double> obtenerCalificacionPromedio(@PathVariable Long contenidoId) {
        Double promedio = resenaService.obtenerCalificacionPromedio(contenidoId);
        return promedio != null ? ResponseEntity.ok(promedio) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Resena> crearResena(@Valid @RequestBody Resena resena) {
        Long usuarioId = resena.getUsuario().getId();
        Long contenidoId = resena.getContenido().getId();
        if (resenaService.existePorUsuarioYContenido(usuarioId, contenidoId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(resenaService.crear(resena));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Resena> actualizarResena(
            @PathVariable Long id,
            @Valid @RequestBody Resena resena) {
        try {
            Resena resenaActualizada = resenaService.actualizar(id, resena);
            return ResponseEntity.ok(resenaActualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarResena(@PathVariable Long id) {
        try {
            resenaService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/usuario/{usuarioId}/contenido/{contenidoId}")
    public ResponseEntity<List<Resena>> buscarPorUsuarioYContenido(
            @PathVariable Long usuarioId,
            @PathVariable Long contenidoId) {
        return ResponseEntity.ok(resenaService.buscarPorUsuarioYContenido(usuarioId, contenidoId));
    }

    @GetMapping("/usuario/{usuarioId}/contenido/{contenidoId}/existe")
    public ResponseEntity<Boolean> verificarExistencia(
            @PathVariable Long usuarioId,
            @PathVariable Long contenidoId) {
        boolean existe = resenaService.existePorUsuarioYContenido(usuarioId, contenidoId);
        return ResponseEntity.ok(existe);
    }
}
