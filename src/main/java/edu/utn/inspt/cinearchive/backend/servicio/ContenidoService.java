package edu.utn.inspt.cinearchive.backend.servicio;

import edu.utn.inspt.cinearchive.backend.modelo.Contenido;
import java.util.List;

public interface ContenidoService {
    Contenido getById(Long id);
    List<Contenido> getAll();
    void create(Contenido contenido);
    void update(Contenido contenido);
    void delete(Long id);
    List<Contenido> searchByTitulo(String tituloPattern);
    // Búsqueda con filtros
    List<Contenido> search(String q, String genero, String tipo, String orden);
    // Paginada + total para UI
    List<Contenido> searchPaged(String q, String genero, String tipo, String orden, int page, int size);
    long searchCount(String q, String genero, String tipo);
    // Nuevo: versión ligera para catálogo
    List<Contenido> searchPagedLight(String q, String genero, String tipo, String orden, int page, int size);
    // Nuevo: temporadas por prefijo de título
    List<Contenido> getSeasonsByTitlePrefix(String titlePrefix);
}
