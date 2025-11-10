package edu.utn.inspt.cinearchive.backend.repositorio;

import edu.utn.inspt.cinearchive.backend.modelo.Resena;
import java.util.List;
import java.util.Optional;

public interface ResenaRepository {
    // Métodos CRUD básicos
    List<Resena> findAll();
    Optional<Resena> findById(Integer id);
    List<Resena> findByUsuarioId(Integer usuarioId);
    List<Resena> findByContenidoId(Integer contenidoId);
    List<Resena> findByCalificacion(Integer calificacion);
    List<Resena> findByCalificacionRange(Integer minCalificacion, Integer maxCalificacion);
    Optional<Resena> findByUsuarioIdAndContenidoId(Integer usuarioId, Integer contenidoId);
    Resena save(Resena resena);
    void deleteById(Integer id);
    boolean existsByUsuarioIdAndContenidoId(Integer usuarioId, Integer contenidoId);
    boolean existsById(Integer id);
    long count();
    long countByContenidoId(Integer contenidoId);
    Double getPromedioCalificacionByContenidoId(Integer contenidoId);
}
