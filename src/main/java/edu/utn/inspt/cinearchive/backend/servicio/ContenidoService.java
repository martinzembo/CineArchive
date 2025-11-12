package edu.utn.inspt.cinearchive.backend.servicio;

import edu.utn.inspt.cinearchive.backend.modelo.Contenido;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz que define las operaciones disponibles para el servicio de Contenido
 */
public interface ContenidoService {
    
    // ===== MÉTODOS CRUD BÁSICOS =====
    
    /**
     * Obtiene un contenido por su ID
     * @param id ID del contenido
     * @return Optional con el contenido si existe
     */
    Optional<Contenido> getById(Long id);
    
    /**
     * Obtiene todos los contenidos
     */
    List<Contenido> getAll();
    
    /**
     * Crea un nuevo contenido
     * @param contenido Contenido a crear
     */
    void create(Contenido contenido);
    
    /**
     * Actualiza un contenido existente
     * @param contenido Contenido a actualizar
     */
    void update(Contenido contenido);
    
    /**
     * Elimina un contenido por su ID
     * @param id ID del contenido a eliminar
     */
    void delete(Long id);
    
    // ===== MÉTODOS DE BÚSQUEDA =====
    
    /**
     * Busca contenidos por patrón de título (LIKE)
     */
    List<Contenido> searchByTitulo(String tituloPattern);
    
    /**
     * Busca contenidos por título exacto
     */
    List<Contenido> findByTitulo(String titulo);
    
    /**
     * Busca contenidos por género
     */
    List<Contenido> findByGenero(String genero);
    
    /**
     * Busca contenidos por tipo (PELICULA o SERIE)
     */
    List<Contenido> findByTipo(Contenido.Tipo tipo);
    
    /**
     * Busca contenidos disponibles para alquiler
     */
    List<Contenido> findDisponiblesParaAlquiler();
    
    /**
     * Busca contenidos por gestor de inventario
     */
    List<Contenido> findByGestorInventario(Long gestorId);
    
    /**
     * Busca temporadas por prefijo de título
     */
    List<Contenido> getSeasonsByTitlePrefix(String titlePrefix);
    
    // ===== BÚSQUEDA AVANZADA CON FILTROS =====
    
    /**
     * Búsqueda con múltiples filtros
     * @param q Texto de búsqueda
     * @param genero Filtro por género
     * @param tipo Filtro por tipo
     * @param orden Criterio de ordenamiento
     */
    List<Contenido> search(String q, String genero, String tipo, String orden);
    
    /**
     * Búsqueda paginada con filtros
     * @param q Texto de búsqueda
     * @param genero Filtro por género
     * @param tipo Filtro por tipo
     * @param orden Criterio de ordenamiento
     * @param page Número de página (base 1)
     * @param size Tamaño de página
     */
    List<Contenido> searchPaged(String q, String genero, String tipo, String orden, int page, int size);
    
    /**
     * Búsqueda paginada ligera (solo campos esenciales para listados)
     */
    List<Contenido> searchPagedLight(String q, String genero, String tipo, String orden, int page, int size);
    
    /**
     * Cuenta el total de resultados para los filtros dados
     */
    long searchCount(String q, String genero, String tipo);
    
    // ===== MÉTODOS DE VALIDACIÓN =====
    
    /**
     * Verifica si existe un contenido con el ID dado
     */
    boolean existsById(Long id);
    
    /**
     * Verifica si existe un contenido con el título dado
     */
    boolean existsByTitulo(String titulo);
    
    // ===== GESTIÓN DE CATEGORÍAS =====
    
    /**
     * Agrega una categoría a un contenido
     */
    void agregarCategoria(Long contenidoId, Long categoriaId);
    
    /**
     * Remueve una categoría de un contenido
     */
    void removerCategoria(Long contenidoId, Long categoriaId);
    
    // ===== GESTIÓN DE INVENTARIO =====
    
    /**
     * Reserva una copia del contenido (decrementa copias disponibles)
     * @param contenidoId ID del contenido
     * @return true si se reservó exitosamente, false si no hay copias disponibles
     */
    boolean reservarCopia(Long contenidoId);
    
    /**
     * Devuelve una copia del contenido (incrementa copias disponibles)
     * @param contenidoId ID del contenido
     */
    void devolverCopia(Long contenidoId);

    // ===== MÉTODOS ALIAS (COMPATIBILIDAD) =====

    /**
     * Alias para getAll()
     */
    default List<Contenido> obtenerTodos() {
        return getAll();
    }

    /**
     * Alias para getById()
     */
    default Optional<Contenido> obtenerPorId(Long id) {
        return getById(id);
    }

    /**
     * Alias para searchByTitulo()
     */
    default List<Contenido> buscarPorTitulo(String titulo) {
        return searchByTitulo(titulo);
    }

    /**
     * Alias para findByGenero()
     */
    default List<Contenido> buscarPorGenero(String genero) {
        return findByGenero(genero);
    }

    /**
     * Alias para findByTipo()
     */
    default List<Contenido> buscarPorTipo(Contenido.Tipo tipo) {
        return findByTipo(tipo);
    }

    /**
     * Alias para findDisponiblesParaAlquiler()
     */
    default List<Contenido> obtenerDisponiblesParaAlquiler() {
        return findDisponiblesParaAlquiler();
    }

    /**
     * Alias para findByGestorInventario()
     */
    default List<Contenido> obtenerPorGestorInventario(Long gestorId) {
        return findByGestorInventario(gestorId);
    }

    /**
     * Alias para existsByTitulo()
     */
    default boolean existePorTitulo(String titulo) {
        return existsByTitulo(titulo);
    }

    /**
     * Alias para existsById()
     */
    default boolean existePorId(Long id) {
        return existsById(id);
    }

    /**
     * Alias combinado para create/update
     */
    default void guardar(Contenido contenido) {
        if (contenido.getId() == null) {
            create(contenido);
        } else {
            update(contenido);
        }
    }

    /**
     * Alias para delete()
     */
    default void eliminar(Long id) {
        delete(id);
    }
}