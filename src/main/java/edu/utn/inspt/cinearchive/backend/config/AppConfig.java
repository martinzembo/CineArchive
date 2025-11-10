package edu.utn.inspt.cinearchive.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.client.RestTemplate;

import javax.validation.Validator;

// Agrego import para interpolador sin EL
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

/**
 * Configuración principal de Spring MVC
 * Define los componentes principales de la aplicación
 */
@Configuration
@ComponentScan(basePackages = {
    // NOTA: Los controladores los escanea el contexto del DispatcherServlet
    // (WebMvcConfig). Evitamos escanearlos aquí para que queden registrados
    // en el servlet context y no en el root application context, lo que
    // previene problemas donde DispatcherServlet no detecta los controllers
    // y se producen 404 en rutas mapeadas por @Controller.
    "edu.utn.inspt.cinearchive.backend.servicio",
    "edu.utn.inspt.cinearchive.backend.repositorio",
    "edu.utn.inspt.cinearchive.backend.config"
})
@PropertySource("classpath:application.properties")
@EnableScheduling
@EnableCaching
public class AppConfig {

    /**
     * Bean para procesar @Value annotations con property placeholders
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    /**
     * Bean para validación de objetos usando Bean Validation (JSR-303)
     */
    @Bean
    public Validator validator() {
        LocalValidatorFactoryBean v = new LocalValidatorFactoryBean();
        // Evitar dependencia de javax.el usando el interpolador de parámetros
        v.setMessageInterpolator(new ParameterMessageInterpolator());
        return v;
    }

    /**
     * RestTemplate para consumir APIs externas (ej: TMDB, OMDB)
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    // Cache simple en memoria para catálogo y conteos
    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("catalogoPagedLight", "catalogoCount");
    }
}
