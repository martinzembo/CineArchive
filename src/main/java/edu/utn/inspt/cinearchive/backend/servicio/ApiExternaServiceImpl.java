package edu.utn.inspt.cinearchive.backend.servicio;

import edu.utn.inspt.cinearchive.backend.modelo.Contenido;
import edu.utn.inspt.cinearchive.backend.repositorio.ContenidoRepository;
import edu.utn.inspt.cinearchive.backend.util.HttpClientUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Logger;

/**
 * Servicio para integración con APIs externas (TMDb, OMDb)
 */
@Service
@Transactional
public class ApiExternaServiceImpl implements ApiExternaService {

    private static final Logger logger = Logger.getLogger(ApiExternaServiceImpl.class.getName());
    
    // URLs base de las APIs
    private static final String TMDB_BASE_URL = "https://api.themoviedb.org/3";
    private static final String OMDB_BASE_URL = "http://www.omdbapi.com";
    
    // API Keys (deben configurarse en application.properties)
    @Value("${api.tmdb.key:}")
    private String tmdbApiKey;
    
    @Value("${api.omdb.key:}")
    private String omdbApiKey;
    
    private final HttpClientUtil httpClient;
    private final ContenidoRepository contenidoRepository;
    private final Gson gson;

    @Autowired
    public ApiExternaServiceImpl(HttpClientUtil httpClient, ContenidoRepository contenidoRepository) {
        this.httpClient = httpClient;
        this.contenidoRepository = contenidoRepository;
        this.gson = new Gson();
    }

