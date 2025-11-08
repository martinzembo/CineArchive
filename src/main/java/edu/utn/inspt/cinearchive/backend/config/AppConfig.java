package edu.utn.inspt.cinearchive.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

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
public class AppConfig {

    /**
     * Bean para validación de objetos usando Bean Validation (JSR-303)
     */
    @Bean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }
}
