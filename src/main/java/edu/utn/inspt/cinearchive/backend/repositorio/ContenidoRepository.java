package edu.utn.inspt.cinearchive.backend.repositorio;

import edu.utn.inspt.cinearchive.backend.modelo.Contenido;
import java.util.List;

public interface ContenidoRepository {
    Contenido findById(Long id);
    List<Contenido> findAll();
    List<Contenido> findByTituloLike(String tituloPattern);
    int save(Contenido contenido);
    int update(Contenido contenido);
    int delete(Long id);
    // Búsqueda con filtros opcionales
    List<Contenido> search(String q, String genero, String tipo, String orden);
}
