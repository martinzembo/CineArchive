package edu.utn.inspt.cinearchive.backend.servicio;

import edu.utn.inspt.cinearchive.backend.modelo.Contenido;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz que define las operaciones disponibles para el servicio de Contenido
 */
public interface ContenidoService {

    List<Contenido> obtenerTodos();

    Optional<Contenido> obtenerPorId(Long id);

    List<Contenido> buscarPorTitulo(String titulo);

    List<Contenido> buscarPorGenero(String genero);

    List<Contenido> buscarPorTipo(Contenido.Tipo tipo);

    Contenido guardar(Contenido contenido);

    void eliminar(Long id);

    boolean existePorId(Long id);

    boolean existePorTitulo(String titulo);

    List<Contenido> obtenerDisponiblesParaAlquiler();

    List<Contenido> obtenerPorGestorInventario(Long gestorId);

    void agregarCategoria(Long contenidoId, Long categoriaId);

    void removerCategoria(Long contenidoId, Long categoriaId);

    boolean reservarCopia(Long contenidoId);

    void devolverCopia(Long contenidoId);

    // Alias para mantener compatibilidad
    default List<Contenido> getAll() {
        return obtenerTodos();
    }

    default Contenido getById(Long id) {
        return obtenerPorId(id)
            .orElseThrow(() -> new IllegalArgumentException("No existe un contenido con el ID: " + id));
    }
}
