package edu.utn.inspt.cinearchive.backend.repositorio;

import edu.utn.inspt.cinearchive.backend.modelo.Contenido;
import java.util.List;
import java.util.Optional;

public interface ContenidoRepository {
    // Métodos CRUD básicos
    List<Contenido> findAll();
    Optional<Contenido> findById(Integer id);
    List<Contenido> findByTitulo(String titulo);
    List<Contenido> findByGenero(String genero);
    List<Contenido> findByAnio(Integer anio);
    List<Contenido> findByTipo(Contenido.Tipo tipo);
    List<Contenido> findAvailable();
    List<Contenido> findByCategoria(Integer categoriaId);
    Contenido save(Contenido contenido);
    void deleteById(Integer id);
    int updateDisponibilidad(Integer id, Integer disponibilidad);
    boolean existsById(Integer id);
    long count();

    // Métodos específicos adicionales
    boolean existsByTitulo(String titulo);
    List<Contenido> findByTituloContainingIgnoreCase(String titulo);
    List<Contenido> findDisponiblesParaAlquiler();
    List<Contenido> findByGestorInventarioId(Long gestorId);
}
