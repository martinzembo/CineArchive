package edu.utn.inspt.cinearchive.frontend.controlador;

import edu.utn.inspt.cinearchive.backend.modelo.Contenido;
import edu.utn.inspt.cinearchive.backend.servicio.ContenidoService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controlador REST que expone los endpoints para gestionar contenidos
 */
@RestController
@RequestMapping("/api/contenidos")
public class ContenidoController {

    private final ContenidoService contenidoService;

    @Autowired
    public ContenidoController(ContenidoService contenidoService) {
        this.contenidoService = contenidoService;
    }

    @GetMapping
    public ResponseEntity<List<Contenido>> listarContenidos() {
        return ResponseEntity.ok(contenidoService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contenido> obtenerContenido(@PathVariable Long id) {
        return contenidoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/titulo/{titulo}")
    public ResponseEntity<List<Contenido>> buscarPorTitulo(@PathVariable String titulo) {
        List<Contenido> contenidos = contenidoService.buscarPorTitulo(titulo);
        return ResponseEntity.ok(contenidos);
    }

    @GetMapping("/genero/{genero}")
    public ResponseEntity<List<Contenido>> buscarPorGenero(@PathVariable String genero) {
        List<Contenido> contenidos = contenidoService.buscarPorGenero(genero);
        return ResponseEntity.ok(contenidos);
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Contenido>> buscarPorTipo(@PathVariable Contenido.Tipo tipo) {
        List<Contenido> contenidos = contenidoService.buscarPorTipo(tipo);
        return ResponseEntity.ok(contenidos);
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<Contenido>> listarDisponiblesParaAlquiler() {
        return ResponseEntity.ok(contenidoService.obtenerDisponiblesParaAlquiler());
    }

    @GetMapping("/gestor/{gestorId}")
    public ResponseEntity<List<Contenido>> listarPorGestorInventario(@PathVariable Long gestorId) {
        return ResponseEntity.ok(contenidoService.obtenerPorGestorInventario(gestorId));
    }

    @PostMapping
    public ResponseEntity<Contenido> crearContenido(@Valid @RequestBody Contenido contenido) {
        if (contenidoService.existePorTitulo(contenido.getTitulo())) {
            return ResponseEntity.badRequest().build();
        }
        contenidoService.guardar(contenido);
        return ResponseEntity.status(HttpStatus.CREATED).body(contenido);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Contenido> actualizarContenido(
            @PathVariable Long id,
            @Valid @RequestBody Contenido contenido) {
        if (!contenidoService.existePorId(id)) {
            return ResponseEntity.notFound().build();
        }
        contenido.setId(id);
        contenidoService.guardar(contenido);
        return ResponseEntity.ok(contenido);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarContenido(@PathVariable Long id) {
        if (!contenidoService.existePorId(id)) {
            return ResponseEntity.notFound().build();
        }
        contenidoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/categorias")
    public ResponseEntity<Void> agregarCategoria(
            @PathVariable Long id,
            @RequestBody Map<String, Long> body) {
        try {
            Long categoriaId = body.get("categoriaId");
            if (categoriaId == null) {
                return ResponseEntity.badRequest().build();
            }
            contenidoService.agregarCategoria(id, categoriaId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}/categorias/{categoriaId}")
    public ResponseEntity<Void> removerCategoria(
            @PathVariable Long id,
            @PathVariable Long categoriaId) {
        try {
            contenidoService.removerCategoria(id, categoriaId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/reservar")
    public ResponseEntity<Void> reservarCopia(@PathVariable Long id) {
        try {
            boolean reservado = contenidoService.reservarCopia(id);
            return reservado ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/devolver")
    public ResponseEntity<Void> devolverCopia(@PathVariable Long id) {
        try {
            contenidoService.devolverCopia(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
