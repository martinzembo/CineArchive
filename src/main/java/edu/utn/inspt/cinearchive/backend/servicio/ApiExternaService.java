package edu.utn.inspt.cinearchive.backend.servicio;

import edu.utn.inspt.cinearchive.backend.modelo.Contenido;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Interfaz que define las operaciones para integración con APIs externas
 * Soporta TMDb (The Movie Database) y OMDb (Open Movie Database)
 */
public interface ApiExternaService {

    // ==================== BÚSQUEDA DE CONTENIDO ====================

    /**
     * Busca películas en APIs externas por título
     */
    List<Map<String, Object>> buscarPeliculas(String titulo, int limite);

    /**
     * Busca series en APIs externas por título
     */
    List<Map<String, Object>> buscarSeries(String titulo, int limite);

    /**
     * Busca contenido general (películas y series) por título
     */
    List<Map<String, Object>> buscarContenido(String titulo, int limite);

    /**
     * Obtiene detalles completos de una película por ID externo
     */
    Optional<Map<String, Object>> obtenerDetallesPelicula(String idExterno, String fuente);

    /**
     * Obtiene detalles completos de una serie por ID externo
     */
    Optional<Map<String, Object>> obtenerDetallesSerie(String idExterno, String fuente);

    // ==================== IMPORTACIÓN DE CONTENIDO ====================

    /**
     * Importa una película desde una API externa al sistema local
     */
    Optional<Contenido> importarPelicula(String idExterno, String fuente, Long gestorInventarioId);

    /**
     * Importa una serie desde una API externa al sistema local
     */
    Optional<Contenido> importarSerie(String idExterno, String fuente, Long gestorInventarioId);

    /**
     * Importa múltiples contenidos desde resultados de búsqueda
     */
    List<Contenido> importarContenidos(List<String> idsExternos, String fuente, Long gestorInventarioId);

    // ==================== ACTUALIZACIÓN DE CONTENIDO ====================

    /**
     * Actualiza información de contenido existente con datos de API externa
     */
    boolean actualizarContenidoDesdeApi(Long contenidoId);

    /**
     * Sincroniza todos los contenidos que tienen ID de API externa
     */
    Map<String, Object> sincronizarTodosLosContenidos();

    // ==================== CONFIGURACIÓN Y UTILIDADES ====================

    /**
     * Verifica la conectividad con las APIs externas
     */
    Map<String, Boolean> verificarConectividadApis();

    /**
     * Obtiene géneros disponibles desde las APIs externas
     */
    List<Map<String, Object>> obtenerGenerosDisponibles();

    /**
     * Obtiene contenido popular/trending de las APIs
     */
    List<Map<String, Object>> obtenerContenidoPopular(String tipo, int limite);

    /**
     * Valida y normaliza datos de contenido desde API externa
     */
    Map<String, Object> validarYNormalizarDatos(Map<String, Object> datosApi, String fuente);
}
