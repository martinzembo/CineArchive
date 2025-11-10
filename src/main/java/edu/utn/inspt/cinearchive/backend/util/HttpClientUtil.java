package edu.utn.inspt.cinearchive.backend.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Utilidad para realizar llamadas HTTP a APIs externas
 */
@Component
public class HttpClientUtil {

    private static final Logger logger = Logger.getLogger(HttpClientUtil.class.getName());

    private static final int TIMEOUT_SECONDS = 30;

    private final CloseableHttpClient httpClient;
    private final RequestConfig requestConfig;

    public HttpClientUtil() {
        // Configuración de timeouts
        this.requestConfig = RequestConfig.custom()
                .setConnectTimeout(TIMEOUT_SECONDS * 1000)
                .setSocketTimeout(TIMEOUT_SECONDS * 1000)
                .setConnectionRequestTimeout(TIMEOUT_SECONDS * 1000)
                .build();

        // Crear cliente HTTP con configuración
        this.httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();
    }

    /**
     * Realiza una petición GET HTTP
     *
     * @param url URL a la que hacer la petición
     * @return Respuesta JSON como String, null si hay error
     */
    public String executeGet(String url) {
        if (url == null || url.trim().isEmpty()) {
            logger.warning("URL no puede estar vacía");
            return null;
        }

        HttpGet request = new HttpGet(url);
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-Type", "application/json");
        request.setHeader("User-Agent", "CineArchive/1.0");

        try (CloseableHttpResponse response = httpClient.execute(request)) {

            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String responseBody = EntityUtils.toString(entity);
                    logger.info("Petición exitosa a: " + url);
                    return responseBody;
                }
            } else if (statusCode == HttpStatus.SC_NOT_FOUND) {
                logger.warning("Recurso no encontrado (404) para URL: " + url);
                return null;
            } else if (statusCode == HttpStatus.SC_UNAUTHORIZED) {
                logger.severe("Error de autenticación (401) para URL: " + url);
                return null;
            } else if (statusCode == HttpStatus.SC_FORBIDDEN) {
                logger.severe("Error de permisos (403) para URL: " + url);
                return null;
            } else if (statusCode >= 500) {
                logger.severe("Error del servidor (" + statusCode + ") para URL: " + url);
                return null;
            } else {
                logger.warning("Código de estado inesperado (" + statusCode + ") para URL: " + url);
                return null;
            }

        } catch (IOException e) {
            logger.severe("Error de conexión al hacer petición a: " + url + " - " + e.getMessage());
            return null;
        } catch (Exception e) {
            logger.severe("Error inesperado al hacer petición a: " + url + " - " + e.getMessage());
            return null;
        }

        return null;
    }

    /**
     * Realiza una petición GET HTTP con parámetros adicionales en headers
     *
     * @param url URL a la que hacer la petición
     * @param apiKey API Key para autenticación
     * @return Respuesta JSON como String, null si hay error
     */
    public String executeGetWithApiKey(String url, String apiKey) {
        if (url == null || url.trim().isEmpty()) {
            logger.warning("URL no puede estar vacía");
            return null;
        }

        HttpGet request = new HttpGet(url);
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-Type", "application/json");
        request.setHeader("User-Agent", "CineArchive/1.0");

        // Agregar API Key si está presente
        if (apiKey != null && !apiKey.trim().isEmpty()) {
            request.setHeader("Authorization", "Bearer " + apiKey);
        }

        try (CloseableHttpResponse response = httpClient.execute(request)) {

            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String responseBody = EntityUtils.toString(entity);
                    logger.info("Petición exitosa con API Key a: " + url);
                    return responseBody;
                }
            } else {
                logger.warning("Error en petición con API Key (" + statusCode + ") para URL: " + url);
                return null;
            }

        } catch (IOException e) {
            logger.severe("Error de conexión al hacer petición con API Key a: " + url + " - " + e.getMessage());
            return null;
        } catch (Exception e) {
            logger.severe("Error inesperado al hacer petición con API Key a: " + url + " - " + e.getMessage());
            return null;
        }

        return null;
    }

    /**
     * Valida si una URL es válida
     *
     * @param url URL a validar
     * @return true si la URL es válida, false en caso contrario
     */
    public boolean isValidUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            return false;
        }

        try {
            return url.matches("^https?://.*");
        } catch (Exception e) {
            logger.warning("Error al validar URL: " + url + " - " + e.getMessage());
            return false;
        }
    }

    /**
     * Construye una URL con parámetros de query
     *
     * @param baseUrl URL base
     * @param params Parámetros como "param1=valor1&param2=valor2"
     * @return URL completa con parámetros
     */
    public String buildUrlWithParams(String baseUrl, String params) {
        if (baseUrl == null || baseUrl.trim().isEmpty()) {
            return null;
        }

        if (params == null || params.trim().isEmpty()) {
            return baseUrl;
        }

        String separator = baseUrl.contains("?") ? "&" : "?";
        return baseUrl + separator + params;
    }

    /**
     * Cierra el cliente HTTP (llamar al destruir el bean)
     */
    public void close() {
        try {
            if (httpClient != null) {
                httpClient.close();
                logger.info("Cliente HTTP cerrado correctamente");
            }
        } catch (IOException e) {
            logger.warning("Error al cerrar cliente HTTP: " + e.getMessage());
        }
    }
}
