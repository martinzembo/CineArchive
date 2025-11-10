package edu.utn.inspt.cinearchive.backend.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration;

/**
 * Inicializador de la aplicación web sin web.xml
 * Reemplaza la configuración tradicional XML por configuración Java
 * Esta clase es detectada automáticamente por el contenedor Servlet 3.0+
 */
public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    /**
     * Configuración del contexto raíz de la aplicación
     * Incluye beans de servicio, repositorios y configuración de BD
     */
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[] {
            AppConfig.class,
            DatabaseConfig.class
        };
    }

    /**
     * Configuración del contexto web (DispatcherServlet)
     * Incluye controladores, view resolvers, interceptores, etc.
     */
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[] {
            WebMvcConfig.class
        };
    }

    /**
     * Mapeo del DispatcherServlet
     * "/" significa que manejará todas las peticiones
     */
    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }

    /**
     * Configuración adicional del DispatcherServlet
     * Habilita multipart para subida de archivos
     */
    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        // Configuración para subida de archivos (imágenes de perfil, etc.)
        // Tamaño máximo: 5MB por archivo, 10MB por request
        registration.setMultipartConfig(
            new MultipartConfigElement(
                null,           // Ubicación temporal (usa la del sistema)
                5242880,        // 5MB - tamaño máximo de archivo
                10485760,       // 10MB - tamaño máximo de request
                0               // Sin umbral para escribir en disco
            )
        );

        // Carga el servlet al iniciar la aplicación
        registration.setLoadOnStartup(1);
    }
}
