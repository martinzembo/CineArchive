package edu.utn.inspt.cinearchive.frontend.controlador;

import edu.utn.inspt.cinearchive.backend.modelo.Categoria;
import edu.utn.inspt.cinearchive.backend.servicio.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    @Autowired
    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public ResponseEntity<List<Categoria>> listarCategorias() {
        return ResponseEntity.ok(categoriaService.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categoria> obtenerCategoria(@PathVariable int id) {
        return categoriaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Categoria>> listarPorTipo(@PathVariable Categoria.Tipo tipo) {
        return ResponseEntity.ok(categoriaService.obtenerPorTipo(tipo));
    }

    @GetMapping("/generos")
    public ResponseEntity<List<Categoria>> listarGeneros() {
        return ResponseEntity.ok(categoriaService.obtenerGeneros());
    }

    @GetMapping("/tags")
    public ResponseEntity<List<Categoria>> listarTags() {
        return ResponseEntity.ok(categoriaService.obtenerTags());
    }

    @GetMapping("/clasificaciones")
    public ResponseEntity<List<Categoria>> listarClasificaciones() {
        return ResponseEntity.ok(categoriaService.obtenerClasificaciones());
    }

    @PostMapping
    public ResponseEntity<Categoria> crearCategoria(@Valid @RequestBody Categoria categoria) {
        if (categoriaService.existePorNombre(categoria.getNombre())) {
            return ResponseEntity.badRequest().build();
        }
        Categoria nuevaCategoria = categoriaService.guardar(categoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCategoria);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Categoria> actualizarCategoria(
            @PathVariable int id,
            @Valid @RequestBody Categoria categoria) {
        if (!categoriaService.existePorId(id)) {
            return ResponseEntity.notFound().build();
        }
        categoria.setId(id);
        return ResponseEntity.ok(categoriaService.guardar(categoria));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable int id) {
        if (!categoriaService.existePorId(id)) {
            return ResponseEntity.notFound().build();
        }
        categoriaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<Categoria> buscarPorNombre(@PathVariable String nombre) {
        return categoriaService.obtenerPorNombre(nombre)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
