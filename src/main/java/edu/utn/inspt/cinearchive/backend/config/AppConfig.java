package edu.utn.inspt.cinearchive.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.client.RestTemplate;

import javax.validation.Validator;

/**
 * Configuración principal de Spring MVC
 * Define los componentes principales de la aplicación
 */
@Configuration
@ComponentScan(basePackages = {
    "edu.utn.inspt.cinearchive.frontend.controlador",
    "edu.utn.inspt.cinearchive.backend.servicio",
    "edu.utn.inspt.cinearchive.backend.repositorio",
    "edu.utn.inspt.cinearchive.backend.config"
})
@PropertySource("classpath:application.properties")
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
        return new LocalValidatorFactoryBean();
    }

    /**
     * RestTemplate para consumir APIs externas (ej: TMDB, OMDB)
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
