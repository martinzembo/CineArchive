package edu.utn.inspt.cinearchive.backend.repositorio;

import edu.utn.inspt.cinearchive.backend.modelo.Contenido;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para operaciones de persistencia de Contenido
 */
public interface ContenidoRepository {
    
    // ===== MÉTODOS CRUD BÁSICOS =====
    
    /**
     * Busca un contenido por su ID
     * @param id ID del contenido
     * @return Optional con el contenido si existe
     */
    Optional<Contenido> findById(Long id);
    
    /**
     * Obtiene todos los contenidos
     */
    List<Contenido> findAll();
    
    /**
     * Guarda un nuevo contenido o actualiza uno existente
     * @param contenido Contenido a guardar
     * @return Número de filas afectadas
     */
    int save(Contenido contenido);
    
    /**
     * Actualiza un contenido existente
     * @param contenido Contenido a actualizar
     * @return Número de filas afectadas
     */
    int update(Contenido contenido);
    
    /**
     * Elimina un contenido por su ID
     * @param id ID del contenido
     * @return Número de filas afectadas
     */
    int delete(Long id);
    
    // ===== MÉTODOS DE BÚSQUEDA =====
    
    /**
     * Busca contenidos por patrón de título (LIKE)
     */
    List<Contenido> findByTituloLike(String tituloPattern);
    
    /**
     * Busca contenidos por título exacto
     */
    List<Contenido> findByTitulo(String titulo);
    
    /**
     * Busca contenidos que contengan el texto en el título (case-insensitive)
     */
    List<Contenido> findByTituloContainingIgnoreCase(String titulo);
    
    /**
     * Busca contenidos por género
     */
    List<Contenido> findByGenero(String genero);
    
    /**
     * Busca contenidos por año
     */
    List<Contenido> findByAnio(Integer anio);
    
    /**
     * Busca contenidos por tipo (PELICULA o SERIE)
     */
    List<Contenido> findByTipo(Contenido.Tipo tipo);
    
    /**
     * Busca contenidos disponibles
     */
    List<Contenido> findAvailable();
    
    /**
     * Busca contenidos disponibles para alquiler
     */
    List<Contenido> findDisponiblesParaAlquiler();
    
    /**
     * Busca contenidos por categoría
     */
    List<Contenido> findByCategoria(Integer categoriaId);
    
    /**
     * Busca contenidos por gestor de inventario
     */
    List<Contenido> findByGestorInventarioId(Long gestorId);
    
    /**
     * Busca temporadas por prefijo de título (para series del mismo universo)
     */
    List<Contenido> findSeasonsByTitlePrefix(String titlePrefix);
    
    // ===== MÉTODOS DE BÚSQUEDA AVANZADA =====
    
    /**
     * Búsqueda con filtros opcionales
     * @param q Texto de búsqueda
     * @param genero Filtro por género
     * @param tipo Filtro por tipo
     * @param orden Criterio de ordenamiento
     */
    List<Contenido> search(String q, String genero, String tipo, String orden);
    
    /**
     * Búsqueda paginada (page base 1)
     */
    List<Contenido> searchPaged(String q, String genero, String tipo, String orden, int page, int size);
    
    /**
     * Búsqueda ligera paginada (solo columnas necesarias para listado)
     */
    List<Contenido> searchPagedLight(String q, String genero, String tipo, String orden, int page, int size);
    
    /**
     * Cuenta total de resultados para los mismos filtros
     */
    long searchCount(String q, String genero, String tipo);
    
    // ===== MÉTODOS DE VALIDACIÓN Y CONTEO =====
    
    /**
     * Verifica si existe un contenido por ID
     */
    boolean existsById(Long id);
    
    /**
     * Verifica si existe un contenido por título
     */
    boolean existsByTitulo(String titulo);
    
    /**
     * Cuenta el total de contenidos
     */
    long count();
    
    // ===== MÉTODOS DE GESTIÓN DE INVENTARIO =====
    
    /**
     * Ajuste de stock de copias disponibles
     * @param contenidoId ID del contenido
     * @param delta Cantidad a sumar o restar
     * @return Número de filas afectadas
     */
    int updateCopiasDisponibles(Long contenidoId, int delta);
    
    /**
     * Actualiza la disponibilidad de un contenido
     * @param id ID del contenido
     * @param disponibilidad Nueva disponibilidad
     * @return Número de filas afectadas
     */
    int updateDisponibilidad(Long id, Integer disponibilidad);
    
    /**
     * Reserva atómica de una copia (decrementa si hay stock)
     * @param contenidoId ID del contenido
     * @return Filas afectadas (1=ok, 0=sin stock)
     */
    int reserveCopy(Long contenidoId);
}