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
    // Ajuste de stock de copias disponibles
    int updateCopiasDisponibles(Long contenidoId, int delta);
    // Búsqueda paginada (page base 1) y conteo total para los mismos filtros
    List<Contenido> searchPaged(String q, String genero, String tipo, String orden, int page, int size);
    long searchCount(String q, String genero, String tipo);
    // Reserva atómica de una copia (decrementa si hay stock). Devuelve filas afectadas (1=ok,0=sin stock)
    int reserveCopy(Long contenidoId);
    // Nuevo: búsqueda ligera paginada (solo columnas necesarias para listado)
    List<Contenido> searchPagedLight(String q, String genero, String tipo, String orden, int page, int size);
    // Nuevo: buscar temporadas por prefijo de título (para series del mismo universo)
    List<Contenido> findSeasonsByTitlePrefix(String titlePrefix);
}