    // ==================== BÚSQUEDA DE CONTENIDO ====================

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> buscarPeliculas(String titulo, int limite) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título de búsqueda es obligatorio");
        }
        if (limite <= 0 || limite > 50) {
            limite = 20; // Valor por defecto
        }
        
        List<Map<String, Object>> resultados = new ArrayList<>();
        
        // Buscar en TMDb
        if (!tmdbApiKey.isEmpty()) {
            resultados.addAll(buscarPeliculasEnTMDb(titulo, limite / 2));
        }
        
        // Buscar en OMDb si no hay suficientes resultados
        if (resultados.size() < limite && !omdbApiKey.isEmpty()) {
            resultados.addAll(buscarPeliculasEnOMDb(titulo, limite - resultados.size()));
        }
        
        return resultados.size() > limite ? resultados.subList(0, limite) : resultados;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> buscarSeries(String titulo, int limite) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título de búsqueda es obligatorio");
        }
        if (limite <= 0 || limite > 50) {
            limite = 20;
        }
        
        List<Map<String, Object>> resultados = new ArrayList<>();
        
        // Solo TMDb para series (OMDb tiene soporte limitado)
        if (!tmdbApiKey.isEmpty()) {
            resultados.addAll(buscarSeriesEnTMDb(titulo, limite));
        }
        
        return resultados;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> buscarContenido(String titulo, int limite) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título de búsqueda es obligatorio");
        }
        
        List<Map<String, Object>> resultados = new ArrayList<>();
        
        // Buscar películas
        resultados.addAll(buscarPeliculas(titulo, limite / 2));
        
        // Buscar series
        resultados.addAll(buscarSeries(titulo, limite / 2));
        
        return resultados.size() > limite ? resultados.subList(0, limite) : resultados;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Map<String, Object>> obtenerDetallesPelicula(String idExterno, String fuente) {
        if (idExterno == null || idExterno.trim().isEmpty()) {
            return Optional.empty();
        }
        
        switch (fuente.toLowerCase()) {
            case "tmdb":
                return obtenerDetallesPeliculaTMDb(idExterno);
            case "omdb":
                return obtenerDetallesPeliculaOMDb(idExterno);
            default:
                logger.warning("Fuente de API desconocida: " + fuente);
                return Optional.empty();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Map<String, Object>> obtenerDetallesSerie(String idExterno, String fuente) {
        if (idExterno == null || idExterno.trim().isEmpty()) {
            return Optional.empty();
        }
        
        if ("tmdb".equalsIgnoreCase(fuente)) {
            return obtenerDetallesSereTMDb(idExterno);
        }
        
        logger.warning("Fuente no compatible para series: " + fuente);
        return Optional.empty();
    }

    // ==================== IMPORTACIÓN DE CONTENIDO ====================

    @Override
    public Optional<Contenido> importarPelicula(String idExterno, String fuente, Long gestorInventarioId) {
        if (gestorInventarioId == null || gestorInventarioId <= 0) {
            throw new IllegalArgumentException("El ID del gestor de inventario es obligatorio");
        }
        
        Optional<Map<String, Object>> detallesOpt = obtenerDetallesPelicula(idExterno, fuente);
        
        if (!detallesOpt.isPresent()) {
            logger.warning("No se pudieron obtener detalles para película ID: " + idExterno + " de fuente: " + fuente);
            return Optional.empty();
        }
        
        Map<String, Object> detalles = validarYNormalizarDatos(detallesOpt.get(), fuente);
        
        try {
            Contenido contenido = mapearAPeliculaLocal(detalles, gestorInventarioId);
            contenido.setIdApiExterna(fuente.toLowerCase() + ":" + idExterno);
            
            // Verificar si ya existe
            if (contenidoRepository.existsByTitulo(contenido.getTitulo())) {
                logger.info("La película ya existe en el sistema: " + contenido.getTitulo());
                return Optional.empty();
            }
            
            int resultado = contenidoRepository.save(contenido);
            if (resultado > 0) {
                logger.info("Película importada exitosamente: " + contenido.getTitulo());
                return Optional.of(contenido);
            } else {
                logger.severe("Error al guardar película: " + contenido.getTitulo());
                return Optional.empty();
            }

        } catch (Exception e) {
            logger.severe("Error al importar película: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<Contenido> importarSerie(String idExterno, String fuente, Long gestorInventarioId) {
        if (gestorInventarioId == null || gestorInventarioId <= 0) {
            throw new IllegalArgumentException("El ID del gestor de inventario es obligatorio");
        }
        
        Optional<Map<String, Object>> detallesOpt = obtenerDetallesSerie(idExterno, fuente);
        
        if (!detallesOpt.isPresent()) {
            logger.warning("No se pudieron obtener detalles para serie ID: " + idExterno + " de fuente: " + fuente);
            return Optional.empty();
        }
        
        Map<String, Object> detalles = validarYNormalizarDatos(detallesOpt.get(), fuente);
        
        try {
            Contenido contenido = mapearASerieLocal(detalles, gestorInventarioId);
            contenido.setIdApiExterna(fuente.toLowerCase() + ":" + idExterno);
            
            if (contenidoRepository.existsByTitulo(contenido.getTitulo())) {
                logger.info("La serie ya existe en el sistema: " + contenido.getTitulo());
                return Optional.empty();
            }
            
            int resultado = contenidoRepository.save(contenido);
            if (resultado > 0) {
                logger.info("Serie importada exitosamente: " + contenido.getTitulo());
                return Optional.of(contenido);
            } else {
                logger.severe("Error al guardar serie: " + contenido.getTitulo());
                return Optional.empty();
            }

        } catch (Exception e) {
            logger.severe("Error al importar serie: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public List<Contenido> importarContenidos(List<String> idsExternos, String fuente, Long gestorInventarioId) {
        if (idsExternos == null || idsExternos.isEmpty()) {
            return new ArrayList<>();
        }
        
        List<Contenido> contenidosImportados = new ArrayList<>();
        
        for (String id : idsExternos) {
            try {
                // Intentar como película primero
                Optional<Contenido> pelicula = importarPelicula(id, fuente, gestorInventarioId);
                if (pelicula.isPresent()) {
                    contenidosImportados.add(pelicula.get());
                    continue;
                }
                
                // Si no es película, intentar como serie
                Optional<Contenido> serie = importarSerie(id, fuente, gestorInventarioId);
                if (serie.isPresent()) {
                    contenidosImportados.add(serie.get());
                }
                
            } catch (Exception e) {
                logger.warning("Error al importar contenido ID " + id + ": " + e.getMessage());
            }
        }
        
        return contenidosImportados;
    }

    // ==================== ACTUALIZACIÓN DE CONTENIDO ====================

    @Override
    public boolean actualizarContenidoDesdeApi(Long contenidoId) {
        Optional<Contenido> contenidoOpt = contenidoRepository.findById(contenidoId);

        if (!contenidoOpt.isPresent()) {
            logger.warning("Contenido no encontrado con ID: " + contenidoId);
            return false;
        }
        
        Contenido contenido = contenidoOpt.get();
        String idApiExterna = contenido.getIdApiExterna();
        
        if (idApiExterna == null || idApiExterna.trim().isEmpty()) {
            logger.warning("El contenido no tiene ID de API externa: " + contenido.getTitulo());
            return false;
        }
        
        try {
            String[] partes = idApiExterna.split(":");
            if (partes.length != 2) {
                logger.warning("Formato de ID API externa inválido: " + idApiExterna);
                return false;
            }
            
            String fuente = partes[0];
            String idExterno = partes[1];
            
            Optional<Map<String, Object>> detallesOpt;
            if (contenido.getTipo() == Contenido.Tipo.PELICULA) {
                detallesOpt = obtenerDetallesPelicula(idExterno, fuente);
            } else {
                detallesOpt = obtenerDetallesSerie(idExterno, fuente);
            }
            
            if (!detallesOpt.isPresent()) {
                logger.warning("No se pudieron obtener detalles actualizados para: " + contenido.getTitulo());
                return false;
            }
            
            Map<String, Object> detalles = validarYNormalizarDatos(detallesOpt.get(), fuente);
            actualizarContenidoConDatosApi(contenido, detalles);
            
            contenidoRepository.save(contenido);
            logger.info("Contenido actualizado exitosamente: " + contenido.getTitulo());
            return true;
            
        } catch (Exception e) {
            logger.severe("Error al actualizar contenido desde API: " + e.getMessage());
            return false;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> sincronizarTodosLosContenidos() {
        List<Contenido> contenidos = contenidoRepository.findAll();
        
        Map<String, Object> resultado = new HashMap<>();
        int exitosos = 0;
        int fallidos = 0;
        
        for (Contenido contenido : contenidos) {
            if (contenido.getIdApiExterna() != null && !contenido.getIdApiExterna().trim().isEmpty()) {
                if (actualizarContenidoDesdeApi(contenido.getId())) {
                    exitosos++;
                } else {
                    fallidos++;
                }
            }
        }
        
        resultado.put("total_contenidos", contenidos.size());
        resultado.put("actualizados_exitosamente", exitosos);
        resultado.put("fallidos", fallidos);
        resultado.put("sin_api_externa", contenidos.size() - exitosos - fallidos);
        
        return resultado;
    }

    // ==================== CONFIGURACIÓN Y UTILIDADES ====================

    @Override
    @Transactional(readOnly = true)
    public Map<String, Boolean> verificarConectividadApis() {
        Map<String, Boolean> conectividad = new HashMap<>();
        
        // Verificar TMDb
        if (!tmdbApiKey.isEmpty()) {
            String url = TMDB_BASE_URL + "/configuration?api_key=" + tmdbApiKey;
            String respuesta = httpClient.executeGet(url);
            conectividad.put("tmdb", respuesta != null);
        } else {
            conectividad.put("tmdb", false);
        }
        
        // Verificar OMDb
        if (!omdbApiKey.isEmpty()) {
            String url = OMDB_BASE_URL + "?apikey=" + omdbApiKey + "&i=tt0111161"; // Test con película conocida
            String respuesta = httpClient.executeGet(url);
            conectividad.put("omdb", respuesta != null);
        } else {
            conectividad.put("omdb", false);
        }
        
        return conectividad;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> obtenerGenerosDisponibles() {
        List<Map<String, Object>> generos = new ArrayList<>();
        
        // Obtener géneros de TMDb
        if (!tmdbApiKey.isEmpty()) {
            generos.addAll(obtenerGenerosTMDb());
        }
        
        return generos;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> obtenerContenidoPopular(String tipo, int limite) {
        List<Map<String, Object>> contenidoPopular = new ArrayList<>();
        
        if (!tmdbApiKey.isEmpty()) {
            if ("movie".equalsIgnoreCase(tipo)) {
                contenidoPopular.addAll(obtenerPeliculasPopularesTMDb(limite));
            } else if ("tv".equalsIgnoreCase(tipo)) {
                contenidoPopular.addAll(obtenerSeriesPopularesTMDb(limite));
            } else {
                contenidoPopular.addAll(obtenerPeliculasPopularesTMDb(limite / 2));
                contenidoPopular.addAll(obtenerSeriesPopularesTMDb(limite / 2));
            }
        }
        
        return contenidoPopular;
    }

    @Override
    public Map<String, Object> validarYNormalizarDatos(Map<String, Object> datosApi, String fuente) {
        Map<String, Object> datosNormalizados = new HashMap<>(datosApi);
        
        // Normalizar campos según la fuente
        if ("tmdb".equalsIgnoreCase(fuente)) {
            normalizarDatosTMDb(datosNormalizados);
        } else if ("omdb".equalsIgnoreCase(fuente)) {
            normalizarDatosOMDb(datosNormalizados);
        }
        
        return datosNormalizados;
    }

    // ==================== MÉTODOS PRIVADOS PARA TMDb ====================

    private List<Map<String, Object>> buscarPeliculasEnTMDb(String titulo, int limite) {
        String url = httpClient.buildUrlWithParams(
            TMDB_BASE_URL + "/search/movie",
            "api_key=" + tmdbApiKey + "&query=" + titulo.replace(" ", "%20") + "&language=es"
        );
        
        String respuesta = httpClient.executeGet(url);
        return procesarRespuestaBusquedaTMDb(respuesta, limite);
    }

    private List<Map<String, Object>> buscarSeriesEnTMDb(String titulo, int limite) {
        String url = httpClient.buildUrlWithParams(
            TMDB_BASE_URL + "/search/tv",
            "api_key=" + tmdbApiKey + "&query=" + titulo.replace(" ", "%20") + "&language=es"
        );
        
        String respuesta = httpClient.executeGet(url);
        return procesarRespuestaBusquedaTMDb(respuesta, limite);
    }

    private Optional<Map<String, Object>> obtenerDetallesPeliculaTMDb(String idExterno) {
        String url = httpClient.buildUrlWithParams(
            TMDB_BASE_URL + "/movie/" + idExterno,
            "api_key=" + tmdbApiKey + "&language=es"
        );
        
        String respuesta = httpClient.executeGet(url);
        if (respuesta != null) {
            try {
                return Optional.of(gson.fromJson(respuesta, Map.class));
            } catch (Exception e) {
                logger.warning("Error al parsear respuesta TMDb: " + e.getMessage());
            }
        }
        return Optional.empty();
    }

    private Optional<Map<String, Object>> obtenerDetallesSereTMDb(String idExterno) {
        String url = httpClient.buildUrlWithParams(
            TMDB_BASE_URL + "/tv/" + idExterno,
            "api_key=" + tmdbApiKey + "&language=es"
        );
        
        String respuesta = httpClient.executeGet(url);
        if (respuesta != null) {
            try {
                return Optional.of(gson.fromJson(respuesta, Map.class));
            } catch (Exception e) {
                logger.warning("Error al parsear respuesta TMDb: " + e.getMessage());
            }
        }
        return Optional.empty();
    }

    // ==================== MÉTODOS PRIVADOS PARA OMDb ====================

    private List<Map<String, Object>> buscarPeliculasEnOMDb(String titulo, int limite) {
        String url = httpClient.buildUrlWithParams(
            OMDB_BASE_URL,
            "apikey=" + omdbApiKey + "&s=" + titulo.replace(" ", "+") + "&type=movie"
        );
        
        String respuesta = httpClient.executeGet(url);
        return procesarRespuestaBusquedaOMDb(respuesta, limite);
    }

    private Optional<Map<String, Object>> obtenerDetallesPeliculaOMDb(String idExterno) {
        String url = httpClient.buildUrlWithParams(
            OMDB_BASE_URL,
            "apikey=" + omdbApiKey + "&i=" + idExterno + "&plot=full"
        );
        
        String respuesta = httpClient.executeGet(url);
        if (respuesta != null) {
            try {
                return Optional.of(gson.fromJson(respuesta, Map.class));
            } catch (Exception e) {
                logger.warning("Error al parsear respuesta OMDb: " + e.getMessage());
            }
        }
        return Optional.empty();
    }

    // ==================== MÉTODOS AUXILIARES ====================

    private List<Map<String, Object>> procesarRespuestaBusquedaTMDb(String respuesta, int limite) {
        List<Map<String, Object>> resultados = new ArrayList<>();
        
        if (respuesta != null) {
            try {
                JsonObject json = gson.fromJson(respuesta, JsonObject.class);
                JsonArray results = json.getAsJsonArray("results");
                
                for (JsonElement elemento : results) {
                    if (resultados.size() >= limite) break;
                    
                    JsonObject item = elemento.getAsJsonObject();
                    Map<String, Object> resultado = gson.fromJson(item, Map.class);
                    resultado.put("fuente", "tmdb");
                    resultados.add(resultado);
                }
            } catch (Exception e) {
                logger.warning("Error al procesar respuesta de búsqueda TMDb: " + e.getMessage());
            }
        }
        
        return resultados;
    }

    private List<Map<String, Object>> procesarRespuestaBusquedaOMDb(String respuesta, int limite) {
        List<Map<String, Object>> resultados = new ArrayList<>();
        
        if (respuesta != null) {
            try {
                JsonObject json = gson.fromJson(respuesta, JsonObject.class);
                
                if ("True".equals(json.get("Response").getAsString())) {
                    JsonArray search = json.getAsJsonArray("Search");
                    
                    for (JsonElement elemento : search) {
                        if (resultados.size() >= limite) break;
                        
                        JsonObject item = elemento.getAsJsonObject();
                        Map<String, Object> resultado = gson.fromJson(item, Map.class);
                        resultado.put("fuente", "omdb");
                        resultados.add(resultado);
                    }
                }
            } catch (Exception e) {
                logger.warning("Error al procesar respuesta de búsqueda OMDb: " + e.getMessage());
            }
        }
        
        return resultados;
    }

    private List<Map<String, Object>> obtenerGenerosTMDb() {
        List<Map<String, Object>> generos = new ArrayList<>();
        
        // Géneros de películas
        String urlPeliculas = httpClient.buildUrlWithParams(
            TMDB_BASE_URL + "/genre/movie/list",
            "api_key=" + tmdbApiKey + "&language=es"
        );
        
        String respuestaPeliculas = httpClient.executeGet(urlPeliculas);
        generos.addAll(procesarGenerosRespuesta(respuestaPeliculas, "movie"));
        
        // Géneros de series
        String urlSeries = httpClient.buildUrlWithParams(
            TMDB_BASE_URL + "/genre/tv/list",
            "api_key=" + tmdbApiKey + "&language=es"
        );
        
        String respuestaSeries = httpClient.executeGet(urlSeries);
        generos.addAll(procesarGenerosRespuesta(respuestaSeries, "tv"));
        
        return generos;
    }

    private List<Map<String, Object>> procesarGenerosRespuesta(String respuesta, String tipo) {
        List<Map<String, Object>> generos = new ArrayList<>();
        
        if (respuesta != null) {
            try {
                JsonObject json = gson.fromJson(respuesta, JsonObject.class);
                JsonArray genres = json.getAsJsonArray("genres");
                
                for (JsonElement elemento : genres) {
                    JsonObject genero = elemento.getAsJsonObject();
                    Map<String, Object> generoMap = new HashMap<>();
                    generoMap.put("id", genero.get("id").getAsInt());
                    generoMap.put("nombre", genero.get("name").getAsString());
                    generoMap.put("tipo", tipo);
                    generoMap.put("fuente", "tmdb");
                    generos.add(generoMap);
                }
            } catch (Exception e) {
                logger.warning("Error al procesar géneros: " + e.getMessage());
            }
        }
        
        return generos;
    }

    private List<Map<String, Object>> obtenerPeliculasPopularesTMDb(int limite) {
        String url = httpClient.buildUrlWithParams(
            TMDB_BASE_URL + "/movie/popular",
            "api_key=" + tmdbApiKey + "&language=es&page=1"
        );
        
        String respuesta = httpClient.executeGet(url);
        return procesarRespuestaBusquedaTMDb(respuesta, limite);
    }

    private List<Map<String, Object>> obtenerSeriesPopularesTMDb(int limite) {
        String url = httpClient.buildUrlWithParams(
            TMDB_BASE_URL + "/tv/popular",
            "api_key=" + tmdbApiKey + "&language=es&page=1"
        );
        
        String respuesta = httpClient.executeGet(url);
        return procesarRespuestaBusquedaTMDb(respuesta, limite);
    }

    private void normalizarDatosTMDb(Map<String, Object> datos) {
        // Normalizar título
        if (datos.containsKey("title")) {
            datos.put("titulo_normalizado", datos.get("title"));
        } else if (datos.containsKey("name")) {
            datos.put("titulo_normalizado", datos.get("name"));
        }
        
        // Normalizar fecha de lanzamiento
        if (datos.containsKey("release_date")) {
            datos.put("fecha_lanzamiento", datos.get("release_date"));
        } else if (datos.containsKey("first_air_date")) {
            datos.put("fecha_lanzamiento", datos.get("first_air_date"));
        }
        
        // Normalizar descripción
        if (datos.containsKey("overview")) {
            datos.put("descripcion_normalizada", datos.get("overview"));
        }
    }

    private void normalizarDatosOMDb(Map<String, Object> datos) {
        // Normalizar campos de OMDb
        if (datos.containsKey("Title")) {
            datos.put("titulo_normalizado", datos.get("Title"));
        }
        
        if (datos.containsKey("Year")) {
            datos.put("anio_normalizado", datos.get("Year"));
        }
        
        if (datos.containsKey("Plot")) {
            datos.put("descripcion_normalizada", datos.get("Plot"));
        }
    }

    private Contenido mapearAPeliculaLocal(Map<String, Object> datos, Long gestorInventarioId) {
        Contenido contenido = new Contenido();

        // Datos básicos
        contenido.setTitulo(obtenerValorString(datos, "titulo_normalizado", "Sin título"));
        contenido.setDescripcion(obtenerValorString(datos, "descripcion_normalizada", ""));
        contenido.setTipo(Contenido.Tipo.PELICULA);
        contenido.setGestorInventarioId(gestorInventarioId);

        // Año
        String fechaLanzamiento = obtenerValorString(datos, "fecha_lanzamiento", null);
        if (fechaLanzamiento != null && !fechaLanzamiento.isEmpty()) {
            try {
                contenido.setAnio(Integer.parseInt(fechaLanzamiento.substring(0, 4)));
            } catch (Exception e) {
                contenido.setAnio(LocalDate.now().getYear());
            }
        } else {
            contenido.setAnio(LocalDate.now().getYear());
        }

        // Duración (TMDb lo da en minutos)
        if (datos.containsKey("runtime")) {
            try {
                Object runtime = datos.get("runtime");
                if (runtime instanceof Number) {
                    contenido.setDuracion(((Number) runtime).intValue());
                }
            } catch (Exception e) {
                logger.warning("Error al mapear duración: " + e.getMessage());
            }
        }

        // Valores por defecto
        contenido.setDisponibleParaAlquiler(true);
        contenido.setPrecioAlquiler(new BigDecimal("5.99")); // Precio por defecto
        contenido.setCopiasDisponibles(1);
        contenido.setCopiasTotales(1);

        return contenido;
    }

    private Contenido mapearASerieLocal(Map<String, Object> datos, Long gestorInventarioId) {
        Contenido contenido = new Contenido();

        // Datos básicos
        contenido.setTitulo(obtenerValorString(datos, "titulo_normalizada", "Sin título"));
        contenido.setDescripcion(obtenerValorString(datos, "descripcion_normalizada", ""));
        contenido.setTipo(Contenido.Tipo.SERIE);
        contenido.setGestorInventarioId(gestorInventarioId);

        // Año
        String fechaLanzamiento = obtenerValorString(datos, "fecha_lanzamiento", null);
        if (fechaLanzamiento != null && !fechaLanzamiento.isEmpty()) {
            try {
                contenido.setAnio(Integer.parseInt(fechaLanzamiento.substring(0, 4)));
            } catch (Exception e) {
                contenido.setAnio(LocalDate.now().getYear());
            }
        } else {
            contenido.setAnio(LocalDate.now().getYear());
        }

        // Temporadas
        if (datos.containsKey("number_of_seasons")) {
            try {
                Object seasons = datos.get("number_of_seasons");
                if (seasons instanceof Number) {
                    contenido.setTemporadas(((Number) seasons).intValue());
                }
            } catch (Exception e) {
                contenido.setTemporadas(1);
            }
        } else {
            contenido.setTemporadas(1);
        }

        // Episodios
        if (datos.containsKey("number_of_episodes")) {
            try {
                Object episodes = datos.get("number_of_episodes");
                if (episodes instanceof Number) {
                    contenido.setCapitulosTotales(((Number) episodes).intValue());
                }
            } catch (Exception e) {
                contenido.setCapitulosTotales(0);
            }
        }

        // Estado de emisión
        if (datos.containsKey("status")) {
            String status = obtenerValorString(datos, "status", "");
            contenido.setEnEmision(!"Ended".equalsIgnoreCase(status) && !"Canceled".equalsIgnoreCase(status));
        } else {
            contenido.setEnEmision(false);
        }

        // Valores por defecto
        contenido.setDisponibleParaAlquiler(true);
        contenido.setPrecioAlquiler(new BigDecimal("3.99"));
        contenido.setCopiasDisponibles(1);
        contenido.setCopiasTotales(1);

        return contenido;
    }

    private void actualizarContenidoConDatosApi(Contenido contenido, Map<String, Object> datos) {
        // Actualizar descripción si está disponible
        String descripcion = obtenerValorString(datos, "descripcion_normalizada", null);
        if (descripcion != null && !descripcion.isEmpty()) {
            contenido.setDescripcion(descripcion);
        }

        // Actualizar datos específicos según el tipo
        if (contenido.getTipo() == Contenido.Tipo.SERIE) {
            // Actualizar temporadas y episodios para series
            if (datos.containsKey("number_of_seasons")) {
                try {
                    Object seasons = datos.get("number_of_seasons");
                    if (seasons instanceof Number) {
                        contenido.setTemporadas(((Number) seasons).intValue());
                    }
                } catch (Exception e) {
                    logger.warning("Error al actualizar temporadas: " + e.getMessage());
                }
            }

            if (datos.containsKey("number_of_episodes")) {
                try {
                    Object episodes = datos.get("number_of_episodes");
                    if (episodes instanceof Number) {
                        contenido.setCapitulosTotales(((Number) episodes).intValue());
                    }
                } catch (Exception e) {
                    logger.warning("Error al actualizar episodios: " + e.getMessage());
                }
            }

            // Actualizar estado de emisión
            if (datos.containsKey("status")) {
                String status = obtenerValorString(datos, "status", "");
                contenido.setEnEmision(!"Ended".equalsIgnoreCase(status) && !"Canceled".equalsIgnoreCase(status));
            }
        }
    }

    private String obtenerValorString(Map<String, Object> datos, String clave, String valorPorDefecto) {
        Object valor = datos.get(clave);
        if (valor != null) {
            return valor.toString().trim();
        }
        return valorPorDefecto;
    }
}
