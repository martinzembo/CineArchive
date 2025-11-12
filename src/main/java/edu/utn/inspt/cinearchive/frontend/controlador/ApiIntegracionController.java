package edu.utn.inspt.cinearchive.frontend.controlador;

import edu.utn.inspt.cinearchive.backend.modelo.Contenido;
import edu.utn.inspt.cinearchive.backend.servicio.ApiExternaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controlador para integración con APIs externas (TMDb, OMDb)
 */
@Controller
@RequestMapping("/api-integracion")
public class ApiIntegracionController {

    private final ApiExternaService apiExternaService;

    @Autowired
    public ApiIntegracionController(ApiExternaService apiExternaService) {
        this.apiExternaService = apiExternaService;
    }

    // ==================== ENDPOINTS REST (JSON) ====================

    /**
     * Verificar conectividad con APIs externas
     */
    @GetMapping("/api/conectividad")
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> verificarConectividad() {
        try {
            Map<String, Boolean> conectividad = apiExternaService.verificarConectividadApis();
            return ResponseEntity.ok(conectividad);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ==================== BÚSQUEDA DE CONTENIDO ====================

    /**
     * Buscar películas en APIs externas
     */
    @GetMapping("/api/buscar/peliculas")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> buscarPeliculas(
            @RequestParam String titulo,
            @RequestParam(defaultValue = "20") int limite) {
        try {
            if (titulo == null || titulo.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            List<Map<String, Object>> resultados = apiExternaService.buscarPeliculas(titulo, limite);
            return ResponseEntity.ok(resultados);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Buscar series en APIs externas
     */
    @GetMapping("/api/buscar/series")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> buscarSeries(
            @RequestParam String titulo,
            @RequestParam(defaultValue = "20") int limite) {
        try {
            if (titulo == null || titulo.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            List<Map<String, Object>> resultados = apiExternaService.buscarSeries(titulo, limite);
            return ResponseEntity.ok(resultados);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Buscar contenido general (películas y series)
     */
    @GetMapping("/api/buscar/contenido")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> buscarContenido(
            @RequestParam String titulo,
            @RequestParam(defaultValue = "20") int limite) {
        try {
            if (titulo == null || titulo.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            List<Map<String, Object>> resultados = apiExternaService.buscarContenido(titulo, limite);
            return ResponseEntity.ok(resultados);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Obtener detalles de película
     */
    @GetMapping("/api/detalles/pelicula/{fuente}/{idExterno}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> obtenerDetallesPelicula(
            @PathVariable String fuente,
            @PathVariable String idExterno) {
        try {
            Optional<Map<String, Object>> detalles = apiExternaService.obtenerDetallesPelicula(idExterno, fuente);
            return detalles.map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Obtener detalles de serie
     */
    @GetMapping("/api/detalles/serie/{fuente}/{idExterno}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> obtenerDetallesSerie(
            @PathVariable String fuente,
            @PathVariable String idExterno) {
        try {
            Optional<Map<String, Object>> detalles = apiExternaService.obtenerDetallesSerie(idExterno, fuente);
            return detalles.map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ==================== IMPORTACIÓN DE CONTENIDO ====================

    /**
     * Importar película desde API externa
     */
    @PostMapping("/api/importar/pelicula")
    @ResponseBody
    public ResponseEntity<Contenido> importarPelicula(
            @RequestParam String idExterno,
            @RequestParam String fuente,
            @RequestParam Long gestorInventarioId) {
        try {
            Optional<Contenido> contenido = apiExternaService.importarPelicula(
                idExterno, fuente, gestorInventarioId);

            return contenido.map(c -> ResponseEntity.status(HttpStatus.CREATED).body(c))
                           .orElse(ResponseEntity.badRequest().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Importar serie desde API externa
     */
    @PostMapping("/api/importar/serie")
    @ResponseBody
    public ResponseEntity<Contenido> importarSerie(
            @RequestParam String idExterno,
            @RequestParam String fuente,
            @RequestParam Long gestorInventarioId) {
        try {
            Optional<Contenido> contenido = apiExternaService.importarSerie(
                idExterno, fuente, gestorInventarioId);

            return contenido.map(c -> ResponseEntity.status(HttpStatus.CREATED).body(c))
                           .orElse(ResponseEntity.badRequest().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Importar múltiples contenidos
     */
    @PostMapping("/api/importar/lote")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> importarContenidosLote(
            @RequestBody Map<String, Object> requestData) {
        try {
            @SuppressWarnings("unchecked")
            List<String> idsExternos = (List<String>) requestData.get("idsExternos");
            String fuente = (String) requestData.get("fuente");
            Long gestorInventarioId = ((Number) requestData.get("gestorInventarioId")).longValue();

            if (idsExternos == null || idsExternos.isEmpty() ||
                fuente == null || gestorInventarioId == null) {
                return ResponseEntity.badRequest().build();
            }

            List<Contenido> contenidosImportados = apiExternaService.importarContenidos(
                idsExternos, fuente, gestorInventarioId);

            Map<String, Object> resultado = Map.of(
                "total_solicitados", idsExternos.size(),
                "total_importados", contenidosImportados.size(),
                "contenidos", contenidosImportados
            );

            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ==================== ACTUALIZACIÓN Y SINCRONIZACIÓN ====================

    /**
     * Actualizar contenido desde API externa
     */
    @PutMapping("/api/actualizar/{contenidoId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> actualizarContenidoDesdeApi(
            @PathVariable Long contenidoId) {
        try {
            boolean actualizado = apiExternaService.actualizarContenidoDesdeApi(contenidoId);

            Map<String, Object> resultado = Map.of(
                "contenido_id", contenidoId,
                "actualizado", actualizado,
                "mensaje", actualizado ? "Contenido actualizado exitosamente" : "No se pudo actualizar el contenido"
            );

            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Sincronizar todos los contenidos con APIs externas
     */
    @PostMapping("/api/sincronizar")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> sincronizarTodosLosContenidos() {
        try {
            Map<String, Object> resultado = apiExternaService.sincronizarTodosLosContenidos();
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ==================== UTILIDADES Y CONFIGURACIÓN ====================

    /**
     * Obtener géneros disponibles de APIs externas
     */
    @GetMapping("/api/generos")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> obtenerGenerosDisponibles() {
        try {
            List<Map<String, Object>> generos = apiExternaService.obtenerGenerosDisponibles();
            return ResponseEntity.ok(generos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Obtener contenido popular de APIs externas
     */
    @GetMapping("/api/popular/{tipo}")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> obtenerContenidoPopular(
            @PathVariable String tipo,
            @RequestParam(defaultValue = "20") int limite) {
        try {
            List<Map<String, Object>> contenidoPopular = apiExternaService.obtenerContenidoPopular(tipo, limite);
            return ResponseEntity.ok(contenidoPopular);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Validar y normalizar datos de API
     */
    @PostMapping("/api/validar")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> validarYNormalizarDatos(
            @RequestBody Map<String, Object> datosApi,
            @RequestParam String fuente) {
        try {
            Map<String, Object> datosNormalizados = apiExternaService.validarYNormalizarDatos(datosApi, fuente);
            return ResponseEntity.ok(datosNormalizados);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ==================== VISTAS JSP ====================

    /**
     * Vista principal de integración de APIs
     */
    @GetMapping
    public String mostrarIntegracionApis(Model model) {
        try {
            // Verificar conectividad
            Map<String, Boolean> conectividad = apiExternaService.verificarConectividadApis();
            model.addAttribute("conectividad", conectividad);

            // Obtener géneros disponibles
            List<Map<String, Object>> generos = apiExternaService.obtenerGenerosDisponibles();
            model.addAttribute("generos", generos);

            return "api-integracion";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar la página de integración de APIs");
            return "error";
        }
    }

    /**
     * Vista de búsqueda de contenido
     */
    @GetMapping("/buscar")
    public String mostrarBusquedaContenido(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false, defaultValue = "contenido") String tipo,
            Model model) {
        try {
            if (titulo != null && !titulo.trim().isEmpty()) {
                List<Map<String, Object>> resultados;

                switch (tipo.toLowerCase()) {
                    case "pelicula":
                        resultados = apiExternaService.buscarPeliculas(titulo, 20);
                        break;
                    case "serie":
                        resultados = apiExternaService.buscarSeries(titulo, 20);
                        break;
                    default:
                        resultados = apiExternaService.buscarContenido(titulo, 20);
                }

                model.addAttribute("resultados", resultados);
                model.addAttribute("titulo", titulo);
                model.addAttribute("tipo", tipo);
            }

            return "busqueda-contenido";
        } catch (Exception e) {
            model.addAttribute("error", "Error al realizar la búsqueda");
            return "error";
        }
    }

    /**
     * Vista de importación masiva
     */
    @GetMapping("/importar")
    public String mostrarImportacionMasiva(Model model) {
        try {
            // Verificar conectividad
            Map<String, Boolean> conectividad = apiExternaService.verificarConectividadApis();
            model.addAttribute("conectividad", conectividad);

            // Obtener contenido popular para mostrar como opciones
            List<Map<String, Object>> peliculasPopulares = apiExternaService.obtenerContenidoPopular("movie", 10);
            List<Map<String, Object>> seriesPopulares = apiExternaService.obtenerContenidoPopular("tv", 10);

            model.addAttribute("peliculasPopulares", peliculasPopulares);
            model.addAttribute("seriesPopulares", seriesPopulares);

            return "importacion-masiva";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar la página de importación");
            return "error";
        }
    }

    /**
     * Vista de estado de sincronización
     */
    @GetMapping("/sincronizacion")
    public String mostrarEstadoSincronizacion(Model model) {
        try {
            // Verificar conectividad
            Map<String, Boolean> conectividad = apiExternaService.verificarConectividadApis();
            model.addAttribute("conectividad", conectividad);

            return "estado-sincronizacion";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar el estado de sincronización");
            return "error";
        }
    }
}
